package com.example.homeeats.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.firbasedao.Listeners.RetrievalEventListener;
import com.example.homeeats.Dao.DeliveryBoyDao;
import com.example.homeeats.Dao.FoodBuyerDao;
import com.example.homeeats.Dao.FoodMakerDao;
import com.example.homeeats.LocationManager;
import com.example.homeeats.Models.DeliveryBoy;
import com.example.homeeats.Models.FoodBuyer;
import com.example.homeeats.Models.FoodMaker;
import com.example.homeeats.R;
import com.example.firbasedao.Listeners.TaskListener;
import com.example.homeeats.UserAuthenticationDatabase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Date;

public class SignUpActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        GoogleMap.OnMapClickListener {

    static String boyURL =
            "https://firebasestorage.googleapis.com/v0/b/home-eats-c2c26.appspot.com/o/default%20images%2Fboy-avatar.png?alt=media";
    static String girlURL =
            "https://firebasestorage.googleapis.com/v0/b/home-eats-c2c26.appspot.com/o/default%20images%2Fgirl-avatar.png?alt=media";


    EditText editTextSignUpName;
    EditText editTextSignUpEmail;
    EditText editTextSignUpPassword;
    Spinner spinnerSignUpUserGender;
    Spinner spinnerSignUpType;
    Button buttonSignUp;

    GoogleMap gMap;
    MapView mapView;

    LatLng location;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        editTextSignUpName = findViewById(R.id.editTextSignUpName);
        editTextSignUpEmail = findViewById(R.id.editTextSignUpEmail);
        editTextSignUpPassword = findViewById(R.id.editTextSignUpPassword);
        spinnerSignUpUserGender = findViewById(R.id.spinnerSignUpUserGender);
        spinnerSignUpType = findViewById(R.id.spinnerSignUpType);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        mapView = findViewById(R.id.mapViewSignUpLocation);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp(editTextSignUpName.getText().toString(),
                        editTextSignUpEmail.getText().toString(),
                        editTextSignUpPassword.getText().toString(),
                        spinnerSignUpUserGender.getSelectedItem().toString().toLowerCase(),
                        spinnerSignUpType.getSelectedItem().toString(),
                        location, new TaskListener() {
                            @Override
                            public void OnSuccess() {
                                SignUpActivity.this.onBackPressed();
                            }

                            @Override
                            public void OnFail() {

                            }
                        });


            }
        });

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
    }

    private void signUp(final String name, String email, String password, String gender, String type,
                        LatLng location, final TaskListener taskListener) {
        UserAuthenticationDatabase userAuthenticationDatabase = UserAuthenticationDatabase.GetInstance();
        //Food Buyer
        if (type.equals(getResources().getStringArray(R.array.user_type)[0])) {
            final FoodBuyer foodBuyer = new FoodBuyer(null, name, gender, email, "011",
                    (gender.equals("male") ? boyURL : girlURL), location);
            userAuthenticationDatabase.SignUpFoodBuyer(
                    foodBuyer,
                    password,
                    new TaskListener() {
                        @Override
                        public void OnSuccess() {
                            Toast.makeText(getApplicationContext(), foodBuyer.id,
                                    Toast.LENGTH_SHORT).show();
                            taskListener.OnSuccess();
                        }

                        @Override
                        public void OnFail() {
                            Toast.makeText(getApplicationContext(), "Sign up failed",
                                    Toast.LENGTH_SHORT).show();
                            taskListener.OnFail();
                        }
                    });
        }
        //Food Maker
        else if (type.equals(getResources().getStringArray(R.array.user_type)[1])) {
            final FoodMaker foodMaker = new FoodMaker(null, name, gender, email, "011",
                    (gender.equals("male") ? boyURL : girlURL), location, 5);
            userAuthenticationDatabase.SignUpFoodMaker(
                    foodMaker,
                    password,
                    new TaskListener() {
                        @Override
                        public void OnSuccess() {
                            Toast.makeText(getApplicationContext(), foodMaker.id,
                                    Toast.LENGTH_SHORT).show();
                            taskListener.OnSuccess();
                        }

                        @Override
                        public void OnFail() {
                            Toast.makeText(getApplicationContext(), "Sign up failed",
                                    Toast.LENGTH_SHORT).show();
                            taskListener.OnFail();
                        }
                    });
        }
        //Delivery Boy
        else if (type.equals(getResources().getStringArray(R.array.user_type)[2])) {
            final DeliveryBoy deliveryBoy = new DeliveryBoy(null, name, gender, email,
                    "011", (gender.equals("male") ? boyURL : girlURL),
                    true, location, new Date().getTime());
            userAuthenticationDatabase.SignUpDeliveryBoy(
                    deliveryBoy,
                    password,
                    new TaskListener() {
                        @Override
                        public void OnSuccess() {
                            Toast.makeText(getApplicationContext(), "Signed up",
                                    Toast.LENGTH_SHORT).show();
                            taskListener.OnSuccess();
                        }

                        @Override
                        public void OnFail() {
                            Toast.makeText(getApplicationContext(), "Sign up failed",
                                    Toast.LENGTH_SHORT).show();
                            taskListener.OnFail();
                        }
                    });
        } else {
            Toast.makeText(this, "Invalid type", Toast.LENGTH_SHORT).show();
            taskListener.OnFail();
        }
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
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        addMarkerLocation(latLng);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(getApplicationContext(), "Click on MAP to mark your current location",
                Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        addMarkerLocation(new LatLng(location.getLatitude(), location.getLongitude()));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        gMap.setOnMapClickListener(this);
        gMap.setOnMyLocationClickListener(this);
        gMap.setOnMyLocationButtonClickListener(this);

        gMap.setMyLocationEnabled(true);

        gMap.getUiSettings().setMyLocationButtonEnabled(true);
        gMap.getUiSettings().setZoomControlsEnabled(true);

        LocationManager.GetInstance(getApplicationContext()).
                getLastLocation(new RetrievalEventListener<Location>() {
                    @Override
                    public void OnDataRetrieved(Location location) {
                        addMarkerLocation(new LatLng(location.getLatitude(), location.getLongitude()));
                        gMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                                new CameraPosition.Builder()
                                        .target(new LatLng(location.getLatitude(), location.getLongitude()))
                                        .zoom(15.0f)
                                        .build()
                        ));
                    }
                });
    }

    private void addMarkerLocation(LatLng latLng) {
        gMap.clear();
        location = latLng;
        gMap.addMarker(new MarkerOptions().position(latLng));
    }

    Bitmap getBitmapFromDrawable(Drawable drawable) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        return bitmapDrawable.getBitmap();
    }
}
