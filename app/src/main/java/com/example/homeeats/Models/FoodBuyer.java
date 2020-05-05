package com.example.homeeats.Models;

import com.google.android.gms.maps.model.LatLng;

public class FoodBuyer extends Client {
    public LatLng location;

    public FoodBuyer(String id, String name, String gender, String emailAddress, String phone, String photo, LatLng location) {
        super(id, name, gender, emailAddress, phone, photo);
        this.location = location;
    }
    public FoodBuyer(){super();}
}
