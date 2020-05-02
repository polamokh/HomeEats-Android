package com.example.homeeats.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homeeats.Models.MealItem;
import com.example.homeeats.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MealViewHolder> {
    List<MealItem> meals;

    public RecyclerViewAdapter(List<MealItem> meals) {
        this.meals = meals;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_meal, parent,
                false);
        MealViewHolder mealViewHolder = new MealViewHolder(view);
        return mealViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        holder.name.setText(meals.get(position).name);
        holder.description.setText(meals.get(position).description);
        holder.price.setText("EGP" + meals.get(position).price.toString());
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }


    public static class MealViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView image;
        TextView name;
        TextView description;
        TextView price;


        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.mealCardView);
            image = (ImageView)itemView.findViewById(R.id.mealCardImageView);
            name = (TextView)itemView.findViewById(R.id.mealCardTextViewName);
            description = (TextView)itemView.findViewById(R.id.mealCardTextViewDescription);
            price = (TextView)itemView.findViewById(R.id.mealCardTextViewDescription);
        }
    }
}
