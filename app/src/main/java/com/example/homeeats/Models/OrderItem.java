package com.example.homeeats.Models;

import com.example.homeeats.Dao.MealItemDao;
import com.example.firbasedao.Listeners.RetrievalEventListener;

public class OrderItem {
    public String mealItemId;
    public Integer quantity;
    public String notes;
    public Integer rating;
    public Double totalPrice;

    public OrderItem() {
    }

    public OrderItem(String mealItemId, Integer quantity, String notes, Integer rating, Double totalPrice) {
        this.mealItemId = mealItemId;
        this.quantity = quantity;
        this.notes = notes;
        this.rating = rating;
        this.totalPrice = totalPrice;
    }

    public void getCurrentTotalPrice(final RetrievalEventListener<Double> retrievalEventListener) {
        MealItemDao.GetInstance().get(mealItemId, new RetrievalEventListener<MealItem>() {
            @Override
            public void OnDataRetrieved(MealItem mealItem) {
                retrievalEventListener.OnDataRetrieved(mealItem.price * quantity);
            }
        });
    }

    public void GetMealItem(final RetrievalEventListener<MealItem> mealItemRetrievalEventListener) {
        MealItemDao.GetInstance().get(mealItemId, new RetrievalEventListener<MealItem>() {
            @Override
            public void OnDataRetrieved(MealItem mealItem) {
                mealItemRetrievalEventListener.OnDataRetrieved(mealItem);
            }
        });
    }
}
