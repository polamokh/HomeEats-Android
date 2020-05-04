package com.example.homeeats;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.homeeats.Dao.DeliveryBoyDao;
import com.example.homeeats.Dao.UserPrimitiveDataDao;
import com.example.homeeats.Listeners.RetrievalEventListener;
import com.example.homeeats.Listeners.TaskListener;
import com.example.homeeats.Models.Client;
import com.example.homeeats.Models.DeliveryBoy;
import com.example.homeeats.Models.UserPrimitiveData;
import com.example.homeeats.Models.UserType;
import com.google.android.gms.maps.model.LatLng;

public class LiveLocationService extends Service {
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("Service", "Created");
        LocationManager.GetInstance(getApplicationContext()).setOnApplicationUpdatedListener(new RetrievalEventListener<Location>() {
            @Override
            public void OnDataRetrieved(final Location location) {
                if(location == null)
                    return;
                UserAuthenticationDatabase.GetInstance().GetActiveClient(new RetrievalEventListener<Client>() {
                    @Override
                    public void OnDataRetrieved(final Client client) {
                        if(client == null)
                            return;
                        UserPrimitiveDataDao.GetInstance().get(client.id, new RetrievalEventListener<UserPrimitiveData>() {
                            @Override
                            public void OnDataRetrieved(UserPrimitiveData userPrimitiveData) {
                                if(userPrimitiveData.userType != UserType.DeliveryBoy)
                                    return;
                                DeliveryBoy deliveryBoy = (DeliveryBoy) client;
                                deliveryBoy.location = new LatLng(location.getLatitude(), location.getLongitude());
                                deliveryBoy.lastSeen = location.getTime();
                                DeliveryBoyDao.GetInstance().save(deliveryBoy, deliveryBoy.id, new TaskListener() {
                                    @Override
                                    public void OnSuccess() {
                                        Log.e("Live Location", "" + location);
                                    }

                                    @Override
                                    public void OnFail() {
                                        Log.e("Live Location", "Failed");
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
        createNotificationChannel();
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID).build();
        startForeground(1, notification);
        //do heavy work on a background thread
        //stopSelf();
        return START_NOT_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    @Override
    public void onDestroy() {
        Log.e("Service", "Stopping");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
