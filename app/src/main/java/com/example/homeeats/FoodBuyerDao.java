package com.example.homeeats;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
    public void get(final AppCompatActivity appCompatActivity, final String id, final Method callback) {
        final FoodBuyer foodBuyer = new FoodBuyer();
        DatabaseReference rowReference = dbReference.child("FoodBuyers").child(id);
        rowReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                foodBuyer.name = dataSnapshot.child("name").getValue().toString();
                foodBuyer.gender = dataSnapshot.child("gender").getValue().toString();
                foodBuyer.emailAddress = dataSnapshot.child("emailAddress").getValue().toString();
                foodBuyer.phone = dataSnapshot.child("phone").getValue().toString();
                double latitude = Double.parseDouble(dataSnapshot.child("location").child("latitude").getValue().toString());
                double longitude = Double.parseDouble(dataSnapshot.child("location").child("longitude").getValue().toString());
                foodBuyer.location = new LatLng(latitude, longitude);
                try {
                    callback.invoke(appCompatActivity, new Object[]{foodBuyer});
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public List<FoodBuyer> getAll() throws Exception {
        return null;
    }

    @Override
    void save(FoodBuyer foodBuyer) {
        dbReference.child("FoodBuyers").child(foodBuyer.id).setValue(foodBuyer);
    }

    @Override
    void delete(FoodBuyer foodBuyer) {
        
    }
}
