package com.example.homeeats.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.firbasedao.Listeners.EventListenersListener;
import com.example.firbasedao.Listeners.RetrievalEventListener;
import com.example.firbasedao.Listeners.TaskListener;
import com.example.gmailsender.GmailSender;
import com.example.homeeats.Activities.DeliveryBoy.DeliveryBoyActivity;
import com.example.homeeats.Activities.FoodBuyer.FoodBuyerActivity;
import com.example.homeeats.Activities.FoodMaker.FoodMakerActivity;
import com.example.homeeats.Dao.MealItemDao;
import com.example.homeeats.Dao.OrderDao;
import com.example.homeeats.Dao.UserPrimitiveDataDao;
import com.example.homeeats.LiveLocationService;
import com.example.homeeats.Models.Client;
import com.example.homeeats.Models.MealItem;
import com.example.homeeats.Models.Order;
import com.example.homeeats.Models.OrderItem;
import com.example.homeeats.Models.OrderStatus;
import com.example.homeeats.Models.UserPrimitiveData;
import com.example.homeeats.Models.UserType;
import com.example.homeeats.R;
import com.example.homeeats.UserAuthenticationDatabase;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "1";
    EditText editTextEmail;
    EditText editTextPassword;
    Button buttonLogin;
    TextView textViewSignup;

    private static final String TAG = "EmailPassword";

    private FirebaseAuth mAuth;

    public void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED
        ){//Can add more as per requirement

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.INTERNET},
                    123);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        }

        Toast.makeText(getApplicationContext(), FirebaseInstanceId.getInstance().getToken(), Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewSignup = findViewById(R.id.textViewSignup);

//        testing sending emails
//        GmailSender.setFrom("homeeats.ris.2020@gmail.com");
//        GmailSender.setPassword("HomeEats123");
//        GmailSender.sendEmail("ramyeg26@gmail.com", "Test", "Test email.");
//        AddNewOrder();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Toast failedLoginToast = Toast.makeText(getApplicationContext(), "Invalid login ya 3'aby >:(",Toast.LENGTH_LONG);
                if (editTextEmail.getText().toString().equals("") || editTextPassword.getText().toString().equals("")){
                    failedLoginToast.show();
                    return;
                }
                UserAuthenticationDatabase.GetInstance().Login(editTextEmail.getText().toString(),
                        editTextPassword.getText().toString(), new RetrievalEventListener<Client>() {
                            @Override
                            public void OnDataRetrieved(Client client) {
                                if (client == null){
                                    failedLoginToast.show();
                                    return;
                                }
                                Toast.makeText(getApplicationContext(), "Welcome "+ client.name, Toast.LENGTH_LONG).show();
                                UserPrimitiveDataDao.GetInstance().get(client.id, new RetrievalEventListener<UserPrimitiveData>() {
                                    @Override
                                    public void OnDataRetrieved(UserPrimitiveData userPrimitiveData) {

                                        if (userPrimitiveData.userType == UserType.DeliveryBoy){
                                            runLiveLocationService();
                                            Intent intent = new Intent(MainActivity.this, DeliveryBoyActivity.class);
                                            intent.putExtra("DeliveryBoyID", userPrimitiveData.id);
                                            startActivity(intent);

                                        }else if (userPrimitiveData.userType == UserType.FoodBuyer){
                                            stopLiveLocationService();
                                            Intent intent = new Intent(MainActivity.this, FoodBuyerActivity.class);
                                            intent.putExtra("FoodBuyerID", userPrimitiveData.id);
                                            startActivity(intent);

                                        }else if (userPrimitiveData.userType == UserType.FoodMaker){
                                            stopLiveLocationService();
                                            Intent intent = new Intent(MainActivity.this, FoodMakerActivity.class);
                                            intent.putExtra("FoodMakerID", userPrimitiveData.id);
                                            startActivity(intent);
                                        }else{
                                            stopLiveLocationService();
                                            Toast.makeText(getApplicationContext(), "Invalid Login",Toast.LENGTH_LONG).show();
                                        }

                                    }
                                });
                            }
                        });
            }
        });

        textViewSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Log.e("Tag", key + " , " + getIntent().getExtras().get(key).toString());
                String value = getIntent().getExtras().get(key).toString();
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }

    }

    protected void AddNewOrder()
    {
        final Order order = new Order(null,
                "FnFqPvY2VuMWrLf04ZZacTbrV293",
                "3cs3kGaPDIQiofGVfFw1ckrPNCr2",
                "xdor7utP2rfQCKPUfijxLTky6X83",
                new ArrayList<OrderItem>(),
                "Koshry gamd zo7l2a",
                4,
                0.0,
                OrderStatus.Pending,
                new LatLng(1, 1));
        final EventListenersListener eventListenersListener = new EventListenersListener() {
            @Override
            public void onFinish() {
                order.totalPrice = order.totalPrice;
                OrderDao.GetInstance().save(order, OrderDao.GetInstance().GetNewKey(), new TaskListener() {
                    @Override
                    public void OnSuccess() {
                        Toast.makeText(getApplicationContext(), "Added order", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void OnFail(){

                    }
                });
            }
        };
        List<RetrievalEventListener> mealsEventListeners = new ArrayList<>();
        for(int i = 1; i <= 7; i++){
            final OrderItem orderItem = new OrderItem(null, i, "da2aa zayada", 5, 3242.3);
            RetrievalEventListener<MealItem> mealEventListener = new RetrievalEventListener<MealItem>() {
                @Override
                public void OnDataRetrieved(MealItem mealItem) {
                    orderItem.mealItemId = mealItem.id;
                    orderItem.totalPrice = orderItem.quantity * mealItem.price;
                    order.orderItems.add(orderItem);
                    eventListenersListener.notify(this);
                }
            };
            mealsEventListeners.add(mealEventListener);
            eventListenersListener.Add(mealEventListener);
        }
        eventListenersListener.OnFinishAddingListeners();
        for(int i = 1; i <= 7; i++){
            MealItemDao.GetInstance().get("-M64XGeazTb_HbQnboZB", mealsEventListeners.get(i - 1));
        }
    }

    public void runLiveLocationService(){
        stopLiveLocationService();
        startLiveLocationService();
    }
    public void startLiveLocationService() {
        Intent serviceIntent = new Intent(this, LiveLocationService.class);
        ContextCompat.startForegroundService(this, serviceIntent);
    }
    public void stopLiveLocationService() {
        Intent serviceIntent = new Intent(this, LiveLocationService.class);
        stopService(serviceIntent);
    }

    @Override
    protected void onStart() {



        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();


        /*
        TODO:     ATTENTION! this absolute piece art of a code is perfectly working. It preservers sessions even after closing the application.
         That's right, like facebook. We disabled it for now to make your lives more easier in testing,

         */
        if (currentUser != null){

            UserPrimitiveDataDao.GetInstance().get(currentUser.getUid(), new RetrievalEventListener<UserPrimitiveData>() {
                @Override
                public void OnDataRetrieved(UserPrimitiveData userPrimitiveData) {
                    if (userPrimitiveData.userType == UserType.DeliveryBoy){
                        runLiveLocationService();
                        Intent intent = new Intent(MainActivity.this, DeliveryBoyActivity.class);
                        intent.putExtra("DeliveryBoyID", userPrimitiveData.id);
                        startActivity(intent);

                    }else if (userPrimitiveData.userType == UserType.FoodBuyer){
                        stopLiveLocationService();
                        Intent intent = new Intent(MainActivity.this, FoodBuyerActivity.class);
                        intent.putExtra("FoodBuyerID", userPrimitiveData.id);
                        startActivity(intent);

                    }else if (userPrimitiveData.userType == UserType.FoodMaker){
                        stopLiveLocationService();
                        Intent intent = new Intent(MainActivity.this, FoodMakerActivity.class);
                        intent.putExtra("FoodMakerID", userPrimitiveData.id);
                        startActivity(intent);
                    }else{
                        stopLiveLocationService();
                        Toast.makeText(getApplicationContext(), "Invalid Login",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else
            stopLiveLocationService();

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }
    }
}
