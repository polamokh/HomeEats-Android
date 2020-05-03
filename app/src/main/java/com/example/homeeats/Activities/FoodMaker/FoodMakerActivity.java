package com.example.homeeats.Activities.FoodMaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.homeeats.R;
import com.google.android.material.navigation.NavigationView;

import static com.example.homeeats.R.id.foodMakerToolbar;

public class FoodMakerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Button addMealButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foodmaker_activity);

        addMealButton = findViewById(R.id.foodmaker_add_meal_button);
        drawerLayout = findViewById(R.id.foodMakerDrawerLayout);
        NavigationView navigationView = findViewById(R.id.foodMakerNavView);
        Toolbar toolbar = findViewById(foodMakerToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        setSupportActionBar(toolbar);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        addMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodMakerActivity.this, FoodMakerAddMealActivity.class);
                String foodMakerID = getIntent().getExtras().getString("FoodMakerID");
                intent.putExtra("FoodMakerID", foodMakerID);
                startActivity(intent);
            }
        });

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.foodMakerFragmentContainer,
                    new FoodMakerMealsFragment()).commit();
            navigationView.setCheckedItem(R.id.foodMakerNavMeals);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
