package com.example.homeeats.Activities.FoodMaker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homeeats.Dao.FoodBuyerDao;
import com.example.homeeats.Dao.FoodMakerDao;
import com.example.homeeats.Models.FoodBuyer;
import com.example.homeeats.Models.FoodMaker;
import com.example.homeeats.R;
import com.example.firbasedao.Listeners.RetrievalEventListener;
import com.example.firbasedao.Listeners.TaskListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class FoodMakerEditProfileFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        GoogleMap.OnMapClickListener {
    private static FoodMaker maker;

    private ImageView imageView;
    private Bitmap image;

    private MapView mapView;
    private GoogleMap gMap;
    private LatLng markerLocation;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST = 1888;
    private static int PICK_IMAGE_REQUEST = 1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_food_maker_edit_profile, container, false);

        imageView = view.findViewById(R.id.foodMakerSettingsImageView);
        final EditText editTextName = view.findViewById(R.id.foodMakerSettingsEditTextName);
        final EditText editTextMobile = view.findViewById(R.id.foodMakerSettingsEditTextMobile);
        final Spinner spinnerGender = view.findViewById(R.id.foodMakerSettingsSpinnerGender);

        final String foodmakerID = getActivity().getIntent().getExtras().getString("FoodMakerID");

        if (maker == null) {
            FoodMakerDao.GetInstance().get(foodmakerID, new RetrievalEventListener<FoodMaker>() {
                @Override
                public void OnDataRetrieved(FoodMaker foodMaker)
                {
                    maker = foodMaker;

                    editTextName.setText(maker.name);
                    editTextMobile.setText(maker.phone);
                    spinnerGender.setSelection(getGenderIndex(maker.gender));
                    Picasso.get()
                            .load(maker.photo)
                            .into(imageView, new Callback()
                            {
                                @Override
                                public void onSuccess() {
                                    image = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                                }

                                @Override
                                public void onError(Exception e) {

                                }
                            });
                }




            });
        }

        else {
            editTextName.setText(maker.name);
            editTextMobile.setText(maker.phone);
            spinnerGender.setSelection(getGenderIndex(maker.gender));
            Picasso.get()
                    .load(maker.photo)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            image = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
        }

        Button buttonBrowse = view.findViewById(R.id.foodMakerSettingsImageButtonBrowse);
        Button buttonCamera = view.findViewById(R.id.foodMakerSettingsImageButtonCamera);

        buttonBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PermissionChecker.checkSelfPermission(view.getContext()
                        , Manifest.permission.CAMERA) != PermissionChecker.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });

        Button buttonSave = view.findViewById(R.id.foodMakerSettingsButtonSave);
        TextView textViewDiscard = view.findViewById(R.id.foodMakerSettingsTextViewDiscard);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                maker.name = editTextName.getText().toString();
                maker.phone = editTextMobile.getText().toString();
                maker.gender = spinnerGender.getSelectedItem().toString().toLowerCase();
                maker.location = markerLocation;

                Bitmap currentImage = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                if (currentImage != image) {
                    image = currentImage;
                    FoodMakerDao.GetInstance().save(maker, foodmakerID, image,
                            new TaskListener() {
                                @Override
                                public void OnSuccess() {
                                    Toast.makeText(v.getContext(), "Profile information updated successfully",
                                            Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void OnFail() {

                                }
                            });
                } else {
                    FoodMakerDao.GetInstance().save(maker,foodmakerID, new TaskListener() {
                        @Override
                        public void OnSuccess() {
                            Toast.makeText(v.getContext(), "Profile information updated successfully",
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void OnFail() {

                        }
                    });
                }
            }
        });

        textViewDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextName.setText(maker.name);
                editTextMobile.setText(maker.phone);
                spinnerGender.setSelection(getGenderIndex(maker.gender));
                imageView.setImageBitmap(image);

                addMarkerLocation(maker.location);
                gMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                        new CameraPosition.Builder()
                                .target(maker.location)
                                .zoom(15.0f)
                                .build()
                ));
            }
        });

        mapView = view.findViewById(R.id.foodMakerSettingsMapView);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        return view;
    }

    private int getGenderIndex(String gender) {
        if (gender.equals("male"))
            return 0;
        return 1;
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(getContext(), "Camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
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
    public void onMapReady(final GoogleMap googleMap) {
        gMap = googleMap;

        gMap.setOnMapClickListener(this);
        gMap.setOnMyLocationClickListener(this);
        gMap.setOnMyLocationButtonClickListener(this);

        gMap.setMyLocationEnabled(true);

        gMap.getUiSettings().setMyLocationButtonEnabled(true);
        gMap.getUiSettings().setZoomControlsEnabled(true);

        if (maker == null)
        {
            FoodMakerDao.GetInstance().get(getActivity().getIntent().getExtras()
                            .getString("FoodMakerID"),
                    new RetrievalEventListener<FoodMaker>() {
                        @Override
                        public void OnDataRetrieved(FoodMaker foodMaker) {
                            maker = foodMaker;

                            addMarkerLocation(maker.location);
                            gMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                                    new CameraPosition.Builder()
                                            .target(maker.location)
                                            .zoom(15.0f)
                                            .build()
                            ));
                        }

                    });
        }

        else {
            addMarkerLocation(maker.location);
            gMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                    new CameraPosition.Builder()
                            .target(maker.location)
                            .zoom(15.0f)
                            .build()
            ));
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(getContext(), "Click on MAP to mark your current location",
                Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        addMarkerLocation(latLng);
    }

    private void addMarkerLocation(LatLng latLng) {
        gMap.clear();

        markerLocation = latLng;
        gMap.addMarker(new MarkerOptions().position(markerLocation));
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        addMarkerLocation(new LatLng(location.getLatitude(), location.getLongitude()));
    }




}
