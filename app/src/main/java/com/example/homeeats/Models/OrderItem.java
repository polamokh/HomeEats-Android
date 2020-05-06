package com.example.homeeats.Models;

import com.example.homeeats.Dao.MealItemDao;
import com.example.homeeats.RetrievalEventListener;
import com.google.firebase.database.Exclude;

public class OrderItem {
    public String mealItemId;
    public Integer quantity;
    public String notes;
    public Integer rating;
        public Double totalPrice;

    public OrderItem(){}
    public OrderItem(String mealItemId, Integer quantity, String notes, Integer rating) {
        this.mealItemId = mealItemId;
        this.quantity = quantity;
        this.notes = notes;
        this.rating = rating;
    }

    public void GetMealItem(final RetrievalEventListener<MealItem> mealItemRetrievalEventListener)
    {
        MealItemDao.GetInstance().get(mealItemId, new RetrievalEventListener<MealItem>() {
            @Override
            public void OnDataRetrieved(MealItem mealItem) {
                mealItemRetrievalEventListener.OnDataRetrieved(mealItem);
            }
        });
    }
}
