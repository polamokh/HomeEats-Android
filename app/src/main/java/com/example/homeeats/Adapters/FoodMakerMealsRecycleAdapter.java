package com.example.homeeats.Adapters;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.example.homeeats.Models.MealItem;
import com.example.homeeats.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodMakerMealsRecycleAdapter extends RecyclerView.Adapter<FoodMakerMealsRecycleAdapter.MealViewHolder> {
    List<MealItem> meals;

    public FoodMakerMealsRecycleAdapter(List<MealItem> meals) {
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



        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            image = itemView.findViewById(R.id.imageView);
            name = itemView.findViewById(R.id.textViewMealCardName);
            description = itemView.findViewById(R.id.textViewMealCardDescription);
            price = itemView.findViewById(R.id.textViewMealCardPrice);
            category = itemView.findViewById(R.id.textViewMealCardCategory);


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
            // END OF HABD


            /*
            cardView = (CardView)itemView.findViewById(R.id.mealCardView);
            image = (ImageView)itemView.findViewById(R.id.mealCardImageView);
            name = (TextView)itemView.findViewById(R.id.mealCardTextViewName);
            description = (TextView)itemView.findViewById(R.id.mealCardTextViewDescription);
            price = (TextView)itemView.findViewById(R.id.mealCardTextViewDescription);
             */
        }
    }
}
