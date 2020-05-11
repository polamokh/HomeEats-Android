package com.example.homeeats.Activities.FoodBuyer;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firbasedao.Listeners.RetrievalEventListener;
import com.example.homeeats.Adapters.FoodBuyerMealsRecyclerAdapter;
import com.example.homeeats.Dao.FoodMakerDao;
import com.example.homeeats.Models.FoodMaker;
import com.example.homeeats.Models.MealItem;
import com.example.homeeats.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodBuyerViewMakerActivity extends AppCompatActivity implements OnMapReadyCallback {
    MapView mapView;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foodbuyer_view_maker_activity);

        final ImageView imageView = findViewById(R.id.foodBuyerViewMakerImage);
        final TextView textViewName = findViewById(R.id.foodBuyerViewMakerName);
        final TextView textViewPhone = findViewById(R.id.foodBuyerViewMakerPhone);
        final TextView textViewEmail = findViewById(R.id.foodBuyerViewMakerEmail);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        final RecyclerView recyclerView = findViewById(R.id.foodBuyerViewMakerRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        String makerID = getIntent().getExtras().getString("FoodMakerID");
        FoodMakerDao.GetInstance().get(makerID, new RetrievalEventListener<FoodMaker>() {
                    @Override
                    public void OnDataRetrieved(FoodMaker foodMaker) {
                        textViewName.setText(foodMaker.name);
                        textViewPhone.setText(foodMaker.phone);
                        textViewEmail.setText(foodMaker.emailAddress);
                        Picasso.get().load(foodMaker.photo).into(imageView);
                    }
                });

        FoodMakerDao.GetInstance().GetFoodMakerMeals(makerID, new RetrievalEventListener<List<MealItem>>() {
                    @Override
                    public void OnDataRetrieved(List<MealItem> mealItems) {
                        FoodBuyerMealsRecyclerAdapter adapter =
                                new FoodBuyerMealsRecyclerAdapter(mealItems, getSupportFragmentManager());
                        recyclerView.setAdapter(adapter);
                    }
                });

        mapView = findViewById(R.id.foodBuyerViewMakerMap);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
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
        mapView.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMap.setMinZoomPreference(5);
        FoodMakerDao.GetInstance().get(getIntent().getExtras().getString("FoodMakerID"),
                new RetrievalEventListener<FoodMaker>() {
                    @Override
                    public void OnDataRetrieved(FoodMaker foodMaker) {
                        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                        googleMap.addMarker(new MarkerOptions().position(foodMaker.location));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(foodMaker.location));
                    }
                });
    }
}
