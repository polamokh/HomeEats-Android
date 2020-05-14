package com.example.homeeats.Activities.FoodBuyer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firbasedao.Listeners.RetrievalEventListener;
import com.example.homeeats.Adapters.FoodBuyerCartOrdersRecyclerAdapter;
import com.example.homeeats.Dao.CartOrderItemDao;
import com.example.homeeats.Dao.FoodBuyerDao;
import com.example.homeeats.Dao.OrderDao;
import com.example.homeeats.Models.CartOrderItem;
import com.example.homeeats.Models.FoodBuyer;
import com.example.homeeats.Models.Order;
import com.example.homeeats.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FoodBuyerCartFragment extends Fragment {
    private TextView textViewTotalPrice;
    private TextView textViewTotalPriceLabel;
    private Button buttonCheckout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.foodbuyer_cart_fragment, container, false);

        textViewTotalPrice = view.findViewById(R.id.foodBuyerCartOrdersTotalPrice);
        textViewTotalPriceLabel = view.findViewById(R.id.foodBuyerCartOrdersTotalPriceLabel);
        buttonCheckout = view.findViewById(R.id.foodBuyerCartOrdersCheckout);

        final RecyclerView recyclerView = view.findViewById(R.id.foodBuyerCartOrdersRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        CartOrderItemDao.GetInstance().GetFoodBuyerCart(getActivity().getIntent().getExtras()
                .getString("FoodBuyerID"), new RetrievalEventListener<HashMap<String, List<CartOrderItem>>>() {
            @Override
            public void OnDataRetrieved(HashMap<String, List<CartOrderItem>> stringListHashMap) {
                ArrayList<String> foodMakerKeys = new ArrayList<>(stringListHashMap.keySet());

                FoodBuyerCartOrdersRecyclerAdapter adapter =
                        new FoodBuyerCartOrdersRecyclerAdapter(stringListHashMap,
                                foodMakerKeys, FoodBuyerCartFragment.this);
                recyclerView.setAdapter(adapter);

                recalculateTotalPrice(stringListHashMap, foodMakerKeys);
            }
        });

        buttonCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkoutOrders();
            }
        });

        return view;
    }

    public void recalculateTotalPrice(HashMap<String, List<CartOrderItem>> stringListHashMap,
                                      List<String> foodMakerKeys) {
        if (foodMakerKeys.size() > 0) {
            Double ordersTotalPrice = 0.0;
            for (int i = 0; i < foodMakerKeys.size(); i++) {
                for (CartOrderItem cartOrderItem : stringListHashMap.get(foodMakerKeys.get(i))) {
                    ordersTotalPrice += cartOrderItem.totalPrice;
                }
            }

            textViewTotalPrice.setVisibility(View.VISIBLE);
            textViewTotalPriceLabel.setVisibility(View.VISIBLE);
            buttonCheckout.setVisibility(View.VISIBLE);

            textViewTotalPrice.setText(ordersTotalPrice.toString());
        } else {
            textViewTotalPrice.setVisibility(View.INVISIBLE);
            textViewTotalPriceLabel.setVisibility(View.INVISIBLE);
            buttonCheckout.setVisibility(View.INVISIBLE);
        }
    }

    private void checkoutOrders() {
        //TODO: Add on checkout click
    }
}
