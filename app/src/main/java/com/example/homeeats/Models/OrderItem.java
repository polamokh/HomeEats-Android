package com.example.homeeats.Models;

import com.google.firebase.database.Exclude;

public class OrderItem {
    @Exclude
    public MealItem mealItem;
    public String mealItemId;
    public Integer quantity;
    public String notes;
    public Integer rating;

    public OrderItem(MealItem mealItem, Integer quantity, String notes, Integer rating) {
        this.mealItem = mealItem;
        this.quantity = quantity;
        this.notes = notes;
        this.rating = rating;
    }

    public String getMealItemId() {
        return mealItem.id;
    }

    public double getItemPrice()
    {
        if(mealItem == null || mealItem.price == null || quantity == null)
            return -1;
        return (double) quantity * mealItem.price;
    }
}
