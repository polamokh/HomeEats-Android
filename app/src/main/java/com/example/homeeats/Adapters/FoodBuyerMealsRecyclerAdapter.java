package com.example.homeeats.Adapters;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homeeats.Dao.FoodMakerDao;
import com.example.homeeats.Models.FoodMaker;
import com.example.homeeats.Models.MealItem;
import com.example.homeeats.R;
import com.example.homeeats.Listeners.RetrievalEventListener;

import java.util.List;

public class FoodBuyerMealsRecyclerAdapter extends
        RecyclerView.Adapter<FoodBuyerMealsRecyclerAdapter.MealViewHolder> {
    List<MealItem> meals;

    public FoodBuyerMealsRecyclerAdapter(List<MealItem> meals) {
        this.meals = meals;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.foodbuyer_meal_card,
                parent, false);
        MealViewHolder mealViewHolder = new MealViewHolder(view);
        return mealViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MealViewHolder holder, int position) {
        //TODO: Add required images(Meal and Maker)
        holder.mealName.setText(meals.get(position).name);
        holder.mealPrice.setText("EGP" + meals.get(position).price.toString());
        holder.mealDescription.setText(meals.get(position).description);

        FoodMakerDao.GetInstance().get(meals.get(position).foodMakerId,
                new RetrievalEventListener<FoodMaker>() {
                    @Override
                    public void OnDataRetrieved(FoodMaker foodMaker) {
                        holder.makerName.setText("by " + foodMaker.name);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public static class MealViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView mealImageView;
        TextView mealName;
        TextView mealPrice;
        TextView mealDescription;
        TextView makerName;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.foodBuyerMealCardView);
            mealImageView = itemView.findViewById(R.id.foodBuyerMealCardViewImage);
            mealName = itemView.findViewById(R.id.foodBuyerMealCardViewName);
            mealPrice = itemView.findViewById(R.id.foodBuyerMealCardViewPrice);
            mealDescription = itemView.findViewById(R.id.foodBuyerMealCardViewDescription);
            makerName = itemView.findViewById(R.id.foodBuyerMealCardViewMakerName);

            makerName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    makerClick(v);
                }
            });
        }

        void makerClick(View view) {
            //TODO: Add onClick maker event.
            Toast.makeText(view.getContext(), "Clicked", Toast.LENGTH_SHORT).show();
        }
    }
}
