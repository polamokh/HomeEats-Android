package com.example.homeeats.Activities.FoodBuyer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homeeats.Adapters.FoodBuyerMealsRecyclerAdapter;
import com.example.homeeats.Dao.FoodBuyerDao;
import com.example.homeeats.Dao.MealItemDao;
import com.example.homeeats.Models.MealItem;
import com.example.homeeats.R;
import com.example.homeeats.Listeners.RetrievalEventListener;

import java.util.List;

public class FoodBuyerMealsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.foodbuyer_fragment, container, false);

        final RecyclerView recyclerView = view.findViewById(R.id.foodBuyerRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        MealItemDao.GetInstance().getAll(new RetrievalEventListener<List<MealItem>>() {
            @Override
            public void OnDataRetrieved(List<MealItem> mealItems) {
                FoodBuyerMealsRecyclerAdapter adapter =
                        new FoodBuyerMealsRecyclerAdapter(mealItems);
                recyclerView.setAdapter(adapter);
            }
        });

        return view;
    }
}
