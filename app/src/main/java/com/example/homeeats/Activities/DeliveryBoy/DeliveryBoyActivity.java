package com.example.homeeats.Activities.DeliveryBoy;

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
import com.example.homeeats.Activities.FoodBuyer.FoodBuyerActivity;
import com.example.homeeats.Activities.FoodBuyer.FoodBuyerCartFragment;
import com.example.homeeats.Activities.FoodBuyer.FoodBuyerEditProfileFragment;
import com.example.homeeats.Activities.FoodBuyer.FoodBuyerMakersFragment;
import com.example.homeeats.Activities.FoodBuyer.FoodBuyerMealsFragment;
import com.example.homeeats.Dao.DeliveryBoyDao;
import com.example.homeeats.Dao.FoodBuyerDao;
import com.example.homeeats.Models.DeliveryBoy;
import com.example.homeeats.Models.FoodBuyer;
import com.example.homeeats.R;
import com.example.homeeats.UserAuthenticationDatabase;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

public class DeliveryBoyActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deliveryboy_activity);
        Toolbar toolbar = findViewById(R.id.DeliveryBoyToolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.DeliveryBoyDrawerLayout);
        navigationView = findViewById(R.id.DeliveryBoyNavView);

        final TextView textViewNavHeaderName = navigationView.getHeaderView(0)
                .findViewById(R.id.navHeaderTextViewName);
        final TextView textViewNavHeaderEmail = navigationView.getHeaderView(0)
                .findViewById(R.id.navHeaderTextViewEmail);
        final ImageView imageViewNavHeader = navigationView.getHeaderView(0)
                .findViewById(R.id.navHeaderImageView);
        DeliveryBoyDao.GetInstance().get(getIntent().getExtras().getString("DeliveryBoyID"), new RetrievalEventListener<DeliveryBoy>() {
            @Override
            public void OnDataRetrieved(DeliveryBoy deliveryBoy) {
                textViewNavHeaderName.setText(deliveryBoy.name);
                textViewNavHeaderEmail.setText(deliveryBoy.emailAddress);
                Picasso.get()
                        .load(deliveryBoy.photo)
                        .into(imageViewNavHeader);
            }
        });
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.DeliveryBoyFragmentContainer,
                    new DeliveryBoyOrdersFragment()).commit();
            navigationView.setCheckedItem(R.id.DeliveryBoyNavOrders);
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
            case R.id.DeliveryBoyNavOrders:
                getSupportFragmentManager().beginTransaction().replace(R.id.DeliveryBoyFragmentContainer,
                        new DeliveryBoyOrdersFragment()).commit();
                break;
            case R.id.DeliverBoyNavSettings:
                getSupportFragmentManager().beginTransaction().replace(R.id.DeliveryBoyFragmentContainer,
                        new DeliveryBoyEditProfileFragment()).commit();
                break;
            case R.id.DeliveryBoyNavLogout:
                UserAuthenticationDatabase.GetInstance().SignOut(new TaskListener() {
                    @Override
                    public void OnSuccess() {
                        DeliveryBoyActivity.super.onBackPressed();
                    }

                    @Override
                    public void OnFail() {
                    }
                });
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
