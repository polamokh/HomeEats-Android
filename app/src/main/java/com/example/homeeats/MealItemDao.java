package com.example.homeeats;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;

public class MealItemDao extends Dao<MealItem> {
    private MealItemDao()
    {
        super("Meals");
    }
    private static MealItemDao singletonObject;
    public static MealItemDao GetInstance()
    {
        if(singletonObject == null)
            singletonObject = new MealItemDao();
        return singletonObject;
    }
    @Override
    public MealItem parseDataSnapshot(DataSnapshot dataSnapshot) {
        final MealItem mealItem = new MealItem();
        mealItem.id = dataSnapshot.getKey();
        mealItem.name = dataSnapshot.child("name").getValue().toString();
        mealItem.price = Double.parseDouble(dataSnapshot.child("price").getValue().toString());
        mealItem.photo = dataSnapshot.child("photo").getValue().toString();
        mealItem.description = dataSnapshot.child("description").getValue().toString();
        mealItem.mealCategory = dataSnapshot.child("mealCategory").getValue().toString();
        mealItem.rating = Double.parseDouble(dataSnapshot.child("rating").getValue().toString());
        mealItem.foodMaker = new FoodMaker();
        mealItem.foodMaker.id = dataSnapshot.child("foodMakerId").getValue().toString();
        return mealItem;
    }

    @Override
    void delete(MealItem mealItem) {

    }
}
