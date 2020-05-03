package com.example.homeeats.Activities.FoodMaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homeeats.Activities.MainActivity;
import com.example.homeeats.Dao.FoodMakerDao;
import com.example.homeeats.Models.FoodMaker;
import com.example.homeeats.R;
import com.example.homeeats.RetrievalEventListener;
import com.example.homeeats.TaskListener;
import com.example.homeeats.UserAuthenticationDatabase;
import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;

import static com.example.homeeats.R.id.foodMakerToolbar;

public class FoodMakerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Button addMealButton;
    private ClipData.Item signOut;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foodmaker_activity);

        addMealButton = findViewById(R.id.foodmaker_add_meal_button);

        Toolbar toolbar = findViewById(foodMakerToolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.foodMakerDrawerLayout);
        navigationView = findViewById(R.id.foodMakerNavView);

        final TextView textViewNavHeaderName = navigationView.getHeaderView(0)
                .findViewById(R.id.navHeaderTextViewName);
        final TextView textViewNavHeaderEmail = navigationView.getHeaderView(0)
                .findViewById(R.id.navHeaderTextViewEmail);
        FoodMakerDao.GetInstance().get(getIntent().getExtras().getString("FoodMakerID"),
                new RetrievalEventListener<FoodMaker>() {
                    @Override
                    public void OnDataRetrieved(FoodMaker foodMaker) {
                        textViewNavHeaderName.setText(foodMaker.name);
                        textViewNavHeaderEmail.setText(foodMaker.emailAddress);
                    }
                });

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
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
        item.setChecked(true);

        switch (item.getItemId()) {
            case R.id.foodMakerNavLogout:
                UserAuthenticationDatabase.GetInstance().SignOut(new TaskListener() {
                    @Override
                    public void OnSuccess() {
                        Toast.makeText(getApplicationContext(), "Goodbye :)", Toast.LENGTH_LONG).show();
                        FoodMakerActivity.super.onBackPressed();
                    }

                    @Override
                    public void OnFail() {
                        Toast.makeText(getApplicationContext(), "How did you reach here?", Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case R.id.foodMakerNavSettings:
                Intent intent = new Intent(FoodMakerActivity.this, FoodMakerEditProfileActivity.class);
                String foodMakerID = getIntent().getExtras().getString("FoodMakerID");
                intent.putExtra("FoodMakerID", foodMakerID);
                startActivity(intent);
                break;
            case R.id.foodMakerNavRequests:
                //TODO
                break;
            case R.id.foodMakerNavMeals:
                getSupportFragmentManager().beginTransaction().replace(R.id.foodMakerFragmentContainer,
                        new FoodMakerMealsFragment()).commit();
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }
}
