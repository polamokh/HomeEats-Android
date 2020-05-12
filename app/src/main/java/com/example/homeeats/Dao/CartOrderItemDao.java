package com.example.homeeats.Dao;

import androidx.annotation.NonNull;

import com.example.firbasedao.Listeners.RetrievalEventListener;
import com.example.firbasedao.Listeners.TaskListener;
import com.example.homeeats.Models.CartOrderItem;
import com.example.homeeats.Models.MealItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartOrderItemDao {
    protected static final DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference();
    private static CartOrderItemDao singletonObject;
    private static String tableName;
    private CartOrderItemDao(){
        tableName = "Cart";
    }
    public static CartOrderItemDao GetInstance(){
        if(singletonObject == null)
            singletonObject = new CartOrderItemDao();
        return singletonObject;
    }
    public void Add(final CartOrderItem cartOrderItem, final TaskListener taskListener) {
        cartOrderItem.GetMealItem(new RetrievalEventListener<MealItem>() {
            @Override
            public void OnDataRetrieved(MealItem mealItem) {
                cartOrderItem.id = dbReference.child(tableName).child(cartOrderItem.foodBuyerId).child(mealItem.foodMakerId).push().getKey();
                dbReference.child(tableName).child(cartOrderItem.foodBuyerId).child(mealItem.foodMakerId).child(cartOrderItem.id).setValue(cartOrderItem);
                taskListener.OnSuccess();
            }
        });
    }

    public void Remove(final CartOrderItem cartOrderItem, final TaskListener taskListener){
        cartOrderItem.GetMealItem(new RetrievalEventListener<MealItem>() {
            @Override
            public void OnDataRetrieved(MealItem mealItem) {
                dbReference.child(tableName).child(cartOrderItem.foodBuyerId).child(mealItem.foodMakerId).child(cartOrderItem.id).setValue(null);
                taskListener.OnSuccess();
            }
        });
    }

    public void GetFoodBuyerFoodMakerCartOrderItems(String foodBuyerId, String foodMakerId, final RetrievalEventListener<List<CartOrderItem>> retrievalEventListener){
        DatabaseReference rowReference = dbReference.child(tableName).child(foodBuyerId).child(foodMakerId);
        rowReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<CartOrderItem> cartOrderItems = new ArrayList<>();
                for(DataSnapshot currentDataSnapshot : dataSnapshot.getChildren())
                    cartOrderItems.add(parseFoodBuyerFoodMakerDataSnapshot(currentDataSnapshot));
                retrievalEventListener.OnDataRetrieved(cartOrderItems);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                retrievalEventListener.OnDataRetrieved(null);
            }
        });
    }

    public void GetFoodBuyerCart(String foodBuyerId, final RetrievalEventListener<HashMap<String, List<CartOrderItem>>> retrievalEventListener){
        DatabaseReference rowReference = dbReference.child(tableName).child(foodBuyerId);
        rowReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, List<CartOrderItem>> cart = new HashMap<>();
                for(DataSnapshot currentDataSnapshot : dataSnapshot.getChildren()){
                    String foodMakerId = currentDataSnapshot.getKey();
                    List<CartOrderItem> foodMakerOrderItems = new ArrayList<>();
                    for(DataSnapshot cartOrderItemsSnapshot : currentDataSnapshot.getChildren())
                        foodMakerOrderItems.add(parseFoodBuyerFoodMakerDataSnapshot(cartOrderItemsSnapshot));
                    cart.put(foodMakerId, foodMakerOrderItems);
                }
                retrievalEventListener.OnDataRetrieved(cart);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                retrievalEventListener.OnDataRetrieved(null);
            }
        });
    }

    private CartOrderItem parseFoodBuyerFoodMakerDataSnapshot(DataSnapshot dataSnapshot){
        CartOrderItem cartOrderItem = new CartOrderItem();
        cartOrderItem.id = dataSnapshot.getKey();
        cartOrderItem.foodBuyerId = dataSnapshot.child("foodBuyerId").getValue().toString();
        cartOrderItem.mealItemId = dataSnapshot.child("mealItemId").getValue().toString();
        cartOrderItem.notes = dataSnapshot.child("notes").getValue().toString();
        cartOrderItem.quantity = Integer.parseInt(dataSnapshot.child("quantity").getValue().toString());
        cartOrderItem.rating = Integer.parseInt(dataSnapshot.child("rating").getValue().toString());
//        cartOrderItem.totalPrice = Double.parseDouble(dataSnapshot.child("totalPrice").getValue().toString());
        return cartOrderItem;
    }
}
