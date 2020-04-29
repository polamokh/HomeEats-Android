package com.example.homeeats.Models;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;

import java.util.List;

public class Order {
    @Exclude
    String id;
    public LatLng buyerLocation;
    public FoodMaker foodMaker;
    public FoodBuyer foodBuyer;
    public DeliveryBoy deliveryBoy;
    public List<OrderItem> orderItems;
    public String review;
    public Integer rating;
    public Double totalPrice;
    public OrderStatus orderStatus;

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
