package com.example.homeeats.Activities.FoodMaker;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.homeeats.Adapters.FoodMakerMealsRecycleAdapter;
import com.example.homeeats.Adapters.FoodMakerRequestsAdapter;
import com.example.homeeats.Dao.FoodMakerDao;
import com.example.homeeats.Models.MealItem;
import com.example.homeeats.Models.Order;
import com.example.homeeats.R;
import com.example.homeeats.RetrievalEventListener;

import java.util.List;

public class FoodMakerRequestsFragment extends Fragment {


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_food_maker_requests, container, false);
        final RecyclerView recyclerView = view.findViewById(R.id.foodMakerRequestRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        String foodMakerID = getActivity().getIntent().getExtras().getString("FoodMakerID");
        Toast.makeText(view.getContext(),"here",Toast.LENGTH_LONG).show();
        FoodMakerDao.GetInstance().GetFoodMakerOrders(foodMakerID, new RetrievalEventListener<List<Order>>() {
            @Override
            public void OnDataRetrieved(List<Order> orders) {

                FoodMakerRequestsAdapter foodMakerRequestsRecycleAdapter =
                        new FoodMakerRequestsAdapter(orders);

                recyclerView.setAdapter(foodMakerRequestsRecycleAdapter);
            }
        });
        Toast.makeText(view.getContext(),"abl el return",Toast.LENGTH_LONG).show();
        return view;
    }

}
