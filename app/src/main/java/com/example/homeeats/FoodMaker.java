package com.example.homeeats;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class FoodMaker extends Client {
    public LatLng location;
    public Double rating;
    public List<String> reviews;
    public List<MealItem> mealItems;

    public FoodMaker(String id, String name, String gender, String emailAddress, String phone, LatLng location, double rating, List<String> reviews, List<MealItem> mealItems) {
        super(id, name, gender, emailAddress, phone);
        this.location = location;
        this.rating = rating;
        this.reviews = reviews;
        this.mealItems = mealItems;
    }
}
