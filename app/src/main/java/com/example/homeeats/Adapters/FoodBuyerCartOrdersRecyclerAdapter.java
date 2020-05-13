package com.example.homeeats.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firbasedao.Listeners.RetrievalEventListener;
import com.example.homeeats.Dao.FoodMakerDao;
import com.example.homeeats.Models.FoodMaker;
import com.example.homeeats.Models.Order;
import com.example.homeeats.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodBuyerCartOrdersRecyclerAdapter extends
        RecyclerView.Adapter<FoodBuyerCartOrdersRecyclerAdapter.MakersViewHolder> {

    private List<Order> orders;
    private Context context;

    public FoodBuyerCartOrdersRecyclerAdapter(List<Order> orders, Context context) {
        this.orders = orders;
        this.context = context;
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
        FoodMakerDao.GetInstance().get(orders.get(position).foodMakerId,
                new RetrievalEventListener<FoodMaker>() {
                    @Override
                    public void OnDataRetrieved(FoodMaker foodMaker) {
                        holder.name.setText(foodMaker.name);
                        Picasso.get()
                                .load(foodMaker.photo)
                                .into(holder.image);
                    }
                });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.context);
        holder.recyclerView.setLayoutManager(linearLayoutManager);
        FoodBuyerCartMealsRecyclerAdapter adapter =
                new FoodBuyerCartMealsRecyclerAdapter(orders.get(position).orderItems);
        holder.recyclerView.setAdapter(adapter);
        holder.totalPrice.setText(orders.get(position).totalPrice.toString());

        holder.buttonRemoveOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orders.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, orders.size());
            }
        });
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
