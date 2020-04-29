package com.example.homeeats.Models;

public class OrderItem {
    public MealItem mealItem;
    public Integer quantity;
    public String notes;
    public Integer rating;

    public OrderItem(MealItem mealItem, Integer quantity, String notes, Integer rating) {
        this.mealItem = mealItem;
        this.quantity = quantity;
        this.notes = notes;
        this.rating = rating;
    }

    public double getItemPrice()
    {
        if(mealItem == null || mealItem.price == null || quantity == null)
            return -1;
        return (double) quantity * mealItem.price;
    }
}
