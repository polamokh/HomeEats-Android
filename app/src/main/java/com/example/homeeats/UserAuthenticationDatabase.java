package com.example.homeeats;

import androidx.annotation.NonNull;

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

import java.util.ArrayList;

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
                    case DeliveryBoy:
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

    public void SignUpFoodMaker(final FoodMaker foodMaker, String password, final TaskListener taskListener)
    {
        mAuth.createUserWithEmailAndPassword(foodMaker.emailAddress, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    final FirebaseUser user = mAuth.getCurrentUser();
                    FoodMakerDao foodMakerDao = FoodMakerDao.GetInstance();
                    foodMaker.id = user.getUid();
                    foodMakerDao.save(foodMaker, foodMaker.id, new TaskListener() {
                        @Override
                        public void OnSuccess() {
                            final UserPrimitiveData userPrimitiveData = new UserPrimitiveData(user.getUid(), UserType.FoodMaker, new ArrayList<String>());
                            MessagingService.GetFirebaseAppToken(new RetrievalEventListener<String>() {
                                @Override
                                public void OnDataRetrieved(String token) {
                                    userPrimitiveData.userTokens.add(token);
                                    UserPrimitiveDataDao.GetInstance().save(userPrimitiveData, user.getUid(), new TaskListener() {
                                        @Override
                                        public void OnSuccess() {
                                            taskListener.OnSuccess();
                                        }

                                        @Override
                                        public void OnFail() {
                                            final TaskListener userPrimitiveDataTaskListener = this;
                                            FoodMakerDao.GetInstance().save(null, foodMaker.id, new TaskListener() {
                                                @Override
                                                public void OnSuccess() {
                                                    taskListener.OnFail();
                                                }

                                                @Override
                                                public void OnFail() {
                                                    userPrimitiveDataTaskListener.OnFail();
                                                }
                                            });

                                        }
                                    });
                                }
                            });

                        }

                        @Override
                        public void OnFail() {
                            taskListener.OnFail();
                        }
                    });
                } else
                    taskListener.OnFail();
            }
        });
    }
    public void SignUpFoodBuyer(final FoodBuyer foodBuyer, String password, final TaskListener taskListener)
    {
        mAuth.createUserWithEmailAndPassword(foodBuyer.emailAddress, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    final FirebaseUser user = mAuth.getCurrentUser();
                    FoodBuyerDao foodBuyerDao = FoodBuyerDao.GetInstance();
                    foodBuyer.id = user.getUid();
                    foodBuyerDao.save(foodBuyer, foodBuyer.id, new TaskListener() {
                        @Override
                        public void OnSuccess() {
                            final UserPrimitiveData userPrimitiveData = new UserPrimitiveData(user.getUid(), UserType.FoodBuyer, new ArrayList<String>());
                            MessagingService.GetFirebaseAppToken(new RetrievalEventListener<String>() {
                                @Override
                                public void OnDataRetrieved(String token) {
                                    userPrimitiveData.userTokens.add(token);
                                    UserPrimitiveDataDao.GetInstance().save(userPrimitiveData, user.getUid(), new TaskListener() {
                                        @Override
                                        public void OnSuccess() {
                                            taskListener.OnSuccess();
                                        }

                                        @Override
                                        public void OnFail() {
                                            final TaskListener userPrimitiveDataTaskListener = this;
                                            FoodBuyerDao.GetInstance().save(null, foodBuyer.id, new TaskListener() {
                                                @Override
                                                public void OnSuccess() {
                                                    taskListener.OnFail();
                                                }

                                                @Override
                                                public void OnFail() {
                                                    userPrimitiveDataTaskListener.OnFail();
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }

                        @Override
                        public void OnFail() {
                            taskListener.OnFail();
                        }
                    });
                } else
                    taskListener.OnFail();
            }
        });

    }
    public void SignUpDeliveryBoy(final DeliveryBoy deliveryBoy, String password, final TaskListener taskListener)
    {
        mAuth.createUserWithEmailAndPassword(deliveryBoy.emailAddress, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    final FirebaseUser user = mAuth.getCurrentUser();
                    DeliveryBoyDao deliveryBoyDao = DeliveryBoyDao.GetInstance();
                    deliveryBoy.id = user.getUid();
                    deliveryBoyDao.save(deliveryBoy, deliveryBoy.id, new TaskListener() {
                        @Override
                        public void OnSuccess() {
                            final UserPrimitiveData userPrimitiveData = new UserPrimitiveData(user.getUid(), UserType.DeliveryBoy, new ArrayList<String>());
                            MessagingService.GetFirebaseAppToken(new RetrievalEventListener<String>() {
                                @Override
                                public void OnDataRetrieved(String token) {
                                    userPrimitiveData.userTokens.add(token);
                                    UserPrimitiveDataDao.GetInstance().save(userPrimitiveData, user.getUid(), new TaskListener() {
                                        @Override
                                        public void OnSuccess() {
                                            taskListener.OnSuccess();
                                        }
                                        @Override
                                        public void OnFail(){
                                            final TaskListener userPrimitiveDataTaskListener = this;
                                            DeliveryBoyDao.GetInstance().save(null, deliveryBoy.id, new TaskListener() {
                                                @Override
                                                public void OnSuccess() {
                                                    taskListener.OnFail();
                                                }

                                                @Override
                                                public void OnFail() {
                                                    userPrimitiveDataTaskListener.OnFail();
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }

                        @Override
                        public void OnFail() {
                            taskListener.OnFail();
                        }
                    });
                } else
                    taskListener.OnFail();
            }
        });
    }
    public void Login(String email, String password, final RetrievalEventListener<Client> retrievalEventListener)
    {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, return logged in client
                    FirebaseUser user = mAuth.getCurrentUser();
                    buildUser(user, new RetrievalEventListener<Client>() {
                        @Override
                        public void OnDataRetrieved(final Client client) {
                            UserPrimitiveDataDao.GetInstance().get(client.id, new RetrievalEventListener<UserPrimitiveData>() {
                                @Override
                                public void OnDataRetrieved(final UserPrimitiveData userPrimitiveData) {
                                    MessagingService.GetFirebaseAppToken(new RetrievalEventListener<String>() {
                                        @Override
                                        public void OnDataRetrieved(String token) {
                                            UserPrimitiveDataDao.GetInstance().AddUserPrimitiveDataToken(userPrimitiveData, token, new TaskListener() {
                                                @Override
                                                public void OnSuccess() {
                                                    MessagingService.SendUserNotifications(client.id, new TaskListener() {
                                                        @Override
                                                        public void OnSuccess() {
                                                            retrievalEventListener.OnDataRetrieved(client);
                                                        }

                                                        @Override
                                                        public void OnFail() {
                                                            throw new RuntimeException("sad things happen");
                                                        }
                                                    });

                                                }

                                                @Override
                                                public void OnFail() {
                                                    throw new RuntimeException("sad things happen");
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                    });
                } else {
                    //sign in fails
                    retrievalEventListener.OnDataRetrieved(null);
                }
            }
        });

    }
    public void SignOut(final TaskListener taskListener)
    {
        UserPrimitiveDataDao.GetInstance().get(mAuth.getUid(), new RetrievalEventListener<UserPrimitiveData>() {
            @Override
            public void OnDataRetrieved(final UserPrimitiveData userPrimitiveData) {
                MessagingService.GetFirebaseAppToken(new RetrievalEventListener<String>() {
                    @Override
                    public void OnDataRetrieved(String token) {
                        for(String currentToken : userPrimitiveData.userTokens)
                            if(currentToken.equals(token))
                                userPrimitiveData.userTokens.remove(currentToken);
                        UserPrimitiveDataDao.GetInstance().save(userPrimitiveData, userPrimitiveData.id, new TaskListener() {
                            @Override
                            public void OnSuccess() {
                                mAuth.signOut();
                                taskListener.OnSuccess();
                            }

                            @Override
                            public void OnFail() {
                                taskListener.OnFail();
                            }
                        });
                    }
                });
            }
        });

    }
}
