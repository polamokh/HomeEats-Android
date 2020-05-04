package com.example.homeeats;

import android.content.Context;
import android.location.Location;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.homeeats.Activities.MainActivity;
import com.example.homeeats.Listeners.RetrievalEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocationManager {
    private static LocationManager singletonObject;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationManager(Context context){
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        fusedLocationProviderClient = new FusedLocationProviderClient(context);
    }
    public static LocationManager GetInstance(Context context){
        if(singletonObject == null)
            singletonObject = new LocationManager(context);
        return singletonObject;
    }
    public void getLastLocation(final RetrievalEventListener<Location> retrievalEventListener){
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                retrievalEventListener.OnDataRetrieved(location);
            }
        });
    }
    public void setOnApplicationUpdatedListener(final RetrievalEventListener<Location> retrievalEventListener){
        LocationCallback locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location currentLocation = locationResult.getLastLocation();
                if(lastLocation == null || currentLocation.getLatitude() != lastLocation.getLatitude() || currentLocation.getLongitude() != lastLocation.getLongitude()) {
                    lastLocation = currentLocation;
                    retrievalEventListener.OnDataRetrieved(locationResult.getLastLocation());
                }
            }
        };
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }
}
