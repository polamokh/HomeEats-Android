package com.example.homeeats.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.homeeats.Models.DeliveryBoy;
import com.example.homeeats.Models.FoodBuyer;
import com.example.homeeats.Models.FoodMaker;
import com.example.homeeats.R;
import com.example.firbasedao.Listeners.TaskListener;
import com.example.homeeats.UserAuthenticationDatabase;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

public class SignUpActivity extends AppCompatActivity {

    EditText editTextSignUpName;
    EditText editTextSignUpEmail;
    EditText editTextSignUpPassword;
    Spinner spinnerSignUpType;
    Button buttonSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        editTextSignUpName = findViewById(R.id.editTextSignUpName);
        editTextSignUpEmail = findViewById(R.id.editTextSignUpEmail);
        editTextSignUpPassword = findViewById(R.id.editTextSignUpPassword);
        spinnerSignUpType = findViewById(R.id.spinnerSignUpType);
        buttonSignUp = findViewById(R.id.buttonSignUp);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp(editTextSignUpName.getText().toString(),
                        editTextSignUpEmail.getText().toString(),
                        editTextSignUpPassword.getText().toString(),
                        spinnerSignUpType.getSelectedItem().toString());
            }
        });
    }

    private void signUp(final String name, String email, String password, String type) {
        UserAuthenticationDatabase userAuthenticationDatabase = UserAuthenticationDatabase.GetInstance();
        //Food Buyer
        if(type.equals(getResources().getStringArray(R.array.user_type)[0])) {
            final FoodBuyer foodBuyer = new FoodBuyer(null, name, "male", email, "011", "", new LatLng(1, 1));
            userAuthenticationDatabase.SignUpFoodBuyer(
                    foodBuyer,
                    password,
                    new TaskListener() {
                        @Override
                        public void OnSuccess() {
                            Toast.makeText(getApplicationContext(), foodBuyer.id, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void OnFail() {
                            Toast.makeText(getApplicationContext(), "Sign up failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        //Food Maker
        else if(type.equals(getResources().getStringArray(R.array.user_type)[1])){
            final FoodMaker foodMaker = new FoodMaker(null, name, "male", email, "011", "", new LatLng(1, 1), 4);
            userAuthenticationDatabase.SignUpFoodMaker(
                    foodMaker,
                    password,
                    new TaskListener() {
                        @Override
                        public void OnSuccess() {
                            Toast.makeText(getApplicationContext(), foodMaker.id, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void OnFail() {
                            Toast.makeText(getApplicationContext(), "Sign up failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        //Delivery Boy
        else if(type.equals(getResources().getStringArray(R.array.user_type)[2])){
            DeliveryBoy deliveryBoy = new DeliveryBoy(null, name, "male", email, "011", "", true, new LatLng(1, 1), new Date().getTime());
            userAuthenticationDatabase.SignUpDeliveryBoy(
                    deliveryBoy,
                    password,
                    new TaskListener() {
                        @Override
                        public void OnSuccess() {
                            Toast.makeText(getApplicationContext(), "Signed up", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void OnFail() {
                            Toast.makeText(getApplicationContext(), "Sign up failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else {
            Toast.makeText(this, "Invalid type", Toast.LENGTH_SHORT).show();
        }
    }
}
