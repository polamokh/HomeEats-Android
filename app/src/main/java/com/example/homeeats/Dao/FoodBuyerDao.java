package com.example.homeeats.Dao;


import android.graphics.Bitmap;

import com.example.firbasedao.FirebaseDao;
import com.example.firbasedao.Listeners.RetrievalEventListener;
import com.example.firbasedao.Listeners.TaskListener;
import com.example.homeeats.FilesStorageDatabase;
import com.example.homeeats.Models.FoodBuyer;
import com.example.homeeats.Models.Order;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FoodBuyerDao extends FirebaseDao<FoodBuyer> {
    private static FoodBuyerDao singletonObject;
    private FoodBuyerDao(){
        super("FoodBuyers");
    }
    public static FoodBuyerDao GetInstance() {
        if (singletonObject == null)
            singletonObject = new FoodBuyerDao();
        return singletonObject;
    }

    public void save(final FoodBuyer foodBuyer, final String id, Bitmap bitmap, final TaskListener taskListener){
        if(bitmap == null){
            save(foodBuyer, id, taskListener);
            return;
        }
        setFoodBuyerImage(id, bitmap, new RetrievalEventListener<String>() {
            @Override
            public void OnDataRetrieved(String s) {
                foodBuyer.photo = s;
                save(foodBuyer, id, taskListener);
            }
        });
    }

    public void setFoodBuyerImage(final String foodBuyerId, Bitmap image, final RetrievalEventListener<String> retrievalEventListener){
        FilesStorageDatabase.GetInstance().uploadPhoto(image, String.format("food buyers images/%s.jpg", foodBuyerId),
                new RetrievalEventListener<String>() {
                    @Override
                    public void OnDataRetrieved(String uploadPath) {
                        dbReference.child(tableName).child(foodBuyerId).child("photo").setValue(uploadPath);
                        retrievalEventListener.OnDataRetrieved(uploadPath);
                    }
                });
    }

    @Override
    protected void parseDataSnapshot(DataSnapshot dataSnapshot, RetrievalEventListener<FoodBuyer> retrievalEventListener) {
        final FoodBuyer foodBuyer = new FoodBuyer();
        foodBuyer.id = dataSnapshot.getKey();
        foodBuyer.name = dataSnapshot.child("name").getValue().toString();
        foodBuyer.gender = dataSnapshot.child("gender").getValue().toString();
        foodBuyer.emailAddress = dataSnapshot.child("emailAddress").getValue().toString();
        foodBuyer.phone = dataSnapshot.child("phone").getValue().toString();
        foodBuyer.photo = dataSnapshot.child("photo").getValue().toString();
        double latitude = Double.parseDouble(dataSnapshot.child("location").child("latitude").getValue().toString());
        double longitude = Double.parseDouble(dataSnapshot.child("location").child("longitude").getValue().toString());
        foodBuyer.location = new LatLng(latitude, longitude);
        retrievalEventListener.OnDataRetrieved(foodBuyer);
    }

    public void GetFoodBuyerOrders(final String foodBuyerId, final RetrievalEventListener<List<Order>> retrievalEventListener)
    {
        final List<Order> foodBuyerOrders = new ArrayList<>();
        OrderDao.GetInstance().getAll(new RetrievalEventListener<List<Order>>() {
            @Override
            public void OnDataRetrieved(List<Order> orders) {
                for(Order currentOrder : orders)
                {
                    if(currentOrder.foodBuyerId.equals(foodBuyerId))
                        foodBuyerOrders.add(currentOrder);
                }
                retrievalEventListener.OnDataRetrieved(foodBuyerOrders);
            }
        });
    }
}
