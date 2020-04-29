package com.example.homeeats.Dao;

import com.example.homeeats.Models.FoodMaker;
import com.example.homeeats.Models.OrderStatus;
import com.example.homeeats.RetrievalEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;

public class FoodMakerDao extends Dao<FoodMaker> {
    private static FoodMakerDao singletonObject;
    private FoodMakerDao(){
        super("FoodMakers");
    }
    public static FoodMakerDao GetInstance() {
        if(singletonObject == null)
            singletonObject = new FoodMakerDao();
        return singletonObject;
    }

    @Override
    protected void parseDataSnapshot(DataSnapshot dataSnapshot, RetrievalEventListener<FoodMaker> retrievalEventListener) {
        final FoodMaker foodMaker = new FoodMaker();
        foodMaker.id = dataSnapshot.getKey();
        foodMaker.name = dataSnapshot.child("name").getValue().toString();
        foodMaker.gender = dataSnapshot.child("gender").getValue().toString();
        foodMaker.emailAddress = dataSnapshot.child("emailAddress").getValue().toString();
        foodMaker.phone = dataSnapshot.child("phone").getValue().toString();
        double latitude = Double.parseDouble(dataSnapshot.child("location").child("latitude").getValue().toString());
        double longitude = Double.parseDouble(dataSnapshot.child("location").child("longitude").getValue().toString());
        foodMaker.location = new LatLng(latitude, longitude);
        foodMaker.rating = Double.parseDouble(dataSnapshot.child("rating").getValue().toString());
        retrievalEventListener.OnDataRetrieved(foodMaker);
    }

    @Override
    void delete(FoodMaker foodMakerDao) {

    }
}
