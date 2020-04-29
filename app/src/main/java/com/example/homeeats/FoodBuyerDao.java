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
    private FoodBuyerDao(){}
    public static FoodBuyerDao GetInstance() {
        if (singletonObject == null)
            singletonObject = new FoodBuyerDao();
        return singletonObject;
    }

    @Override
    public void get(final String id, final RetrievalEventListener<FoodBuyer> retrievalEventListener) {
        DatabaseReference rowReference = dbReference.child("FoodBuyers").child(id);
        rowReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final FoodBuyer foodBuyer = new FoodBuyer();
                foodBuyer.name = dataSnapshot.child("name").getValue().toString();
                foodBuyer.gender = dataSnapshot.child("gender").getValue().toString();
                foodBuyer.emailAddress = dataSnapshot.child("emailAddress").getValue().toString();
                foodBuyer.phone = dataSnapshot.child("phone").getValue().toString();
                double latitude = Double.parseDouble(dataSnapshot.child("location").child("latitude").getValue().toString());
                double longitude = Double.parseDouble(dataSnapshot.child("location").child("longitude").getValue().toString());
                foodBuyer.location = new LatLng(latitude, longitude);
                retrievalEventListener.OnDataRetrieved(foodBuyer);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void getAll(final RetrievalEventListener<List<FoodBuyer>> retrievalEventListener){
        DatabaseReference rowReference = dbReference.child("FoodBuyers");
        rowReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<FoodBuyer> foodBuyers = new ArrayList<FoodBuyer>();
                for(DataSnapshot currentSnapshot : dataSnapshot.getChildren())
                {
                    final FoodBuyer foodBuyer = new FoodBuyer();
                    foodBuyer.name = currentSnapshot.child("name").getValue().toString();
                    foodBuyer.gender = currentSnapshot.child("gender").getValue().toString();
                    foodBuyer.emailAddress = currentSnapshot.child("emailAddress").getValue().toString();
                    foodBuyer.phone = currentSnapshot.child("phone").getValue().toString();
                    double latitude = Double.parseDouble(currentSnapshot.child("location").child("latitude").getValue().toString());
                    double longitude = Double.parseDouble(currentSnapshot.child("location").child("longitude").getValue().toString());
                    foodBuyer.location = new LatLng(latitude, longitude);
                    foodBuyers.add(foodBuyer);
                }
                retrievalEventListener.OnDataRetrieved(foodBuyers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    void save(FoodBuyer foodBuyer) {
        dbReference.child("FoodBuyers").child(foodBuyer.id).setValue(foodBuyer);
    }

    @Override
    void delete(FoodBuyer foodBuyer) {
        
    }
}
