package com.example.homeeats;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.concurrent.Executor;

public class UserAuthenticationDatabase {
    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static UserAuthenticationDatabase singletonObject;
    private UserAuthenticationDatabase(){}
    public static UserAuthenticationDatabase GetInstance()
    {
        if(singletonObject == null)
            singletonObject = new UserAuthenticationDatabase();
        return singletonObject;
    }
    public Client GetActiveClient()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    public void SignUpFoodMaker(FoodMaker foodMaker, String password)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    public void SignUpFoodBuyer(final AppCompatActivity appCompatActivity, final FoodBuyer foodBuyer, String password)
    {
        mAuth.createUserWithEmailAndPassword(foodBuyer.emailAddress, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = mAuth.getCurrentUser();
                    FoodBuyerDao foodBuyerDao = FoodBuyerDao.GetInstance();
                    foodBuyer.id = user.getUid();
                    foodBuyerDao.save(foodBuyer);
                    Toast.makeText(appCompatActivity, "Sign up succeeded.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(appCompatActivity, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

    }
    public void SignUpDeliveryBoy(DeliveryBoy deliveryBoy, String password)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    public Client Login(String email, String password)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    public void SignOut()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
