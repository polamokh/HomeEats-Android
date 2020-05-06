package com.example.homeeats.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.example.homeeats.Activities.FoodMaker.FoodMakerEditMealActivity;
import com.example.homeeats.Models.FoodMaker;
import com.example.homeeats.Models.MealItem;
import com.example.homeeats.Models.Order;
import com.example.homeeats.Models.OrderItem;
import com.example.homeeats.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FoodMakerRequestsAdapter extends RecyclerView.Adapter<FoodMakerRequestsAdapter.RequestViewHolder> {

    private List<Order> food_Maker_Order;
    private ArrayList<String> OrderItems_list;

    public FoodMakerRequestsAdapter(List<Order> orders) {
        this.food_Maker_Order = orders;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_request, parent,
                false);
        RequestViewHolder RequestViewHolder = new RequestViewHolder(view);
        return RequestViewHolder;
    }

    @Override
    public void onBindViewHolder(RequestViewHolder holder, int position) {
        holder.OrderID.setText(food_Maker_Order.get(position).id);
        holder.DeliveryBoyName.setText(food_Maker_Order.get(position).deliveryBoyId);
        holder.BuyerName.setText(food_Maker_Order.get(position).foodBuyerId);
        holder.OrderStatus.setText(food_Maker_Order.get(position).orderStatus.toString());
        holder.Price.setText(food_Maker_Order.get(position).totalPrice.toString());
        for (OrderItem orderItem : food_Maker_Order.get(position).orderItems) {
            OrderItems_list.add(orderItem.quantity + " " + orderItem.mealItemId + " " + ";" + orderItem.notes);
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(holder.Order_List.getContext(), android.R.layout.simple_list_item_1, OrderItems_list);
        holder.Order_List.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView OrderID;
        TextView BuyerName;
        TextView OrderStatus;
        TextView DeliveryBoyName;
        TextView Price;
        ListView Order_List;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.Request_cardView);
            OrderID = itemView.findViewById(R.id.textViewRequestID);
            Price = itemView.findViewById(R.id.textViewRequestPrice);
            OrderStatus = itemView.findViewById(R.id.textViewRequestStatus);
            Order_List = itemView.findViewById(R.id.RequestsListView);
            BuyerName = itemView.findViewById(R.id.textViewRequestBuyerName);
            DeliveryBoyName = itemView.findViewById(R.id.textViewRequestDeliverBoyName);
            final ConstraintLayout expandableView;
            final Button arrowBtn;
            final CardView cardView;
            expandableView = itemView.findViewById(R.id.expandableView_Requests);
            arrowBtn = itemView.findViewById(R.id.arrowBtn_requests);
            cardView = itemView.findViewById(R.id.Request_cardView);

            arrowBtn.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {
                    if (expandableView.getVisibility() == View.GONE) {
                        TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                        expandableView.setVisibility(View.VISIBLE);
                        //arrowBtn.setBackgroundResource(R.drawable.ic_expand);
                    } else {
                        TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                        expandableView.setVisibility(View.GONE);
                        //arrowBtn.setBackgroundResource(R.drawable.ic_expand);
                    }
                }
            });

        }
    }
}


