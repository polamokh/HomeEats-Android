package com.example.homeeats;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.homeeats.Dao.DeliveryBoyDao;
import com.example.homeeats.Dao.FoodBuyerDao;
import com.example.homeeats.Dao.FoodMakerDao;
import com.example.homeeats.Dao.UserPrimitiveDataDao;
import com.example.homeeats.Models.Client;
import com.example.homeeats.Models.DeliveryBoy;
import com.example.homeeats.Models.FoodBuyer;
import com.example.homeeats.Models.FoodMaker;
import com.example.homeeats.Models.UserPrimitiveData;
import com.example.homeeats.Models.UserType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
    private void buildUser(FirebaseUser user, final RetrievalEventListener<Client> retrievalEventListener)
    {
        if(user == null) {
            retrievalEventListener.OnDataRetrieved(null);
            return;
        }
        String id = user.getUid();
        UserPrimitiveDataDao.GetInstance().get(id, new RetrievalEventListener<UserPrimitiveData>() {
            @Override
            public void OnDataRetrieved(UserPrimitiveData userPrimitiveData) {
                switch (userPrimitiveData.userType){
                    case FoodBuyer:
                        FoodBuyerDao.GetInstance().get(userPrimitiveData.id, new RetrievalEventListener<FoodBuyer>() {
                            @Override
                            public void OnDataRetrieved(FoodBuyer foodBuyer) {
                                retrievalEventListener.OnDataRetrieved(foodBuyer);
                            }
                        });
                        break;
                    case FoodMaker:
                        FoodMakerDao.GetInstance().get(userPrimitiveData.id, new RetrievalEventListener<FoodMaker>() {
                            @Override
                            public void OnDataRetrieved(FoodMaker foodMaker) {
                                retrievalEventListener.OnDataRetrieved(foodMaker);
                            }
                        });
                        break;
                    case DeliverBoy:
                        DeliveryBoyDao.GetInstance().get(userPrimitiveData.id, new RetrievalEventListener<DeliveryBoy>() {
                            @Override
                            public void OnDataRetrieved(DeliveryBoy deliveryBoy) {
                                retrievalEventListener.OnDataRetrieved(deliveryBoy);
                            }
                        });
                        break;
                    case Invalid:
                        retrievalEventListener.OnDataRetrieved(null);
                }
            }
        });
    }
    public void GetActiveClient(final RetrievalEventListener<Client> retrievalEventListener)
    {
        FirebaseUser user = mAuth.getCurrentUser();
        buildUser(user, new RetrievalEventListener<Client>() {
            @Override
            public void OnDataRetrieved(Client client) {
                retrievalEventListener.OnDataRetrieved(client);
            }
        });
    }

    public void SignUpFoodMaker(final AppCompatActivity appCompatActivity, final FoodMaker foodMaker, String password)
    {
        mAuth.createUserWithEmailAndPassword(foodMaker.emailAddress, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = mAuth.getCurrentUser();
                    FoodMakerDao foodMakerDao = FoodMakerDao.GetInstance();
                    foodMaker.id = user.getUid();
                    foodMakerDao.save(foodMaker, foodMaker.id);
                    UserPrimitiveData userPrimitiveData = new UserPrimitiveData(user.getUid(), UserType.FoodMaker);
                    UserPrimitiveDataDao.GetInstance().save(userPrimitiveData,user.getUid());
                    Toast.makeText(appCompatActivity, "Sign up Food Maker succeeded.",
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
                    foodBuyerDao.save(foodBuyer, foodBuyer.id);
                    UserPrimitiveData userPrimitiveData = new UserPrimitiveData(user.getUid(), UserType.FoodBuyer);
                    UserPrimitiveDataDao.GetInstance().save(userPrimitiveData,user.getUid());
                    Toast.makeText(appCompatActivity, "Sign up Food Buyer succeeded.",
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
    public void SignUpDeliveryBoy(final AppCompatActivity appCompatActivity, final DeliveryBoy deliveryBoy, String password)
    {
        mAuth.createUserWithEmailAndPassword(deliveryBoy.emailAddress, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = mAuth.getCurrentUser();
                    DeliveryBoyDao deliveryBoyDao = DeliveryBoyDao.GetInstance();
                    deliveryBoy.id = user.getUid();
                    deliveryBoyDao.save(deliveryBoy, deliveryBoy.id);
                    UserPrimitiveData userPrimitiveData = new UserPrimitiveData(user.getUid(), UserType.DeliverBoy);
                    UserPrimitiveDataDao.GetInstance().save(userPrimitiveData,user.getUid());
                    Toast.makeText(appCompatActivity, "Sign up DeliveryBoy succeeded.",
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
    public void Login(final AppCompatActivity appCompatActivity, String email, String password, final RetrievalEventListener<Client> retrievalEventListener)
    {
     //   throw new UnsupportedOperationException("Not supported yet.");
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = mAuth.getCurrentUser();
                    buildUser(user, new RetrievalEventListener<Client>() {
                        @Override
                        public void OnDataRetrieved(Client client) {
                            retrievalEventListener.OnDataRetrieved(client);
                        }
                    });
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(appCompatActivity, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void SignOut()
    {
        mAuth.signOut();
    }
}
