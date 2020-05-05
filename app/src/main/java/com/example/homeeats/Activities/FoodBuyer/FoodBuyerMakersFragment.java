package com.example.homeeats.Activities.FoodBuyer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homeeats.Adapters.FoodBuyerMakersRecyclerAdapter;
import com.example.homeeats.Dao.FoodMakerDao;
import com.example.homeeats.Models.FoodMaker;
import com.example.homeeats.R;
import com.example.firbasedao.Listeners.RetrievalEventListener;

import java.util.List;

public class FoodBuyerMakersFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.foodbuyer_makers_fragment, container, false);

        final RecyclerView recyclerView = view.findViewById(R.id.foodBuyerMakersRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        FoodMakerDao.GetInstance().getAll(new RetrievalEventListener<List<FoodMaker>>() {
            @Override
            public void OnDataRetrieved(List<FoodMaker> foodMakers) {
                FoodBuyerMakersRecyclerAdapter adapter =
                        new FoodBuyerMakersRecyclerAdapter(foodMakers);
                recyclerView.setAdapter(adapter);
            }
        });

        return view;
    }
}
