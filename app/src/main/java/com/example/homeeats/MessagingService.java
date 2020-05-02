package com.example.homeeats;


import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.homeeats.Dao.UserNotificationDao;
import com.example.homeeats.Dao.UserPrimitiveDataDao;
import com.example.homeeats.Models.UserNotification;
import com.example.homeeats.Models.UserPrimitiveData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;

public class MessagingService extends FirebaseMessagingService {
    static int notificationID = 1;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static void sendNotification(final UserNotification userNotificationBuilder) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    URL url = new URL("https://fcm.googleapis.com/fcm/send");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setUseCaches(false);
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Authorization", "key=AAAAEtgKyyU:APA91bH8K7cEU71GGba99wxIBrZBFnZSZaV9Xe2vwwZgGG1rOwmF53W6LWnKhcLFfu7dU27jfXI9ZQGxS8Su04YuLu9v_TGQvlcs0v97Sk50pnQh7EukH4Qddo5k8uTU9WrQahtkLdJ0");
                    conn.setRequestProperty("Content-Type", "application/json");

                    JSONObject json = new JSONObject();

                    json.put("to", userNotificationBuilder.getToToken());

                    JSONObject info = new JSONObject();
                    info.put("title", userNotificationBuilder.getTitle());   // Notification title
                    info.put("body", userNotificationBuilder.getBody()); // Notification body
                    json.put("notification", info);

                    JSONObject data = new JSONObject(userNotificationBuilder.getData());
                    json.put("data", data);

                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(json.toString());
                    wr.flush();
                    conn.getInputStream();
                    Log.e("kunwar", "sent");
                } catch (Exception e) {

                    Log.e("kunwar",e.getMessage());
                }
                return null;
            }
        }.execute();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e("Service", "received");
        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "some_channel_id")
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        for(String key : remoteMessage.getData().keySet())
            Log.e(key, remoteMessage.getData().get(key));
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationID++, builder.build());

    }

    public static void GetFirebaseAppToken(final RetrievalEventListener<String> retrievalEventListener)
    {
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                retrievalEventListener.OnDataRetrieved(task.getResult().getToken());
            }
        });
    }

    public static void SendUserNotifications(String userId, final TaskListener taskListener){
        UserNotificationDao.GetInstance().GetUserNotificationByUserId(userId, new RetrievalEventListener<List<UserNotification>>() {
            @Override
            public void OnDataRetrieved(final List<UserNotification> userNotifications) {
                if(userNotifications != null)
                {
                    final EventListenersListener eventListenersListener = new EventListenersListener() {
                        @Override
                        public void onFinish() {
                            taskListener.OnSuccess();
                            //delete notifications
                            for(UserNotification userNotification : userNotifications)
                                UserNotificationDao.GetInstance().save(null, userNotification.getId(), new TaskListener() {
                                    @Override
                                    public void OnSuccess() {

                                    }

                                    @Override
                                    public void OnFail() {

                                    }
                                });
                        }
                    };
                    List<TaskListener> taskListeners = new ArrayList<>();
                    for(UserNotification userNotification : userNotifications){
                        TaskListener taskListener = new TaskListener() {
                            @Override
                            public void OnSuccess() {
                                eventListenersListener.notify(this);
                            }

                            @Override
                            public void OnFail() {
                                eventListenersListener.notify(this);
                            }
                        };
                        taskListeners.add(taskListener);
                    }
                    eventListenersListener.OnFinishAddingListeners();
                    for(int i = 0; i < userNotifications.size(); i++)
                        SendUserNotification(userNotifications.get(i), taskListeners.get(i));

                }
            }
        });
    }

    public static void SendUserNotification(final UserNotification userNotification, final TaskListener taskListener){
        UserPrimitiveDataDao.GetInstance().get(userNotification.getUserId(), new RetrievalEventListener<UserPrimitiveData>() {
            @Override
            public void OnDataRetrieved(UserPrimitiveData userPrimitiveData) {
                //save the userNotification to database
                if(userPrimitiveData.userTokens.size() == 0)
                {
                    UserNotificationDao.GetInstance().save(userNotification, UserNotificationDao.GetInstance().GetNewKey(), new TaskListener() {
                        @Override
                        public void OnSuccess() {
                            taskListener.OnSuccess();
                        }

                        @Override
                        public void OnFail() {
                            taskListener.OnFail();
                        }
                    });
                }
                //push notification to current tokens
                else
                {
                    for(String token : userPrimitiveData.userTokens){
                        UserNotification userNotificationClone = userNotification.Clone();
                        userNotificationClone.setToToken(token);
                        sendNotification(userNotificationClone);
                    }
                    taskListener.OnSuccess();
                }
            }
        });
    }
}
