package com.example.homeeats.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homeeats.Dao.DeliveryBoyDao;
import com.example.homeeats.Dao.FoodBuyerDao;
import com.example.homeeats.Dao.FoodMakerDao;
import com.example.homeeats.Dao.MealItemDao;
import com.example.homeeats.Dao.OrderDao;
import com.example.homeeats.Models.DeliveryBoy;
import com.example.homeeats.Models.FoodBuyer;
import com.example.homeeats.Models.FoodMaker;
import com.example.homeeats.Models.MealItem;
import com.example.homeeats.Models.Order;
import com.example.homeeats.Models.OrderItem;
import com.example.homeeats.Models.OrderStatus;
import com.example.homeeats.R;
import com.example.homeeats.RetrievalEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText editTextEmail;
    EditText editTextPassword;
    Button buttonLogin;
    TextView textViewSignup;

    private static final String TAG = "EmailPassword";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewSignup = findViewById(R.id.textViewSignup);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(editTextEmail.getText().toString(), editTextPassword.getText().toString());
            }
        });

        textViewSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        Toast.makeText(this, "Before",
                Toast.LENGTH_SHORT).show();

        final OrderDao orderDao = OrderDao.GetInstance();
        final List<OrderItem> orderItems = new ArrayList<OrderItem>();

        final Order order = new Order(null, new LatLng(1, 1), null, null, null, null, "Good order", 5, null, OrderStatus.Accepted);
        FoodMakerDao.GetInstance().get("ctkAYZMA2sUoiQRKhAOxTy9nOc92", new RetrievalEventListener<FoodMaker>() {
            @Override
            public void OnDataRetrieved(FoodMaker foodMaker) {
                order.foodMaker = foodMaker;
                FoodBuyerDao.GetInstance().get("lrOtoBNeAfcqiDlJJCJy0zSI7Nn2", new RetrievalEventListener<FoodBuyer>() {
                    @Override
                    public void OnDataRetrieved(FoodBuyer foodBuyer) {
                        order.foodBuyer = foodBuyer;
                        DeliveryBoyDao.GetInstance().get("QKchGRN7JBh3nZUiIP9O5GW5pfD3", new RetrievalEventListener<DeliveryBoy>() {
                            @Override
                            public void OnDataRetrieved(DeliveryBoy deliveryBoy) {
                                order.deliveryBoy = deliveryBoy;
                                MealItemDao.GetInstance().get("-M64XGeazTb_HbQnboZB", new RetrievalEventListener<MealItem>() {
                                    @Override
                                    public void OnDataRetrieved(MealItem mealItem) {
                                        order.orderItems = new ArrayList<OrderItem>();
                                        order.orderItems.add(new OrderItem(mealItem, 2, "da2aa zyadaa", 5));
                                        orderDao.save(order, orderDao.GetNewKey());
                                        order.totalPrice = order.getTotalPrice();
                                        Toast.makeText(getApplicationContext(), "Added order", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this, "Logged in successfully",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
