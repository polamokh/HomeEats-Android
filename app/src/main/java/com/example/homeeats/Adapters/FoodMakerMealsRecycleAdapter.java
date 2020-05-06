package com.example.homeeats.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.example.homeeats.Activities.FoodMaker.FoodMakerEditMealActivity;
import com.example.homeeats.Models.MealItem;
import com.example.homeeats.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodMakerMealsRecycleAdapter extends RecyclerView.Adapter<FoodMakerMealsRecycleAdapter.MealViewHolder>
{
    List<MealItem> meals;
    private static Context context;

    public FoodMakerMealsRecycleAdapter(List<MealItem> meals) {
        this.meals = meals;

    }
    public FoodMakerMealsRecycleAdapter(List<MealItem> meals,Context context) {
        this.meals = meals;
        this.context=context;

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
    public void onBindViewHolder(MealViewHolder holder, int position)
    {
        holder.Meal_ID=meals.get(position).id;
        holder.name.setText(meals.get(position).name);
        holder.description.setText(meals.get(position).description);
        holder.price.setText("EGP" + meals.get(position).price.toString());
        holder.category.setText(meals.get(position).mealCategory);
        Picasso.get().load(meals.get(position).photo).into(holder.image);

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
        TextView category;
        ImageButton Edit_Meal;
        String Meal_ID;



        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.cardView);
            image = (ImageView)itemView.findViewById(R.id.imageView);
            name = (TextView)itemView.findViewById(R.id.textViewMealCardName);
            description = (TextView)itemView.findViewById(R.id.textViewMealCardDescription);
            price = (TextView)itemView.findViewById(R.id.textViewMealCardPrice);
            category = (TextView)itemView.findViewById(R.id.textViewMealCardCategory);
            Edit_Meal=(ImageButton)itemView.findViewById(R.id.Food_Maker_Edit_Meal_btn);

            // START OF HABD
            final ConstraintLayout expandableView;
            final Button arrowBtn;
            final CardView cardView;

            expandableView = itemView.findViewById(R.id.expandableView);
            arrowBtn = itemView.findViewById(R.id.arrowBtn);
            cardView = itemView.findViewById(R.id.cardView);

            arrowBtn.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {
                    if (expandableView.getVisibility()==View.GONE){
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
            Edit_Meal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(expandableView.getContext(), FoodMakerEditMealActivity.class);
                    intent.putExtra("MealID",Meal_ID );
                    expandableView.getContext().startActivity(intent);
                }
            });
        }
    }
}
