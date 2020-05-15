package com.example.homeeats.Activities.FoodBuyer;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firbasedao.Listeners.RetrievalEventListener;
import com.example.homeeats.Adapters.FoodBuyerMakersRecyclerAdapter;
import com.example.homeeats.Dao.FoodMakerDao;
import com.example.homeeats.Models.FoodMaker;
import com.example.homeeats.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FoodBuyerMakersFragment extends Fragment {
    private List<FoodMaker> makers;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.foodbuyer_makers_fragment, container, false);

        final AutoCompleteTextView autoCompleteTextView =
                view.findViewById(R.id.foodBuyerMakersAutoCompleteTextView);

        final RecyclerView recyclerView = view.findViewById(R.id.foodBuyerMakersRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        FoodMakerDao.GetInstance().getAll(new RetrievalEventListener<List<FoodMaker>>() {
            @Override
            public void OnDataRetrieved(List<FoodMaker> foodMakers) {
                makers = foodMakers;
                ArrayAdapter<String> makersArrayAdapter = new ArrayAdapter<String>(
                        view.getContext(), android.R.layout.simple_list_item_1,
                        getFoodMakersNames(makers));
                autoCompleteTextView.setAdapter(makersArrayAdapter);

                FoodBuyerMakersRecyclerAdapter adapter =
                        new FoodBuyerMakersRecyclerAdapter(makers,
                                getActivity().getIntent().getExtras().getString("FoodBuyerID"));
                recyclerView.setAdapter(adapter);
            }
        });

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                FoodBuyerMakersRecyclerAdapter adapter =
                        new FoodBuyerMakersRecyclerAdapter(
                                FoodMakerDao.GetInstance().FilterMakersByName(makers, s.toString()),
                                getActivity().getIntent().getExtras().getString("FoodBuyerID")
                        );
                recyclerView.setAdapter(adapter);
            }
        });

        return view;
    }

    private List<String> getFoodMakersNames(List<FoodMaker> foodMakers) {
        List<String> names = new ArrayList<>();
        for (FoodMaker foodMaker : foodMakers)
            names.add(foodMaker.name.toLowerCase());
        names = new ArrayList<>(new HashSet<>(names));
        return names;
    }
}
