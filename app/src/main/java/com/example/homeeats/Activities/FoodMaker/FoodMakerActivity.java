package com.example.homeeats.Activities.FoodMaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.homeeats.R;
import com.google.android.material.navigation.NavigationView;

import static com.example.homeeats.R.id.foodMakerToolbar;

public class FoodMakerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foodmaker_activity);

        Toolbar toolbar = findViewById(foodMakerToolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.foodMakerDrawerLayout);
        NavigationView navigationView = findViewById(R.id.foodMakerNavView);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

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
