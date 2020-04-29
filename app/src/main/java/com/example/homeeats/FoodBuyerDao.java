package com.example.homeeats;


import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FoodBuyerDao extends Dao<FoodBuyer> {
    private static FoodBuyerDao singletonObject;
    private FoodBuyerDao(){
        super("FoodBuyers");
    }
    public static FoodBuyerDao GetInstance() {
        if (singletonObject == null)
            singletonObject = new FoodBuyerDao();
        return singletonObject;
    }

    @Override
    public FoodBuyer parseDataSnapshot(DataSnapshot dataSnapshot) {
        final FoodBuyer foodBuyer = new FoodBuyer();
        foodBuyer.id = dataSnapshot.getKey();
        foodBuyer.name = dataSnapshot.child("name").getValue().toString();
        foodBuyer.gender = dataSnapshot.child("gender").getValue().toString();
        foodBuyer.emailAddress = dataSnapshot.child("emailAddress").getValue().toString();
        foodBuyer.phone = dataSnapshot.child("phone").getValue().toString();
        double latitude = Double.parseDouble(dataSnapshot.child("location").child("latitude").getValue().toString());
        double longitude = Double.parseDouble(dataSnapshot.child("location").child("longitude").getValue().toString());
        foodBuyer.location = new LatLng(latitude, longitude);
        return foodBuyer;
    }

    @Override
    void delete(FoodBuyer foodBuyer) {

    }
}
