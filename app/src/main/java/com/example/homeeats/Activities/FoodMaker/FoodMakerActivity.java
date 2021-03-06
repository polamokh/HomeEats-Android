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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homeeats.Activities.MainActivity;
import com.example.homeeats.Dao.FoodMakerDao;
import com.example.homeeats.Models.FoodMaker;
import com.example.homeeats.R;
import com.example.firbasedao.Listeners.RetrievalEventListener;
import com.example.firbasedao.Listeners.TaskListener;
import com.example.homeeats.UserAuthenticationDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import static com.example.homeeats.R.id.foodMakerToolbar;

public class FoodMakerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    private ClipData.Item signOut;
    private NavigationView navigationView;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foodmaker_activity);
        Toolbar toolbar = findViewById(foodMakerToolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.foodMakerDrawerLayout);
        navigationView = findViewById(R.id.foodMakerNavView);

        final TextView textViewNavHeaderName = navigationView.getHeaderView(0)
                .findViewById(R.id.navHeaderTextViewName);
        final TextView textViewNavHeaderEmail = navigationView.getHeaderView(0)
                .findViewById(R.id.navHeaderTextViewEmail);
        final ImageView imageViewNavHeader = navigationView.getHeaderView(0)
                .findViewById(R.id.navHeaderImageView);
        FoodMakerDao.GetInstance().get(getIntent().getExtras().getString("FoodMakerID"),
                new RetrievalEventListener<FoodMaker>() {
                    @Override
                    public void OnDataRetrieved(FoodMaker foodMaker) {
                        textViewNavHeaderName.setText(foodMaker.name);
                        textViewNavHeaderEmail.setText(foodMaker.emailAddress);
                        Picasso.get()
                                .load(foodMaker.photo)
                                .into(imageViewNavHeader);
                    }
                });

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
        if (item.isChecked()) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }

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
                getSupportFragmentManager().beginTransaction().replace(R.id.foodMakerFragmentContainer,
                        new FoodMakerEditProfileFragment()).commit();
                break;
            case R.id.foodMakerNavRequests:
               getSupportFragmentManager().beginTransaction().replace(R.id.foodMakerFragmentContainer,
                        new FoodMakerRequestsFragment()).commit();
                break;
            case R.id.foodMakerNavMeals:
                getSupportFragmentManager().beginTransaction().replace(R.id.foodMakerFragmentContainer,
                        new FoodMakerMealsFragment()).commit();
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
