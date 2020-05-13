package com.example.homeeats.Models;

import com.google.firebase.database.Exclude;

public class CartOrderItem extends OrderItem {
    @Exclude
    public String id;
    public String foodBuyerId;

    public CartOrderItem(String id, String foodBuyerId, String mealItemId, Integer quantity,
                         String notes, Integer rating, Double totalPrice) {
        super(mealItemId, quantity, notes, rating, totalPrice);
        this.id = id;
        this.foodBuyerId = foodBuyerId;
    }

    public CartOrderItem() {
    }
}
