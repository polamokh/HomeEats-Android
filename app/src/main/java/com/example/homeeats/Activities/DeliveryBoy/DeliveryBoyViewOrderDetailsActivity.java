package com.example.homeeats.Activities.DeliveryBoy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firbasedao.Listeners.RetrievalEventListener;
import com.example.firbasedao.Listeners.TaskListener;
import com.example.homeeats.Dao.DeliveryBoyDao;
import com.example.homeeats.Dao.FoodBuyerDao;
import com.example.homeeats.Dao.FoodMakerDao;
import com.example.homeeats.Dao.OrderDao;
import com.example.homeeats.Models.DeliveryBoy;
import com.example.homeeats.Models.FoodBuyer;
import com.example.homeeats.Models.FoodMaker;
import com.example.homeeats.Models.Order;
import com.example.homeeats.Models.OrderStatus;
import com.example.homeeats.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DeliveryBoyViewOrderDetailsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        GoogleMap.OnMapClickListener {

    private GoogleMap gMap;
    private LatLng markerLocation;
    LatLng Location = new LatLng(1, 1);
    Order currentOrder;
    FoodBuyer buyer;
    FoodMaker maker;
    MapView Locations;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_boy_view_order_details);
        final TextView MakerNumber = (TextView) findViewById(R.id.DeliveryBoyRequestMakerPhoneNumber);
        final TextView BuyerNumber = (TextView) findViewById(R.id.DeliveryBoyRequestBuyerPhoneNumber);
        final TextView Status = (TextView) findViewById(R.id.DeliveryBoyRequestMakerStatus);
        Locations = (MapView) findViewById(R.id.DeliverBoyViewOrderMap);
        final TextView Price = (TextView) findViewById(R.id.DeliveryBoyRequestPrice);
        final ImageView Delivering = (ImageView) findViewById(R.id.Delivering);
        final ImageView Delivered = (ImageView) findViewById(R.id.Delivered);
        OrderDao.GetInstance().get(getIntent().getExtras().getString("OrderID"), new RetrievalEventListener<Order>() {

            @Override
            public void OnDataRetrieved(Order order) {
                currentOrder = order;
                if (currentOrder.orderStatus == OrderStatus.Delivering) {
                    Delivering.setVisibility(View.INVISIBLE);
                    Delivered.setVisibility(View.VISIBLE);
                } else if (currentOrder.orderStatus == OrderStatus.Delivered) {
                    Delivering.setVisibility(View.INVISIBLE);
                    Delivered.setVisibility(View.INVISIBLE);
                }
            }
        });

        Delivering.setOnClickListener(new View.OnClickListener() {
            String OrderID = getIntent().getExtras().getString("OrderID");

            @Override
            public void onClick(View view) {
                OrderDao.GetInstance().get(OrderID, new RetrievalEventListener<Order>() {
                    @Override
                    public void OnDataRetrieved(final Order order) {
                        order.orderStatus = OrderStatus.getValue("Delivering");
                        Status.setText("Delivering");
                        Delivering.setVisibility(View.INVISIBLE);
                        OrderDao.GetInstance().save(order, order.id, new TaskListener() {
                            @Override
                            public void OnSuccess() {
                                Toast.makeText(getApplicationContext(), "Status Updated Successfully", Toast.LENGTH_LONG).show();
                                OrderDao.GetInstance().SendOrderNotifications(order.id, "Order Is Delivering", "Your Order is Currently on Its way");
                            }

                            @Override
                            public void OnFail() {
                                Toast.makeText(getApplicationContext(), "Failed to Update Status", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }


        });

        Delivered.setOnClickListener(new View.OnClickListener() {
            String OrderID = getIntent().getExtras().getString("OrderID");

            @Override
            public void onClick(View view) {
                OrderDao.GetInstance().get(OrderID, new RetrievalEventListener<Order>() {
                    @Override
                    public void OnDataRetrieved(final Order order) {
                        order.orderStatus = OrderStatus.getValue("Delivered");
                        OrderDao.GetInstance().save(order, order.id, new TaskListener() {
                            @Override
                            public void OnSuccess() {
                                Toast.makeText(getApplicationContext(), "Status Updated Successfully", Toast.LENGTH_LONG).show();
                                Delivered.setVisibility(View.INVISIBLE);
                                Delivering.setVisibility(View.INVISIBLE);
                                Status.setText("Delivered");
                                OrderDao.GetInstance().SendOrderNotifications(order.id, "Order Is Delivered", "Your Order is Now Here");
                                DeliveryBoyDao.GetInstance().get(order.deliveryBoyId, new RetrievalEventListener<DeliveryBoy>() {
                                    @Override
                                    public void OnDataRetrieved(DeliveryBoy deliveryBoy) {
                                        deliveryBoy.availability = true;
                                        DeliveryBoyDao.GetInstance().save(deliveryBoy, deliveryBoy.id, new TaskListener() {
                                            @Override
                                            public void OnSuccess() {
                                                Toast.makeText(getApplicationContext(), "You are now free for another order", Toast.LENGTH_LONG).show();
                                            }

                                            @Override
                                            public void OnFail() {

                                            }
                                        });
                                    }
                                });


                            }

                            @Override
                            public void OnFail() {
                                Toast.makeText(getApplicationContext(), "Failed to Update Status", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }


        });
        final String OrderID = getIntent().getExtras().getString("OrderID");

        OrderDao.GetInstance().get(OrderID, new RetrievalEventListener<Order>() {


            @Override
            public void OnDataRetrieved(final Order order) {
                currentOrder = order;
                Status.setText(order.orderStatus.toString());
                Price.setText(order.totalPrice.toString());
                FoodMakerDao.GetInstance().get(order.foodMakerId, new RetrievalEventListener<FoodMaker>() {
                    @Override
                    public void OnDataRetrieved(FoodMaker foodMaker) {
                        maker = foodMaker;
                        MakerNumber.setText(foodMaker.phone);
                        if (order.orderStatus == OrderStatus.Accepted) {
                            Location = foodMaker.location;
                        }
                    }
                });
                FoodBuyerDao.GetInstance().get(order.foodBuyerId, new RetrievalEventListener<FoodBuyer>() {
                    @Override
                    public void OnDataRetrieved(FoodBuyer foodBuyer) {
                        buyer = foodBuyer;
                        BuyerNumber.setText(foodBuyer.phone);
                        if (order.orderStatus == OrderStatus.Delivered) {
                            Location = foodBuyer.location;
                        }
                    }

                });

            }
        });
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        Locations.onCreate(mapViewBundle);
        Locations.getMapAsync(this);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        Locations.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        Locations.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        Locations.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        Locations.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        Locations.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Locations.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Locations.onLowMemory();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        gMap = googleMap;

        gMap.setOnMapClickListener(this);
        gMap.setOnMyLocationClickListener(this);
        gMap.setOnMyLocationButtonClickListener(this);

        gMap.setMyLocationEnabled(true);

        gMap.getUiSettings().setMyLocationButtonEnabled(true);
        gMap.getUiSettings().setZoomControlsEnabled(true);
        addMarkerLocation(Location);
        gMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder()
                        .target(Location)
                        .zoom(15.0f)
                        .build()));

        OrderDao.GetInstance().get(getIntent().getExtras().getString("OrderID"),
                new RetrievalEventListener<Order>() {
                    @Override
                    public void OnDataRetrieved(Order order) {
                        if (currentOrder.orderStatus == OrderStatus.Accepted ||
                                currentOrder.orderStatus == OrderStatus.Pending ||
                                currentOrder.orderStatus == OrderStatus.Making) {
                            if (maker == null) {
                                FoodMakerDao.GetInstance().get(order.foodMakerId,
                                        new RetrievalEventListener<FoodMaker>() {
                                            @Override
                                            public void OnDataRetrieved(FoodMaker foodMaker) {
                                                maker = foodMaker;
                                                addMarkerLocation(maker.location);
                                                gMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                                                        new CameraPosition.Builder()
                                                                .target(maker.location)
                                                                .zoom(15.0f)
                                                                .build()));
                                            }
                                        });
                            } else {
                                addMarkerLocation(maker.location);
                                gMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                                        new CameraPosition.Builder()
                                                .target(maker.location)
                                                .zoom(15.0f)
                                                .build()
                                ));
                            }
                        } else {
                            if (buyer == null) {
                                FoodBuyerDao.GetInstance().get(order.foodBuyerId,
                                        new RetrievalEventListener<FoodBuyer>() {
                                            @Override
                                            public void OnDataRetrieved(FoodBuyer foodBuyer) {
                                                buyer = foodBuyer;
                                                addMarkerLocation(buyer.location);
                                                gMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                                                        new CameraPosition.Builder()
                                                                .target(buyer.location)
                                                                .zoom(15.0f)
                                                                .build()));
                                            }
                                        });
                            } else {
                                addMarkerLocation(buyer.location);
                                gMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                                        new CameraPosition.Builder()
                                                .target(buyer.location)
                                                .zoom(15.0f)
                                                .build()
                                ));
                            }
                        }
                    }
                });
    }


    private void addMarkerLocation(LatLng latLng) {
        gMap.clear();

        markerLocation = latLng;
        gMap.addMarker(new MarkerOptions().position(markerLocation));
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }


}
