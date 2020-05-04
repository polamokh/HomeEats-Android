package com.example.homeeats.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homeeats.Activities.FoodBuyer.FoodBuyerViewMakerActivity;
import com.example.homeeats.Models.FoodMaker;
import com.example.homeeats.R;

import java.util.List;

public class FoodBuyerMakersRecyclerAdapter extends
        RecyclerView.Adapter<FoodBuyerMakersRecyclerAdapter.MakerViewHolder> {
    List<FoodMaker> foodMakers;

    public FoodBuyerMakersRecyclerAdapter(List<FoodMaker> foodMakers) {
        this.foodMakers = foodMakers;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public MakerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.foodbuyer_maker_card,
                parent, false);
        MakerViewHolder makerViewHolder = new MakerViewHolder(view);
        return makerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MakerViewHolder holder, int position) {
        //TODO: Add Maker Image;
        holder.name.setText(foodMakers.get(position).name);
        holder.phone.setText(foodMakers.get(position).phone);
        holder.email.setText(foodMakers.get(position).emailAddress);

        holder.foodMakerID = foodMakers.get(position).id;
    }

    @Override
    public int getItemCount() {
        return foodMakers.size();
    }

    public static class MakerViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView image;
        TextView name;
        TextView phone;
        TextView email;

        String foodMakerID;

        public MakerViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.foodBuyerMakerCardView);
            image = itemView.findViewById(R.id.foodBuyerMakerCardViewImage);
            name = itemView.findViewById(R.id.foodBuyerMakerCardViewName);
            phone = itemView.findViewById(R.id.foodBuyerMakerCardViewPhone);
            email = itemView.findViewById(R.id.foodBuyerMakerCardViewEmail);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cardClick(v);
                }
            });
        }

        void cardClick(View view) {
            Intent intent = new Intent(view.getContext(), FoodBuyerViewMakerActivity.class);
            intent.putExtra("FoodMakerID", foodMakerID);
            view.getContext().startActivity(intent);
        }
    }
}
