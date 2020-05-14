package com.example.homeeats.Activities.FoodBuyer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firbasedao.Listeners.RetrievalEventListener;
import com.example.homeeats.Adapters.FoodBuyerOrderRecycleAdapter;
import com.example.homeeats.Dao.FoodBuyerDao;
import com.example.homeeats.Dao.OrderDao;
import com.example.homeeats.Models.Order;
import com.example.homeeats.R;

import java.util.List;

public class FoodBuyerOrdersFragment extends Fragment {
    private List<Order> ordersList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.foodbuyer_orders_fragment, container, false);

        final RecyclerView recyclerView = view.findViewById(R.id.foodBuyerOrdersRecyclerView);
        
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        String foodBuyerID = getActivity().getIntent().getExtras().getString("FoodBuyerID");

        FoodBuyerDao.GetInstance().GetFoodBuyerOrders(foodBuyerID, new RetrievalEventListener<List<Order>>() {
            @Override
            public void OnDataRetrieved(List<Order> orders) {
                ordersList = orders;
                FoodBuyerOrderRecycleAdapter adapter = new FoodBuyerOrderRecycleAdapter(ordersList);
                recyclerView.setAdapter(adapter);
            }
        });
        return view;
    }
}
