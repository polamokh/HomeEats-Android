package com.example.homeeats.Models;

import com.example.homeeats.Dao.FoodMakerDao;
import com.example.homeeats.Listeners.RetrievalEventListener;
import com.google.firebase.database.Exclude;

public class MealItem {
    @Exclude
    public String id;
    public String name;
    public String foodMakerId;
    //url
    public String photo;
    public String description;
    public Double price;
    public String mealCategory;
    public Double rating;
    public boolean isAvailable;

    public void GetFoodMaker(final RetrievalEventListener<FoodMaker> foodMakerRetrievalEventListener)
    {
        FoodMakerDao.GetInstance().get(foodMakerId, new RetrievalEventListener<FoodMaker>() {
            @Override
            public void OnDataRetrieved(FoodMaker foodMaker) {
                foodMakerRetrievalEventListener.OnDataRetrieved(foodMaker);
            }
        });
    }
    public MealItem(String id, String name, String foodMakerId, String photo, String description, double price, String mealCategory, double rating, boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.foodMakerId = foodMakerId;
        this.photo = photo;
        this.description = description;
        this.price = price;
        this.mealCategory = mealCategory;
        this.rating = rating;
        this.isAvailable = isAvailable;
    }

    public MealItem() {
    }
}
