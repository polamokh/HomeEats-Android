package com.example.homeeats.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firbasedao.Listeners.RetrievalEventListener;
import com.example.homeeats.Dao.MealItemDao;
import com.example.homeeats.Models.MealItem;
import com.example.homeeats.Models.OrderItem;
import com.example.homeeats.R;

import java.util.List;

public class FoodBuyerCartMealsRecyclerAdapter extends
        RecyclerView.Adapter<FoodBuyerCartMealsRecyclerAdapter.OrderItemViewHolder> {

    private List<OrderItem> orderItems;

    public FoodBuyerCartMealsRecyclerAdapter(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.foodbuyer_meals_cart,
                parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderItemViewHolder holder, final int position) {
        MealItemDao.GetInstance().get(orderItems.get(position).mealItemId,
                new RetrievalEventListener<MealItem>() {
                    @Override
                    public void OnDataRetrieved(MealItem mealItem) {
                        holder.name.setText(mealItem.name);
                    }
                });
        holder.quantity.setText("Qty. " + orderItems.get(position).quantity.toString());
        holder.price.setText("EGP" + orderItems.get(position).totalPrice.toString());

        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderItems.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, orderItems.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    public static class OrderItemViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView quantity;
        TextView price;
        ImageButton buttonDelete;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.foodBuyerCartMealsName);
            quantity = itemView.findViewById(R.id.foodBuyerCartMealsQty);
            price = itemView.findViewById(R.id.foodBuyerCartMealsPrice);
            buttonDelete = itemView.findViewById(R.id.foodBuyerCartMealsRemove);
        }
    }
}
