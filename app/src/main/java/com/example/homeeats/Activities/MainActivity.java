package com.example.homeeats.Activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
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

import com.example.homeeats.Dao.MealItemDao;
import com.example.homeeats.Dao.OrderDao;
import com.example.homeeats.EventListenersListener;
import com.example.homeeats.FcmNotifier;
import com.example.homeeats.MessagingService;
import com.example.homeeats.Models.Client;
import com.example.homeeats.Models.MealItem;
import com.example.homeeats.Models.Order;
import com.example.homeeats.Models.OrderItem;
import com.example.homeeats.Models.OrderStatus;
import com.example.homeeats.Models.UserNotification;
import com.example.homeeats.R;
import com.example.homeeats.RetrievalEventListener;
import com.example.homeeats.TaskListener;
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

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "test channel";
            String description = "channel for testing";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final String TOKEN = "eVo-hvnwC-w:APA91bGq4CdsHioP8st9hhSG_PEMyvlc4NqN2Yqj8IisXzs4u2KnEUa9tICNhz2hXTa6urtYxiChUcDcPi9wtSR-tBFA0OTdpbjkiE7k2mqN56BG2Sd5manOnE8nxdfEVgPGPvc9wBgI";

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
                UserAuthenticationDatabase.GetInstance().Login(editTextEmail.getText().toString(), editTextPassword.getText().toString(), new RetrievalEventListener<Client>() {
                    @Override
                    public void OnDataRetrieved(Client client) {
                        Toast.makeText(getApplicationContext(), client.name, Toast.LENGTH_LONG).show();
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
//        FirebaseMessagingService firebaseMessagingService = new FirebaseMessagingService();
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
//        String channelId = "some_channel_id";
//        CharSequence channelName = "Some Channel";
//        int importance = NotificationManager.IMPORTANCE_DEFAULT;
//        NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
//        notificationChannel.enableLights(true);
//        notificationChannel.setLightColor(Color.RED);
//        notificationChannel.enableVibration(true);
//        notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//        notificationManager.createNotificationChannel(notificationChannel);
//        final OrderDao orderDao = OrderDao.GetInstance();
//        final List<OrderItem> orderItems = new ArrayList<OrderItem>();
//
//        final Order order = new Order(null, new LatLng(1, 1), null, null, null, null, "Good order", 5, null, OrderStatus.Accepted);
//        FoodMakerDao.GetInstance().get("ctkAYZMA2sUoiQRKhAOxTy9nOc92", new RetrievalEventListener<FoodMaker>() {
//            @Override
//            public void OnDataRetrieved(FoodMaker foodMaker) {
//                order.foodMaker = foodMaker;
//                FoodBuyerDao.GetInstance().get("lrOtoBNeAfcqiDlJJCJy0zSI7Nn2", new RetrievalEventListener<FoodBuyer>() {
//                    @Override
//                    public void OnDataRetrieved(FoodBuyer foodBuyer) {
//                        order.foodBuyer = foodBuyer;
//                        DeliveryBoyDao.GetInstance().get("QKchGRN7JBh3nZUiIP9O5GW5pfD3", new RetrievalEventListener<DeliveryBoy>() {
//                            @Override
//                            public void OnDataRetrieved(DeliveryBoy deliveryBoy) {
//                                order.deliveryBoy = deliveryBoy;
//                                MealItemDao.GetInstance().get("-M64XGeazTb_HbQnboZB", new RetrievalEventListener<MealItem>() {
//                                    @Override
//                                    public void OnDataRetrieved(MealItem mealItem) {
//                                        order.orderItems = new ArrayList<OrderItem>();
//                                        order.orderItems.add(new OrderItem(mealItem, 2, "da2aa zyadaa", 5));
//                                        orderDao.save(order, orderDao.GetNewKey());
//                                        order.totalPrice = order.getTotalPrice();
//                                        Toast.makeText(getApplicationContext(), "Added order", Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                            }
//                        });
//                    }
//                });
//            }
//        });
//
//        AddNewOrder();

//        Order order;
//        OrderDao.GetInstance().get("-M67Seiqi02gO1XNvA6T", new RetrievalEventListener<Order>() {
//            @Override
//            public void OnDataRetrieved(Order order) {
//                Toast.makeText(getApplicationContext(), "Get Order", Toast.LENGTH_SHORT);
//            }
//        });
    }

    protected void AddNewOrder()
    {
        final Order order = new Order(null, "ctkAYZMA2sUoiQRKhAOxTy9nOc92", "lrOtoBNeAfcqiDlJJCJy0zSI7Nn2", "QKchGRN7JBh3nZUiIP9O5GW5pfD3", new ArrayList<OrderItem>(), "Koshry gamd zo7l2a", 4, 0.0, OrderStatus.Accepted, new LatLng(1, 1));
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
            final OrderItem orderItem = new OrderItem(null, i, "da2aa zayada", 5);
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
//    protected void AddNewOrder()
//    {
//        final Order order = new Order(null, new LatLng(1, 1), null, null, null, null, "Good order", 5, null, OrderStatus.Accepted);
//        order.orderItems = new ArrayList<OrderItem>();
//        final EventListenersListener eventListenersListener = new EventListenersListener() {
//            @Override
//            public void onFinish() {
//                OrderDao.GetInstance().save(order, OrderDao.GetInstance().GetNewKey());
//                Toast.makeText(getApplicationContext(), "Order finished", Toast.LENGTH_SHORT).show();
//            }
//        };
//        RetrievalEventListener foodMakerListener = new RetrievalEventListener<FoodMaker>() {
//            @Override
//            public void OnDataRetrieved(FoodMaker foodMaker) {
//                order.foodMaker = foodMaker;
//                eventListenersListener.notify(this);
//            }
//        };
//        eventListenersListener.Add(foodMakerListener);
//        RetrievalEventListener foodBuyerListener = new RetrievalEventListener<FoodBuyer>() {
//            @Override
//            public void OnDataRetrieved(FoodBuyer foodBuyer) {
//                order.foodBuyer = foodBuyer;
//                eventListenersListener.notify(this);
//            }
//        };
//        eventListenersListener.Add(foodBuyerListener);
//        RetrievalEventListener deliveryBoyEventListener = new RetrievalEventListener<DeliveryBoy>() {
//            @Override
//            public void OnDataRetrieved(DeliveryBoy deliveryBoy) {
//                order.deliveryBoy = deliveryBoy;
//                eventListenersListener.notify(this);
//            }
//        };
//        eventListenersListener.Add(deliveryBoyEventListener);
//        List<RetrievalEventListener> mealItemsEventListeners = new ArrayList<>();
//        for(int i = 1; i <= 5; i++)
//        {
//            final OrderItem orderItem = new OrderItem(null, i, "da2aa zyadaa", 5);
//            RetrievalEventListener mealItemEventListener = new RetrievalEventListener<MealItem>() {
//                @Override
//                public void OnDataRetrieved(MealItem mealItem) {
//                    orderItem.mealItem = mealItem;
//                    eventListenersListener.notify(this);
//                }
//            };
//            mealItemsEventListeners.add(mealItemEventListener);
//            eventListenersListener.Add(mealItemEventListener);
//            order.orderItems.add(orderItem);
//        }
//        eventListenersListener.OnFinishAddingListeners();
//        FoodBuyerDao.GetInstance().get("lrOtoBNeAfcqiDlJJCJy0zSI7Nn2", foodBuyerListener);
//        FoodMakerDao.GetInstance().get("ctkAYZMA2sUoiQRKhAOxTy9nOc92", foodMakerListener);
//        DeliveryBoyDao.GetInstance().get("QKchGRN7JBh3nZUiIP9O5GW5pfD3", deliveryBoyEventListener);
//        for(int i = 0; i < 5; i++)
//        {
//            MealItemDao.GetInstance().get("-M64XGeazTb_HbQnboZB", mealItemsEventListeners.get(i));
//        }
//    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }
    }
}
