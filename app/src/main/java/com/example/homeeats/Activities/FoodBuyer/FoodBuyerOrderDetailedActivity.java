package com.example.homeeats.Activities.FoodBuyer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firbasedao.Listeners.RetrievalEventListener;
import com.example.homeeats.Adapters.FoodBuyerOrderDetailedItemsRecyclerAdapter;
import com.example.homeeats.Dao.DeliveryBoyDao;
import com.example.homeeats.Dao.FoodMakerDao;
import com.example.homeeats.Dao.OrderDao;
import com.example.homeeats.Models.DeliveryBoy;
import com.example.homeeats.Models.FoodMaker;
import com.example.homeeats.Models.Order;
import com.example.homeeats.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

public class FoodBuyerOrderDetailedActivity extends AppCompatActivity implements OnMapReadyCallback {
    CardView cardView;
    ImageView profileImage;
    TextView foodMakerName;
    TextView orderId;
    TextView orderStatus;
    TextView deliveryBoyName;
    TextView totalPrice;
    Order currentOrder;
    MapView mapView;
    GoogleMap googleMap;
    String deliveryBoyID;
    FoodBuyerOrderDetailedItemsRecyclerAdapter adapter;
    RecyclerView recyclerView;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.foodbuyer_order_detailed_activity);
            cardView = findViewById(R.id.foodBuyerOrderDetailedView);
            profileImage = findViewById(R.id.foodBuyerOrderDetailedMakerImage);
            foodMakerName = findViewById(R.id.foodBuyerOrderDetailedMakerName);
            orderId = findViewById(R.id.foodBuyerOrderDetailedID);
            orderStatus= findViewById(R.id.foodBuyerOrderDetailedStatus);
            deliveryBoyName = findViewById(R.id.foodBuyerOrderDetailsDeliveryBoyName);
            totalPrice = findViewById(R.id.foodBuyerOrderDetailsTotalPrice);
            mapView = findViewById(R.id.foodBuyerOrderDetailedMapView);
            recyclerView = findViewById(R.id.foodBuyerOrderDetailedRecyclerView);
            Intent intent = getIntent();
            String orderID = intent.getStringExtra("orderID");

            Bundle mapViewBundle = null;
            if (savedInstanceState != null) {
                mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
            }


            mapView.onCreate(mapViewBundle);
            mapView.getMapAsync(this);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(linearLayoutManager);

            OrderDao.GetInstance().get(orderID, new RetrievalEventListener<Order>() {
                @Override
                public void OnDataRetrieved(Order order) {
                    adapter = new FoodBuyerOrderDetailedItemsRecyclerAdapter(order.orderItems);
                    recyclerView.setAdapter(adapter);
                    currentOrder = order;
                    orderId.setText(order.id);
                    orderStatus.setText(order.orderStatus.toString());
                    totalPrice.setText(order.totalPrice.toString());

                    DeliveryBoyDao.GetInstance().get(order.deliveryBoyId, new RetrievalEventListener<DeliveryBoy>() {
                        @Override
                        public void OnDataRetrieved(DeliveryBoy deliveryBoy) {
                            deliveryBoyName.setText(deliveryBoy.name);
                        }
                    });
                    FoodMakerDao.GetInstance().get(order.foodMakerId, new RetrievalEventListener<FoodMaker>() {
                        @Override
                        public void OnDataRetrieved(FoodMaker foodMaker) {
                            foodMakerName.setText(foodMaker.name);
                            Picasso.get().load(foodMaker.photo).into(profileImage);
                        }
                    });
                }
            });
        }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    private RetrievalEventListener<DeliveryBoy> locationChangeListener = new RetrievalEventListener<DeliveryBoy>() {
        @Override
        public void OnDataRetrieved(DeliveryBoy deliveryBoy) {
                Log.d("LOCATION UPATED!! :))", deliveryBoy.location.toString());
                addMarkerLocation(deliveryBoy.location);
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                    new CameraPosition.Builder()
                            .target(deliveryBoy.location)
                            .zoom(15.0f)
                            .build()
            ));
        }
    };

    private void addMarkerLocation(LatLng latLng) {
        googleMap.clear();
        googleMap.addMarker(new MarkerOptions().position(latLng));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DeliveryBoyDao.GetInstance().RemoveDeliveryBoyLiveLocationListener(deliveryBoyID, locationChangeListener);
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        Intent intent = getIntent();
        String orderID = intent.getStringExtra("orderID");
        OrderDao.GetInstance().get(orderID, new RetrievalEventListener<Order>() {
            @Override
            public void OnDataRetrieved(Order order) {
                deliveryBoyID = order.deliveryBoyId;
                DeliveryBoyDao.GetInstance().AddDeliveryBoyLiveLocationListener(order.deliveryBoyId, locationChangeListener);
            }
        });
    }
}
