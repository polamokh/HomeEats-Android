package com.example.homeeats.Activities;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
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

import com.example.homeeats.Dao.DeliveryBoyDao;
import com.example.homeeats.Dao.MealItemDao;
import com.example.homeeats.Dao.OrderDao;
import com.example.homeeats.Listeners.EventListenersListener;
import com.example.homeeats.LiveLocationService;
import com.example.homeeats.Models.Client;
import com.example.homeeats.Models.DeliveryBoy;
import com.example.homeeats.Models.MealItem;
import com.example.homeeats.Models.Order;
import com.example.homeeats.Models.OrderItem;
import com.example.homeeats.Models.OrderStatus;
import com.example.homeeats.R;
import com.example.homeeats.Listeners.RetrievalEventListener;
import com.example.homeeats.Listeners.TaskListener;
import com.example.homeeats.UserAuthenticationDatabase;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
                ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ){//Can add more as per requirement

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        }



        final String TOKEN = "eVo-hvnwC-w:APA91bGq4CdsHioP8st9hhSG_PEMyvlc4NqN2Yqj8IisXzs4u2KnEUa9tICNhz2hXTa6urtYxiChUcDcPi9wtSR-tBFA0OTdpbjkiE7k2mqN56BG2Sd5manOnE8nxdfEVgPGPvc9wBgI";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewSignup = findViewById(R.id.textViewSignup);
        //testing sending emails
//        try {
//            final String fromEmail = "homeeats.ris.2020@gmail.com"; //requires valid gmail id
//            final String password = "HomeEats123"; // correct password for gmail id
//            final String toEmail = "ramyeg26@gmail.com"; // can be any email id
//
//            System.out.println("TLSEmail Start");
//            Properties props = new Properties();
//            props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
//            props.put("mail.smtp.port", "587"); //TLS Port
//            props.put("mail.smtp.auth", "true"); //enable authentication
//            props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
//
//            //create Authenticator object to pass in Session.getInstance argument
//            Authenticator auth = new Authenticator() {
//                //override the getPasswordAuthentication method
//                protected PasswordAuthentication getPasswordAuthentication() {
//                    return new PasswordAuthentication(fromEmail, password);
//                }
//            };
//            Session session = Session.getInstance(props, auth);
//
//            EmailUtil.sendEmail(session, toEmail,"TLSEmail Testing Subject", "TLSEmail Testing Body");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        //start Live Location service
        stopService();
        startLocationService();

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

    }

    public void startLocationService() {
        Intent serviceIntent = new Intent(this, LiveLocationService.class);
        ContextCompat.startForegroundService(this, serviceIntent);
    }
    public void stopService() {
        Intent serviceIntent = new Intent(this, LiveLocationService.class);
        stopService(serviceIntent);
    }

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
