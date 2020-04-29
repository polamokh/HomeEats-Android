package com.example.homeeats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        userAuthenticationDatabase.SignUpFoodBuyer(this, new FoodBuyer(null, name, "male", email, "011", new LatLng(1, 1)),
                password);
        ;
    }
}
