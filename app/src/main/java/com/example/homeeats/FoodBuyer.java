package com.example.homeeats;

import com.google.android.gms.maps.model.LatLng;

public class FoodBuyer extends Client {
    public LatLng location;

    public FoodBuyer(String id, String name, String gender, String emailAddress, String phone, LatLng location) {
        super(id, name, gender, emailAddress, phone);
        this.location = location;
    }
}
