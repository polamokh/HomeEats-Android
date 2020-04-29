package com.example.homeeats;

import com.google.firebase.database.Exclude;

public class MealItem {
        @Exclude
        String id;
        String name;
        FoodMaker foodMaker;
        //url
        String photo;
        String description;
        Double price;
        String mealCategory;
        Double rating;

    public MealItem(String id, String name, FoodMaker foodMaker, String photo, String description, double price, String mealCategory, double rating) {
        this.id = id;
        this.name = name;
        this.foodMaker = foodMaker;
        this.photo = photo;
        this.description = description;
        this.price = price;
        this.mealCategory = mealCategory;
        this.rating = rating;
    }
}
