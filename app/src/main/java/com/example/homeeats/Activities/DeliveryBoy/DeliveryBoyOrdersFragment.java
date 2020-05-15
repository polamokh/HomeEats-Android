package com.example.homeeats.Activities.DeliveryBoy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.firbasedao.Listeners.RetrievalEventListener;
import com.example.homeeats.Adapters.DeliveryBoyRequestsRecycleAdapter;
import com.example.homeeats.Adapters.FoodMakerRequestsRecycleAdapter;
import com.example.homeeats.Dao.DeliveryBoyDao;
import com.example.homeeats.Dao.FoodMakerDao;
import com.example.homeeats.Models.Order;
import com.example.homeeats.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;


public class DeliveryBoyOrdersFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delivery_boy_orders, container, false);
        final RecyclerView recyclerView = view.findViewById(R.id.DeliveryBoyRequestsRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        String DeliveryBoyID = getActivity().getIntent().getExtras().getString("DeliveryBoyID");
        DeliveryBoyDao.GetInstance().GetDeliveryBoyOrder(DeliveryBoyID, new RetrievalEventListener<List<Order>>() {
            @Override
            public void OnDataRetrieved(List<Order> orders) {
                DeliveryBoyRequestsRecycleAdapter deliveryBoyRequestsRecycleAdapter = new DeliveryBoyRequestsRecycleAdapter(orders);
                recyclerView.setAdapter(deliveryBoyRequestsRecycleAdapter);
            }
        });

        return view;
    }
}
