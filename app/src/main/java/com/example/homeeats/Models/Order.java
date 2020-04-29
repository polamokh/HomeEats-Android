package com.example.homeeats.Models;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;

import java.util.List;

public class Order {
    @Exclude
    public String id;
    @Exclude
    public FoodMaker foodMaker;
    public String foodMakerId;
    @Exclude
    public FoodBuyer foodBuyer;
    public String foodBuyerId;
    @Exclude
    public DeliveryBoy deliveryBoy;
    public String deliveryBoyId;
    public List<OrderItem> orderItems;
    public String review;
    public Integer rating;
    public Double totalPrice;
    public OrderStatus orderStatus;
    public LatLng buyerLocation;

    public Order(){}
    public Order(String id, LatLng buyerLocation, FoodMaker foodMaker, FoodBuyer foodBuyer, DeliveryBoy deliveryBoy, List<OrderItem> orderItems, String review, Integer rating, Double totalPrice, OrderStatus orderStatus) {
        this.id = id;
        this.buyerLocation = buyerLocation;
        this.foodMaker = foodMaker;
        this.foodBuyer = foodBuyer;
        this.deliveryBoy = deliveryBoy;
        this.orderItems = orderItems;
        this.review = review;
        this.rating = rating;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
    }

    public String getFoodMakerId() {
        return foodMaker.id;
    }

    public String getFoodBuyerId() {
        return foodBuyer.id;
    }

    public String getDeliveryBoyId() {
        return deliveryBoy.id;
    }

    public double getTotalPrice() {
        double price = 0;
        for(OrderItem item : orderItems)
        {
            if(item.getItemPrice() == -1)
                continue;
            price += item.getItemPrice();
        }
        return price;
    }
}
