package com.example.homeeats.Dao;

import android.app.Notification;

import com.example.homeeats.Models.UserNotification;
import com.example.homeeats.Models.UserPrimitiveData;
import com.example.homeeats.RetrievalEventListener;
import com.example.homeeats.TaskListener;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserNotificationDao extends Dao<UserNotification> {
    private static UserNotificationDao singletonObject;
    private UserNotificationDao() {
        super("userNotifications");
    }

    public static UserNotificationDao GetInstance(){
        if(singletonObject == null)
            singletonObject = new UserNotificationDao();
        return singletonObject;
    }

    @Override
    protected void parseDataSnapshot(DataSnapshot dataSnapshot, RetrievalEventListener<UserNotification> retrievalEventListener) {
        final UserNotification userNotification = new UserNotification();
        userNotification
                .setId(dataSnapshot.getKey())
                .setTitle(dataSnapshot.child("title").getValue().toString())
                .setBody(dataSnapshot.child("body").getValue().toString())
                .setPriority(dataSnapshot.child("priority").getValue().toString())
                .setUserId(dataSnapshot.child("userId").getValue().toString());
        if(dataSnapshot.hasChild("data"))
            for(DataSnapshot currentSnapshot : dataSnapshot.child("data").getChildren())
                userNotification.addData(currentSnapshot.getKey(), currentSnapshot.getValue().toString());
        retrievalEventListener.OnDataRetrieved(userNotification);
    }

    public void GetUserNotificationByUserId(final String userId, final RetrievalEventListener<List<UserNotification>> retrievalEventListener){
        final List<UserNotification> allNotifications = new ArrayList<>();
        getAll(new RetrievalEventListener<List<UserNotification>>() {
            @Override
            public void OnDataRetrieved(List<UserNotification> userNotifications) {
                for(UserNotification userNotification : userNotifications)
                    if(userNotification.getUserId().equals(userId))
                        allNotifications.add(userNotification);
                retrievalEventListener.OnDataRetrieved(allNotifications);
            }
        });
    }
}
