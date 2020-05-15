package com.example.homeeats.Adapters;

import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homeeats.Activities.FoodBuyer.FoodBuyerMealsFragment;
import com.example.homeeats.Activities.FoodBuyer.FoodBuyerOrderItemDialog;
import com.example.homeeats.Activities.FoodBuyer.FoodBuyerViewMakerActivity;
import com.example.homeeats.Dao.FoodMakerDao;
import com.example.homeeats.Models.FoodMaker;
import com.example.homeeats.Models.MealItem;
import com.example.homeeats.Models.Order;
import com.example.homeeats.R;
import com.example.firbasedao.Listeners.RetrievalEventListener;
import com.google.firebase.database.core.Context;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodBuyerMealsRecyclerAdapter extends RecyclerView.Adapter<FoodBuyerMealsRecyclerAdapter.MealViewHolder> {
    List<MealItem> meals;
    FragmentManager fragmentManager;
    Order currentOrder;
    String foodBuyerID;

    public FoodBuyerMealsRecyclerAdapter(List<MealItem> meals, FragmentManager fragmentManager,
                                         Order currentOrder, String foodBuyerID) {
        this.meals = meals;
        this.fragmentManager = fragmentManager;
        this.currentOrder = currentOrder;
        this.foodBuyerID = foodBuyerID;
    }

    public FoodBuyerMealsRecyclerAdapter(List<MealItem> meals, FragmentManager fragmentManager, String foodBuyerID) {
        this.meals = meals;
        this.fragmentManager = fragmentManager;
        this.foodBuyerID = foodBuyerID;
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
    public void onBindViewHolder(@NonNull final MealViewHolder holder, final int position) {

        holder.addOrderItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FoodBuyerOrderItemDialog dialog = new FoodBuyerOrderItemDialog(meals.get(position).id, currentOrder);
                dialog.show(fragmentManager, "HELLO!");
            }
        });


        holder.mealName.setText(meals.get(position).name);
        holder.mealPrice.setText("EGP" + meals.get(position).price.toString());
        holder.mealDescription.setText(meals.get(position).description);
        holder.mealCategory.setText(meals.get(position).mealCategory);
        Picasso.get().load(meals.get(position).photo).into(holder.mealImageView);

        FoodMakerDao.GetInstance().get(meals.get(position).foodMakerId,
                new RetrievalEventListener<FoodMaker>() {
                    @Override
                    public void OnDataRetrieved(FoodMaker foodMaker) {
                        holder.makerName.setText("by " + foodMaker.name);
                        holder.makerID = foodMaker.id;
                    }
                });
        holder.makerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FoodBuyerViewMakerActivity.class);
                intent.putExtra("FoodMakerID", meals.get(position).foodMakerId);
                intent.putExtra("FoodBuyerID", foodBuyerID);
                v.getContext().startActivity(intent);
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
        TextView mealCategory;
        TextView makerName;
        String makerID;
        Button addOrderItemBtn;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.foodBuyerMealCardView);
            mealImageView = itemView.findViewById(R.id.foodBuyerMealCardViewImage);
            mealName = itemView.findViewById(R.id.foodBuyerMealCardViewName);
            mealPrice = itemView.findViewById(R.id.foodBuyerMealCardViewPrice);
            mealDescription = itemView.findViewById(R.id.foodBuyerMealCardViewDescription);
            mealCategory = itemView.findViewById(R.id.foodBuyerMealCardViewCategory);
            makerName = itemView.findViewById(R.id.foodBuyerMealCardViewMakerName);
            addOrderItemBtn = itemView.findViewById(R.id.foodBuyerAddOrderItemButton);
        }
    }
}
