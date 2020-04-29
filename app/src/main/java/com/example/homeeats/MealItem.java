package com.example.homeeats;

import com.google.firebase.database.Exclude;

public class MealItem {
    @Exclude
    public String id;
    public String name;
    @Exclude
    public FoodMaker foodMaker;


    public String foodMakerId;
    //url
    public String photo;
    public String description;
    public Double price;
    public String mealCategory;
    public Double rating;

    public String getFoodMakerId() {
        return foodMaker.id;
    }

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

    public MealItem() {
    }
}
