package com.example.homeeats.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firbasedao.Listeners.RetrievalEventListener;
import com.example.homeeats.Activities.FoodBuyer.FoodBuyerCartFragment;
import com.example.homeeats.Dao.CartOrderItemDao;
import com.example.homeeats.Dao.FoodMakerDao;
import com.example.homeeats.Models.CartOrderItem;
import com.example.homeeats.Models.FoodMaker;
import com.example.homeeats.Models.Order;
import com.example.homeeats.R;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class FoodBuyerCartOrdersRecyclerAdapter extends
        RecyclerView.Adapter<FoodBuyerCartOrdersRecyclerAdapter.MakersViewHolder> {

    private HashMap<String, List<CartOrderItem>> orders;
    private ArrayList<String> foodMakers;
    private Fragment fragment;

    public FoodBuyerCartOrdersRecyclerAdapter(HashMap<String, List<CartOrderItem>> orders,
                                              ArrayList<String> foodMakers, Fragment fragment) {
        this.orders = orders;
        this.foodMakers = foodMakers;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public MakersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.foodbuyer_order_cart,
                parent, false);
        return new MakersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MakersViewHolder holder, final int position) {
        if (orders.get(foodMakers.get(position)).size() == 0) {
            orders.remove(foodMakers.get(position));
            foodMakers.remove(position);

            ((FoodBuyerCartFragment)fragment).recalculateTotalPrice(orders, foodMakers);

            return;
        }

        FoodMakerDao.GetInstance().get(foodMakers.get(position),
                new RetrievalEventListener<FoodMaker>() {
                    @Override
                    public void OnDataRetrieved(FoodMaker foodMaker) {
                        holder.name.setText(foodMaker.name);
                        Picasso.get()
                                .load(foodMaker.photo)
                                .into(holder.image);
                    }
                });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.fragment.getContext());
        holder.recyclerView.setLayoutManager(linearLayoutManager);

        FoodBuyerCartMealsRecyclerAdapter adapter =
                new FoodBuyerCartMealsRecyclerAdapter(orders.get(foodMakers.get(position)), this);
        holder.recyclerView.setAdapter(adapter);

        Double orderTotalPrice = 0.0;
        for (CartOrderItem cartOrderItem : orders.get(foodMakers.get(position))) {
            orderTotalPrice += cartOrderItem.totalPrice;
        }
        holder.totalPrice.setText(orderTotalPrice.toString());

        holder.buttonRemoveOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartOrderItemDao.GetInstance().removeFoodMakerFromFoodBuyerCart(
                        orders.get(foodMakers.get(position)).get(0).foodBuyerId,
                        foodMakers.get(position)
                );

                orders.remove(foodMakers.get(position));
                foodMakers.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, orders.size());

                ((FoodBuyerCartFragment)fragment).recalculateTotalPrice(orders, foodMakers);
            }
        });

        ((FoodBuyerCartFragment)fragment).recalculateTotalPrice(orders, foodMakers);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class MakersViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        RecyclerView recyclerView;
        TextView totalPrice;
        Button buttonRemoveOrder;

        public MakersViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.foodBuyerCartMakerImage);
            name = itemView.findViewById(R.id.foodBuyerCartMakerName);
            recyclerView = itemView.findViewById(R.id.foodBuyerCartMakerMealsRecyclerView);
            totalPrice = itemView.findViewById(R.id.foodBuyerCartMakerTotalPrice);
            buttonRemoveOrder = itemView.findViewById(R.id.foodBuyerCartMakerRemoveOrder);
        }
    }
}
