package com.example.homeeats.Activities.FoodBuyer;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firbasedao.Listeners.RetrievalEventListener;
import com.example.homeeats.Adapters.FoodBuyerMealsRecyclerAdapter;
import com.example.homeeats.Dao.MealItemDao;
import com.example.homeeats.Models.MealItem;
import com.example.homeeats.R;

import java.util.List;

public class FoodBuyerMealsFragment extends Fragment {
    private static final String[] searchBy = new String[] {"Name", "Category"};
    private static List<MealItem> meals;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.foodbuyer_fragment, container, false);

        final EditText editTextSearch = view.findViewById(R.id.foodBuyerMealsSearch);
        final Spinner spinner = view.findViewById(R.id.foodBuyerMealsSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_list_item_1, searchBy);
        spinner.setAdapter(adapter);

        final RecyclerView recyclerView = view.findViewById(R.id.foodBuyerRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        MealItemDao.GetInstance().getAll(new RetrievalEventListener<List<MealItem>>() {
            @Override
            public void OnDataRetrieved(List<MealItem> mealItems) {
                meals = mealItems;

                for (int i = 0; i < meals.size(); i++)
                    if (!meals.get(i).isAvailable)
                        meals.remove(i);

                FoodBuyerMealsRecyclerAdapter mealsRecyclerAdapter =
                        new FoodBuyerMealsRecyclerAdapter(meals);
                recyclerView.setAdapter(mealsRecyclerAdapter);
            }
        });

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (spinner.getSelectedItemPosition() == 0) {
                    FoodBuyerMealsRecyclerAdapter adapter = new FoodBuyerMealsRecyclerAdapter(
                            MealItemDao.GetInstance().FilterMealsByNameAndCategory(
                                    meals, s.toString(), ""
                            )
                    );
                    recyclerView.setAdapter(adapter);
                }
                else if (spinner.getSelectedItemPosition() == 1) {
                    FoodBuyerMealsRecyclerAdapter adapter = new FoodBuyerMealsRecyclerAdapter(
                            MealItemDao.GetInstance().FilterMealsByNameAndCategory(
                                    meals, "", s.toString()
                            )
                    );
                    recyclerView.setAdapter(adapter);
                }
            }
        });

        return view;
    }
}
