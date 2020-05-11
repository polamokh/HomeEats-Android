package com.example.homeeats.Activities.FoodBuyer;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.firbasedao.Listeners.RetrievalEventListener;
import com.example.firbasedao.Listeners.TaskListener;
import com.example.homeeats.Dao.FoodBuyerDao;
import com.example.homeeats.Models.FoodBuyer;
import com.example.homeeats.R;
import com.example.homeeats.UserAuthenticationDatabase;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

public class FoodBuyerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foodbuyer_activity);

        Toolbar toolbar = findViewById(R.id.foodBuyerToolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.foodBuyerDrawerLayout);
        navigationView = findViewById(R.id.foodBuyerNavView);

        final TextView textViewNavHeaderName = navigationView.getHeaderView(0)
                .findViewById(R.id.navHeaderTextViewName);
        final TextView textViewNavHeaderEmail = navigationView.getHeaderView(0)
                .findViewById(R.id.navHeaderTextViewEmail);
        final ImageView imageViewNavHeader = navigationView.getHeaderView(0)
                .findViewById(R.id.navHeaderImageView);
        FoodBuyerDao.GetInstance().get(getIntent().getExtras().getString("FoodBuyerID"),
                new RetrievalEventListener<FoodBuyer>() {
                    @Override
                    public void OnDataRetrieved(FoodBuyer foodBuyer) {
                        textViewNavHeaderName.setText(foodBuyer.name);
                        textViewNavHeaderEmail.setText(foodBuyer.emailAddress);
                        Picasso.get()
                                .load(foodBuyer.photo)
                                .into(imageViewNavHeader);
                    }
                });

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.foodBuyerFragmentContainer,
                    new FoodBuyerMealsFragment()).commit();
            navigationView.setCheckedItem(R.id.foodBuyerNavMeals);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.isChecked()) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }

        item.setChecked(true);

        //TODO: Complete FoodBuyer missing fragments
        switch (item.getItemId()) {
            case R.id.foodBuyerNavMeals:
                getSupportFragmentManager().beginTransaction().replace(R.id.foodBuyerFragmentContainer,
                        new FoodBuyerMealsFragment()).commit();
                break;
            case R.id.foodBuyerNavMakers:
                getSupportFragmentManager().beginTransaction().replace(R.id.foodBuyerFragmentContainer,
                        new FoodBuyerMakersFragment()).commit();
                break;
            case R.id.foodBuyerNavCart:
                break;
            case R.id.foodBuyerNavOrders:
                break;
            case R.id.foodBuyerNavSettings:
                getSupportFragmentManager().beginTransaction().replace(R.id.foodBuyerFragmentContainer,
                        new FoodBuyerEditProfileFragment()).commit();
                break;
            case R.id.foodBuyerNavLogout:
                UserAuthenticationDatabase.GetInstance().SignOut(new TaskListener() {
                    @Override
                    public void OnSuccess() {
                        FoodBuyerActivity.super.onBackPressed();
                    }

                    @Override
                    public void OnFail() { }
                });
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
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
