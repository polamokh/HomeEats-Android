package com.example.homeeats.Models;

import com.example.homeeats.Dao.FoodMakerDao;
import com.example.firbasedao.Listeners.RetrievalEventListener;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class FoodMaker extends Client {
    public LatLng location;
    public Double rating;

    public void GetReviews(final RetrievalEventListener<List<String>> listRetrievalEventListener)
    {
        FoodMakerDao.GetInstance().GetFoodMakerReviews(id, new RetrievalEventListener<List<String>>() {
            @Override
            public void OnDataRetrieved(List<String> reviews) {
                listRetrievalEventListener.OnDataRetrieved(reviews);
            }
        });
    }

    public void GetMealItems(final RetrievalEventListener<List<MealItem>> listRetrievalEventListener)
    {
        FoodMakerDao.GetInstance().GetFoodMakerMeals(id, new RetrievalEventListener<List<MealItem>>() {
            @Override
            public void OnDataRetrieved(List<MealItem> mealItems) {
                listRetrievalEventListener.OnDataRetrieved(mealItems);
            }
        });
    }

    public FoodMaker(String id, String name, String gender, String emailAddress, String phone, LatLng location, double rating) {
        super(id, name, gender, emailAddress, phone);
        this.location = location;
        this.rating = rating;
    }
    public FoodMaker(){}
}
