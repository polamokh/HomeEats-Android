package com.example.homeeats.Dao;


import com.example.homeeats.EventListenersListener;
import com.example.homeeats.Models.FoodBuyer;
import com.example.homeeats.Models.Order;
import com.example.homeeats.RetrievalEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;

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
    protected void parseDataSnapshot(DataSnapshot dataSnapshot, RetrievalEventListener<FoodBuyer> retrievalEventListener) {
        final FoodBuyer foodBuyer = new FoodBuyer();
        foodBuyer.id = dataSnapshot.getKey();
        foodBuyer.name = dataSnapshot.child("name").getValue().toString();
        foodBuyer.gender = dataSnapshot.child("gender").getValue().toString();
        foodBuyer.emailAddress = dataSnapshot.child("emailAddress").getValue().toString();
        foodBuyer.phone = dataSnapshot.child("phone").getValue().toString();
        double latitude = Double.parseDouble(dataSnapshot.child("location").child("latitude").getValue().toString());
        double longitude = Double.parseDouble(dataSnapshot.child("location").child("longitude").getValue().toString());
        foodBuyer.location = new LatLng(latitude, longitude);
        retrievalEventListener.OnDataRetrieved(foodBuyer);
    }

    public void GetFoodBuyerOrders(final String foodBuyerId, final RetrievalEventListener<List<Order>> retrievalEventListener)
    {
        final List<Order> orders = new ArrayList<>();
        OrderDao.GetInstance().getAll(new RetrievalEventListener<List<Order>>() {
            @Override
            public void OnDataRetrieved(List<Order> orders) {
                for(Order currentOrder : orders)
                {
                    if(currentOrder.foodBuyerId.equals(foodBuyerId))
                        orders.add(currentOrder);
                }
                retrievalEventListener.OnDataRetrieved(orders);
            }
        });
    }

    @Override
    void delete(FoodBuyer foodBuyer) {

    }
}
