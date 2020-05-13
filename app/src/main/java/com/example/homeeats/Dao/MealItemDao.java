package com.example.homeeats.Dao;

import android.graphics.Bitmap;

import com.example.firbasedao.FirebaseDao;
import com.example.firbasedao.Listeners.RetrievalEventListener;
import com.example.firbasedao.Listeners.TaskListener;
import com.example.homeeats.FilesStorageDatabase;
import com.example.homeeats.Helpers.StringHelper;
import com.example.homeeats.Models.MealItem;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MealItemDao extends FirebaseDao<MealItem> {
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

    public void save(final MealItem mealItem, final String id, Bitmap bitmap, final TaskListener taskListener) {
        if(bitmap == null){
            save(mealItem, id, taskListener);
            return;
        }
        setMealImage(id, mealItem.foodMakerId, bitmap, new RetrievalEventListener<String>() {
            @Override
            public void OnDataRetrieved(String s) {
                mealItem.photo = s;
                save(mealItem, id, taskListener);
            }
        });
    }

    @Override
    protected void parseDataSnapshot(DataSnapshot dataSnapshot, final RetrievalEventListener<MealItem> retrievalEventListener) {
        final MealItem mealItem = new MealItem();
        mealItem.id = dataSnapshot.getKey();
        mealItem.name = dataSnapshot.child("name").getValue().toString();
        mealItem.price = Double.parseDouble(dataSnapshot.child("price").getValue().toString());
        mealItem.photo = dataSnapshot.child("photo").getValue().toString();
        mealItem.description = dataSnapshot.child("description").getValue().toString();
        mealItem.mealCategory = dataSnapshot.child("mealCategory").getValue().toString();
        mealItem.rating = Double.parseDouble(dataSnapshot.child("rating").getValue().toString());
        mealItem.foodMakerId = dataSnapshot.child("foodMakerId").getValue().toString();
        mealItem.isAvailable = Boolean.parseBoolean(dataSnapshot.child("isAvailable").getValue().toString());
        retrievalEventListener.OnDataRetrieved(mealItem);
    }

    //filters meals by name and category from database
    //if name is null then name is ignored
    //if category is null then category is ignored
    public void FilterMealsByNameAndCategory(final String name, final String category, final RetrievalEventListener<List<MealItem>> retrievalEventListener){
        final List<MealItem> filteredMealItems = new ArrayList<>();
        getAll(new RetrievalEventListener<List<MealItem>>() {
            @Override
            public void OnDataRetrieved(List<MealItem> mealItems) {
                for(MealItem mealItem : mealItems){
                    //check name
                    if(name != null && !StringHelper.Contains(mealItem.name, name))
                        continue;
                    //check category
                    if(category != null && !StringHelper.Contains(mealItem.mealCategory, category))
                        continue;
                    filteredMealItems.add(mealItem);
                }
                retrievalEventListener.OnDataRetrieved(filteredMealItems);
            }
        });
    }
    public void setMealImage(final String mealId, String mealMakerId, Bitmap image, final RetrievalEventListener<String> retrievalEventListener){
        FilesStorageDatabase.GetInstance().uploadPhoto(image, String.format("meal images/%s/%s.jpg", mealMakerId, mealId),
                new RetrievalEventListener<String>() {
                    @Override
                    public void OnDataRetrieved(String uploadPath) {
                        dbReference.child(tableName).child(mealId).child("photo").setValue(uploadPath);
                        retrievalEventListener.OnDataRetrieved(uploadPath);
                    }
                });
    }

    //filters meals by name and category from a given list of meal items
    //if name is null then name is ignored
    //if category is null then category is ignored
    public List<MealItem> FilterMealsByNameAndCategory(List<MealItem> mealItems, final String name, final String category){
        final List<MealItem> filteredMealItems = new ArrayList<>();
        for(MealItem mealItem : mealItems){
            //check name
            if(name != null && !StringHelper.Contains(mealItem.name, name))
                continue;
             //check category
            if(category != null && !StringHelper.Contains(mealItem.mealCategory, category))
                continue;
            filteredMealItems.add(mealItem);
        }
        return filteredMealItems;
    }
}
