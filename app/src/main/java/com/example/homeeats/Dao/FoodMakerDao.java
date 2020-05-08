package com.example.homeeats.Dao;

import android.graphics.Bitmap;

import com.example.firbasedao.FirebaseDao;
import com.example.firbasedao.Listeners.RetrievalEventListener;
import com.example.firbasedao.Listeners.TaskListener;
import com.example.homeeats.FilesStorageDatabase;
import com.example.homeeats.Helpers.StringHelper;
import com.example.homeeats.Models.DeliveryBoy;
import com.example.homeeats.Models.FoodMaker;
import com.example.homeeats.Models.MealItem;
import com.example.homeeats.Models.Order;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FoodMakerDao extends FirebaseDao<FoodMaker> {
    private static FoodMakerDao singletonObject;
    public FoodMakerDao(){
        super("FoodMakers");
    }
    public static FoodMakerDao GetInstance() {
        if(singletonObject == null)
            singletonObject = new FoodMakerDao();
        return singletonObject;
    }

    public void save(final FoodMaker foodMaker, final String id, Bitmap bitmap, final TaskListener taskListener){
        if(bitmap == null){
            save(foodMaker, id, taskListener);
            return;
        }
        setDeliveryBoyImage(id, bitmap, new RetrievalEventListener<String>() {
            @Override
            public void OnDataRetrieved(String s) {
                foodMaker.photo = s;
                save(foodMaker, id, taskListener);
            }
        });
    }

    public void setDeliveryBoyImage(final String foodMakerId, Bitmap image, final RetrievalEventListener<String> retrievalEventListener){
        FilesStorageDatabase.GetInstance().uploadPhoto(image, String.format("food makers images/%s.jpg", foodMakerId),
                new RetrievalEventListener<String>() {
                    @Override
                    public void OnDataRetrieved(String uploadPath) {
                        dbReference.child(tableName).child(foodMakerId).child("photo").setValue(uploadPath);
                        retrievalEventListener.OnDataRetrieved(uploadPath);
                    }
                });
    }

    @Override
    protected void parseDataSnapshot(DataSnapshot dataSnapshot, final RetrievalEventListener<FoodMaker> retrievalEventListener) {
        final FoodMaker foodMaker = new FoodMaker();
        foodMaker.id = dataSnapshot.getKey();
        foodMaker.name = dataSnapshot.child("name").getValue().toString();
        foodMaker.gender = dataSnapshot.child("gender").getValue().toString();
        foodMaker.emailAddress = dataSnapshot.child("emailAddress").getValue().toString();
        foodMaker.phone = dataSnapshot.child("phone").getValue().toString();
        foodMaker.photo = dataSnapshot.child("photo").getValue().toString();
        double latitude = Double.parseDouble(dataSnapshot.child("location").child("latitude").getValue().toString());
        double longitude = Double.parseDouble(dataSnapshot.child("location").child("longitude").getValue().toString());
        foodMaker.location = new LatLng(latitude, longitude);
        foodMaker.rating = Double.parseDouble(dataSnapshot.child("rating").getValue().toString());
        retrievalEventListener.OnDataRetrieved(foodMaker);
    }

    public void GetFoodMakerMeals(final String foodMakerId, final RetrievalEventListener<List<MealItem>> listRetrievalEventListener)
    {
        final List<MealItem> makerMeals = new ArrayList<>();
        MealItemDao.GetInstance().getAll(new RetrievalEventListener<List<MealItem>>() {
            @Override
            public void OnDataRetrieved(List<MealItem> mealItems) {
                for(MealItem currentMealItem : mealItems)
                {
                    if(currentMealItem.foodMakerId.equals(foodMakerId))
                        makerMeals.add(currentMealItem);
                }
                listRetrievalEventListener.OnDataRetrieved(makerMeals);
            }
        });
    }
    public void GetFoodMakerReviews(final String foodMakerId, final RetrievalEventListener<List<String>> listRetrievalEventListener)
    {
        final List<String> reviews = new ArrayList<>();
        OrderDao.GetInstance().getAll(new RetrievalEventListener<List<Order>>() {
            @Override
            public void OnDataRetrieved(List<Order> orders) {
                for(Order currentOrder : orders)
                    if(currentOrder.foodMakerId.equals(foodMakerId))
                        reviews.add(currentOrder.review);
                listRetrievalEventListener.OnDataRetrieved(reviews);
            }
        });
    }
    public void GetFoodMakerOrders(final String foodMakerId, final RetrievalEventListener<List<Order>> listRetrievalEventListener)
    {
        final List<Order> foodMakerOrders = new ArrayList<>();
        OrderDao.GetInstance().getAll(new RetrievalEventListener<List<Order>>() {
            @Override
            public void OnDataRetrieved(List<Order> orders) {
                for(Order currentOrder : orders)
                {
                    if(currentOrder.foodMakerId.equals(foodMakerId))
                        foodMakerOrders.add(currentOrder);
                }
                listRetrievalEventListener.OnDataRetrieved(foodMakerOrders);
            }
        });
    }

    //filter makers by name from database
    public void FilterMakersByName(final String name, final RetrievalEventListener<List<FoodMaker>> retrievalEventListener)
    {
        final List<FoodMaker> filteredFoodMakers = new ArrayList<>();
        getAll(new RetrievalEventListener<List<FoodMaker>>() {
            @Override
            public void OnDataRetrieved(List<FoodMaker> foodMakers) {
                for(FoodMaker foodMaker : foodMakers)
                    if(StringHelper.Contains(foodMaker.name, name))
                        filteredFoodMakers.add(foodMaker);
                retrievalEventListener.OnDataRetrieved(filteredFoodMakers);
            }
        });
    }

    //filter makers by name given a list of food makers
    public List<FoodMaker> FilterMakersByName(List<FoodMaker> foodMakers, String name){
        final List<FoodMaker> filteredFoodMakers = new ArrayList<>();
        for(FoodMaker foodMaker : foodMakers)
            if(StringHelper.Contains(foodMaker.name, name))
                filteredFoodMakers.add(foodMaker);
        return filteredFoodMakers;
    }
}
