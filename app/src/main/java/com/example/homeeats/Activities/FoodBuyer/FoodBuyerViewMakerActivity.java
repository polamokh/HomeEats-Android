package com.example.homeeats.Activities.FoodBuyer;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homeeats.Adapters.FoodBuyerMealsRecyclerAdapter;
import com.example.homeeats.Dao.FoodMakerDao;
import com.example.homeeats.Listeners.RetrievalEventListener;
import com.example.homeeats.Models.FoodMaker;
import com.example.homeeats.Models.MealItem;
import com.example.homeeats.R;

import java.util.List;

public class FoodBuyerViewMakerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foodbuyer_view_maker_activity);

        ImageView imageView = findViewById(R.id.foodBuyerViewMakerImage);
        final TextView textViewName = findViewById(R.id.foodBuyerViewMakerName);
        final TextView textViewPhone = findViewById(R.id.foodBuyerViewMakerPhone);
        final TextView textViewEmail = findViewById(R.id.foodBuyerViewMakerEmail);
        //TODO: Add maker location

        final RecyclerView recyclerView = findViewById(R.id.foodBuyerViewMakerRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        String makerID = getIntent().getExtras().getString("FoodMakerID");
        FoodMakerDao.GetInstance().get(makerID, new RetrievalEventListener<FoodMaker>() {
                    @Override
                    public void OnDataRetrieved(FoodMaker foodMaker) {
                        //TODO: Add maker image
                        textViewName.setText(foodMaker.name);
                        textViewPhone.setText(foodMaker.phone);
                        textViewEmail.setText(foodMaker.emailAddress);
                    }
                });

        FoodMakerDao.GetInstance().GetFoodMakerMeals(makerID, new RetrievalEventListener<List<MealItem>>() {
                    @Override
                    public void OnDataRetrieved(List<MealItem> mealItems) {
                        FoodBuyerMealsRecyclerAdapter adapter =
                                new FoodBuyerMealsRecyclerAdapter(mealItems);
                        recyclerView.setAdapter(adapter);
                    }
                });
    }
}
