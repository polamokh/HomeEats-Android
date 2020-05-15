package com.example.homeeats.Activities.FoodBuyer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.firbasedao.Listeners.RetrievalEventListener;
import com.example.homeeats.Dao.DeliveryBoyDao;
import com.example.homeeats.Dao.FoodMakerDao;
import com.example.homeeats.Dao.OrderDao;
import com.example.homeeats.Models.DeliveryBoy;
import com.example.homeeats.Models.FoodMaker;
import com.example.homeeats.Models.Order;
import com.example.homeeats.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.squareup.picasso.Picasso;

public class FoodBuyerOrderDetailedActivity extends AppCompatActivity implements OnMapReadyCallback {
    CardView cardView;
    ImageView profileImage;
    TextView foodMakerName;
    TextView orderId;
    TextView orderStatus;
    TextView deliveryBoyName;
    TextView totalPrice;
    Order currentOrder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.foodbuyer_order_detailed_activity);
            cardView = findViewById(R.id.foodBuyerOrderDetailedView);
            profileImage = findViewById(R.id.foodBuyerOrderDetailedMakerImage);
            foodMakerName = findViewById(R.id.foodBuyerOrderDetailedMakerName);
            orderId = findViewById(R.id.foodBuyerOrderDetailedID);
            orderStatus= findViewById(R.id.foodBuyerOrderDetailedStatus);
            deliveryBoyName = findViewById(R.id.foodBuyerOrderDetailsDeliveryBoyName);
            totalPrice = findViewById(R.id.foodBuyerOrderDetailsTotalPrice);

            // Setting label values :)
            Intent intent = getIntent();
            String orderID = intent.getStringExtra("orderID");
            OrderDao.GetInstance().get(orderID, new RetrievalEventListener<Order>() {
                @Override
                public void OnDataRetrieved(Order order) {
                    currentOrder = order;
                    orderId.setText(order.id);
                    orderStatus.setText(order.orderStatus.toString());
                    totalPrice.setText(order.totalPrice.toString());

                    DeliveryBoyDao.GetInstance().get(order.deliveryBoyId, new RetrievalEventListener<DeliveryBoy>() {
                        @Override
                        public void OnDataRetrieved(DeliveryBoy deliveryBoy) {
                            deliveryBoyName.setText(deliveryBoy.name);
                        }
                    });
                    FoodMakerDao.GetInstance().get(order.foodMakerId, new RetrievalEventListener<FoodMaker>() {
                        @Override
                        public void OnDataRetrieved(FoodMaker foodMaker) {
                            foodMakerName.setText(foodMaker.name);
                            Picasso.get().load(foodMaker.photo).into(profileImage);
                        }
                    });
                }
            });
        }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
