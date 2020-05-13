package com.example.homeeats.Activities.FoodBuyer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firbasedao.Listeners.RetrievalEventListener;
import com.example.homeeats.Adapters.FoodBuyerCartOrdersRecyclerAdapter;
import com.example.homeeats.Dao.FoodBuyerDao;
import com.example.homeeats.Dao.OrderDao;
import com.example.homeeats.Models.FoodBuyer;
import com.example.homeeats.Models.Order;
import com.example.homeeats.R;

import java.util.List;

public class FoodBuyerCartFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.foodbuyer_cart_fragment, container, false);

        final TextView textViewTotalPrice = view.findViewById(R.id.foodBuyerCartOrdersTotalPrice);

        final RecyclerView recyclerView = view.findViewById(R.id.foodBuyerCartOrdersRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        FoodBuyerDao.GetInstance().GetFoodBuyerOrders(getActivity().getIntent().getExtras()
                .getString("FoodBuyerID"), new RetrievalEventListener<List<Order>>() {
            @Override
            public void OnDataRetrieved(List<Order> orders) {
                FoodBuyerCartOrdersRecyclerAdapter adapter =
                        new FoodBuyerCartOrdersRecyclerAdapter(orders, view.getContext());
                recyclerView.setAdapter(adapter);

                Double totalPrice = 0.0;
                for (Order order : orders) {
                    totalPrice += order.totalPrice;
                }
                textViewTotalPrice.setText("EGP" + totalPrice.toString());
            }
        });

        return view;
    }
}
