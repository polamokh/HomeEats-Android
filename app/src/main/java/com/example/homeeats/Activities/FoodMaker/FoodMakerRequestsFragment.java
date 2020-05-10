package com.example.homeeats.Activities.FoodMaker;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.firbasedao.Listeners.RetrievalEventListener;
import com.example.homeeats.Adapters.FoodMakerMealsRecycleAdapter;
import com.example.homeeats.Adapters.FoodMakerRequestsRecycleAdapter;
import com.example.homeeats.Dao.FoodMakerDao;
import com.example.homeeats.Models.MealItem;
import com.example.homeeats.Models.Order;
import com.example.homeeats.R;


import java.util.List;

public class FoodMakerRequestsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_maker_requests, container, false);
        final RecyclerView recyclerView = view.findViewById(R.id.foodMakerRequestsRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        String foodMakerID = getActivity().getIntent().getExtras().getString("FoodMakerID");
        FoodMakerDao.GetInstance().GetFoodMakerOrders(foodMakerID, new RetrievalEventListener<List<Order>>()
        {

            @Override
            public void OnDataRetrieved(List<Order> orders)
            {
                FoodMakerRequestsRecycleAdapter foodMakerRequestsRecycleAdapter =
                        new FoodMakerRequestsRecycleAdapter(orders);
                recyclerView.setAdapter(foodMakerRequestsRecycleAdapter);
            }
        });
        return view;
    }
}
