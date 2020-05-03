package com.example.homeeats.Activities.FoodMaker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homeeats.Adapters.FoodMakerMealsRecycleAdapter;
import com.example.homeeats.Dao.FoodMakerDao;
import com.example.homeeats.Models.MealItem;
import com.example.homeeats.R;
import android.content.Intent;
import com.example.homeeats.RetrievalEventListener;

import java.util.List;

public class FoodMakerMealsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.foodmaker_fragment, container, false);

        final RecyclerView recyclerView = view.findViewById(R.id.foodMakerRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        String foodMakerID = getActivity().getIntent().getExtras().getString("FoodMakerID");
        FoodMakerDao.GetInstance().GetFoodMakerMeals(foodMakerID,
                new RetrievalEventListener<List<MealItem>>() {
                    @Override
                    public void OnDataRetrieved(List<MealItem> mealItems) {
                        FoodMakerMealsRecycleAdapter foodMakerMealsRecycleAdapter =
                                new FoodMakerMealsRecycleAdapter(mealItems);
                        recyclerView.setAdapter(foodMakerMealsRecycleAdapter);
                    }
                });



        return view;
    }
}
