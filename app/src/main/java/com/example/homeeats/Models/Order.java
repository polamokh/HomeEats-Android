package com.example.homeeats.Models;

import com.example.homeeats.Dao.DeliveryBoyDao;
import com.example.homeeats.Dao.FoodBuyerDao;
import com.example.homeeats.Dao.FoodMakerDao;
import com.example.homeeats.Listeners.RetrievalEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;

import java.util.List;

public class Order {
    @Exclude
    public String id;
    public String foodMakerId;
    public String foodBuyerId;
    public String deliveryBoyId;
    public List<OrderItem> orderItems;
    public String review;
    public Integer rating;
    public Double totalPrice;
    public OrderStatus orderStatus;
    public LatLng buyerLocation;

    public Order(String id, String foodMakerId, String foodBuyerId, String deliveryBoyId, List<OrderItem> orderItems, String review, Integer rating, Double totalPrice, OrderStatus orderStatus, LatLng buyerLocation) {
        this.id = id;
        this.foodMakerId = foodMakerId;
        this.foodBuyerId = foodBuyerId;
        this.deliveryBoyId = deliveryBoyId;
        this.orderItems = orderItems;
        this.review = review;
        this.rating = rating;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
        this.buyerLocation = buyerLocation;
    }

    public Order(){}

    public void GetFoodMaker(final RetrievalEventListener<FoodMaker> retrievalEventListener)
    {
        FoodMakerDao.GetInstance().get(foodMakerId, new RetrievalEventListener<FoodMaker>() {
            @Override
            public void OnDataRetrieved(FoodMaker foodMaker) {
                retrievalEventListener.OnDataRetrieved(foodMaker);
            }
        });
    }

    public void GetFoodBuyer(final RetrievalEventListener<FoodBuyer> retrievalEventListener)
    {
        FoodBuyerDao.GetInstance().get(foodBuyerId, new RetrievalEventListener<FoodBuyer>() {
            @Override
            public void OnDataRetrieved(FoodBuyer foodBuyer) {
                retrievalEventListener.OnDataRetrieved(foodBuyer);
            }
        });
    }

    public void GetDeliveryBoy(final RetrievalEventListener<DeliveryBoy> retrievalEventListener)
    {
        DeliveryBoyDao.GetInstance().get(deliveryBoyId, new RetrievalEventListener<DeliveryBoy>() {
            @Override
            public void OnDataRetrieved(DeliveryBoy deliveryBoy) {
                retrievalEventListener.OnDataRetrieved(deliveryBoy);
            }
        });
    }

    private Double calculateTotalPrice(){
        double total = 0;
        for(OrderItem orderItem : orderItems)
            total += orderItem.totalPrice;
        return total;
    }
    public double getTotalPrice() {
        return calculateTotalPrice();
    }
}
