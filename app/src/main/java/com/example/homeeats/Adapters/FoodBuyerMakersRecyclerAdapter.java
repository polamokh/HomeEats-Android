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
import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodBuyerMakersRecyclerAdapter extends
        RecyclerView.Adapter<FoodBuyerMakersRecyclerAdapter.MakerViewHolder> {
    List<FoodMaker> foodMakers;
    String foodBuyerID;

    public FoodBuyerMakersRecyclerAdapter(List<FoodMaker> foodMakers, String foodBuyerID) {
        this.foodMakers = foodMakers;
        this.foodBuyerID = foodBuyerID;
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
    public void onBindViewHolder(@NonNull MakerViewHolder holder, final int position) {
        holder.name.setText(foodMakers.get(position).name);
        holder.phone.setText(foodMakers.get(position).phone);
        holder.email.setText(foodMakers.get(position).emailAddress);
        Picasso.get().load(foodMakers.get(position).photo).into(holder.image);

        holder.foodMakerID = foodMakers.get(position).id;

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FoodBuyerViewMakerActivity.class);
                intent.putExtra("FoodMakerID", foodMakers.get(position).id);
                intent.putExtra("FoodBuyerID", foodBuyerID);
                v.getContext().startActivity(intent);
            }
        });
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
        }
    }
}
