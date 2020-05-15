package com.example.homeeats.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.firbasedao.Listeners.RetrievalEventListener;
import com.example.homeeats.Dao.FoodBuyerDao;
import com.example.homeeats.Dao.MealItemDao;
import com.example.homeeats.Models.FoodBuyer;
import com.example.homeeats.Models.MealItem;
import com.example.homeeats.Models.OrderItem;
import com.example.homeeats.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FoodBuyerOrderDetailedItemsRecyclerAdapter extends RecyclerView.Adapter<FoodBuyerOrderDetailedItemsRecyclerAdapter.OrderItemViewHolder> {
    List<OrderItem> orderItems;

    public FoodBuyerOrderDetailedItemsRecyclerAdapter(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.foodbuyer_order_detailed_card, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderItemViewHolder holder, final int position) {
        MealItemDao.GetInstance().get(orderItems.get(position).mealItemId, new RetrievalEventListener<MealItem>() {
            @Override
            public void OnDataRetrieved(MealItem mealItem) {
                holder.name.setText(mealItem.name);
                holder.price.setText(mealItem.price.toString());
            }
        });
        holder.qty.setText(orderItems.get(position).quantity.toString());
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    public static class OrderItemViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView qty;
        TextView price;
        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.foodBuyerOrderDetailedMealName);
            qty = itemView.findViewById(R.id.foodBuyerOrderDetailedMealQty);
            price = itemView.findViewById(R.id.foodBuyerOrderDetailedPrice);
        }
    }
}
