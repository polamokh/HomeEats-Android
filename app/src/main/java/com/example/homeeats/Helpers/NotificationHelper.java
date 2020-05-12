package com.example.homeeats.Helpers;

import android.util.Log;

import com.example.firbasedao.Listeners.TaskListener;
import com.example.homeeats.MessagingService;
import com.example.homeeats.Models.UserNotification;

public class NotificationHelper {
    public static void sendUserNotification(final String userId, String title, String body){
        UserNotification userNotification = new UserNotification();
        userNotification.setUserId(userId);
        userNotification.setTitle(title);
        userNotification.setBody(body);
        MessagingService.SendUserNotification(userNotification, new TaskListener() {
            @Override
            public void OnSuccess() {
                Log.e("Notification", "sent to " + userId);
            }

            @Override
            public void OnFail() {
                Log.e("Notification", "failed to send to" + userId);
            }
        });
    }
}
