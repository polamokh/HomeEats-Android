package com.example.homeeats.Activities.FoodBuyer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firbasedao.Listeners.RetrievalEventListener;
import com.example.firbasedao.Listeners.TaskListener;
import com.example.homeeats.Adapters.FoodBuyerCartOrdersRecyclerAdapter;
import com.example.homeeats.Dao.CartOrderItemDao;
import com.example.homeeats.Dao.DeliveryBoyDao;
import com.example.homeeats.Dao.FoodBuyerDao;
import com.example.homeeats.Dao.OrderDao;
import com.example.homeeats.Models.CartOrderItem;
import com.example.homeeats.Models.DeliveryBoy;
import com.example.homeeats.Models.FoodBuyer;
import com.example.homeeats.Models.Order;
import com.example.homeeats.Models.OrderItem;
import com.example.homeeats.Models.OrderStatus;
import com.example.homeeats.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FoodBuyerCartFragment extends Fragment {
    private HashMap<String, List<CartOrderItem>> orders;
    private ArrayList<String> foodMakerKeys;

    private FoodBuyerCartOrdersRecyclerAdapter adapter;

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
                orders = stringListHashMap;
                foodMakerKeys = new ArrayList<>(orders.keySet());

                adapter = new FoodBuyerCartOrdersRecyclerAdapter(orders,
                        foodMakerKeys, FoodBuyerCartFragment.this);
                recyclerView.setAdapter(adapter);

                recalculateTotalPrice(orders, foodMakerKeys);
            }
        });

        buttonCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                checkoutOrders(v);
            }
        });

        return view;
    }

    public void recalculateTotalPrice(HashMap<String, List<CartOrderItem>> orders,
                                      List<String> foodMakerKeys) {
        if (foodMakerKeys.size() > 0) {
            Double ordersTotalPrice = 0.0;
            for (int i = 0; i < foodMakerKeys.size(); i++) {
                for (CartOrderItem cartOrderItem : orders.get(foodMakerKeys.get(i))) {
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

    private void checkoutOrders(final View v) {
        for (int i = 0; i < foodMakerKeys.size(); i++) {
            final Order order = new Order();
            order.foodMakerId = foodMakerKeys.get(i);
            order.foodBuyerId = getActivity().getIntent().getExtras().getString("FoodBuyerID");
            order.orderItems = new ArrayList<>();
            order.review = "";
            order.rating = 1;
            order.totalPrice = 0.0;
            order.orderStatus = OrderStatus.Pending;

            DeliveryBoyDao.GetInstance().GetAvailableDeliveryBoy(
                    new RetrievalEventListener<DeliveryBoy>() {
                        @Override
                        public void OnDataRetrieved(DeliveryBoy deliveryBoy) {
                            if (deliveryBoy != null) {
                                order.deliveryBoyId = deliveryBoy.id;
                                deliveryBoy.availability = false;
                                DeliveryBoyDao.GetInstance().save(
                                        deliveryBoy, deliveryBoy.id, new TaskListener() {
                                            @Override
                                            public void OnSuccess() {
                                            }

                                            @Override
                                            public void OnFail() {
                                            }
                                        }
                                );
                            }
                            else {
                                Toast.makeText(v.getContext(),
                                        "No available delivery boys",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }

                            FoodBuyerDao.GetInstance().get(order.foodBuyerId,
                                    new RetrievalEventListener<FoodBuyer>() {
                                        @Override
                                        public void OnDataRetrieved(FoodBuyer foodBuyer) {
                                            order.buyerLocation = foodBuyer.location;

                                            int mealsSize = orders.get(order.foodMakerId).size();
                                            for (int j = 0; j < mealsSize; j++) {
                                                order.orderItems.add(orders.get(order.foodMakerId).get(j));
                                            }
                                            OrderDao.GetInstance().save(order, OrderDao.GetInstance().GetNewKey(),
                                                    new TaskListener() {
                                                        @Override
                                                        public void OnSuccess() {
                                                            CartOrderItemDao.GetInstance()
                                                                    .removeFoodBuyerCart(
                                                                            order.foodBuyerId
                                                                    );

                                                            orders.clear();
                                                            foodMakerKeys.clear();
                                                            adapter.notifyDataSetChanged();

                                                            recalculateTotalPrice(orders,
                                                                    foodMakerKeys);


                                                            Toast.makeText(v.getContext(),
                                                                    "Your orders made successfully",
                                                                    Toast.LENGTH_SHORT).show();
                                                        }

                                                        @Override
                                                        public void OnFail() {
                                                        }
                                                    });
                                        }
                                    });
                        }
                    });
        }
    }
}
