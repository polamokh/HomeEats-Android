package com.example.homeeats.Dao;

import com.example.homeeats.Models.UserPrimitiveData;
import com.example.homeeats.Models.UserType;
import com.example.homeeats.RetrievalEventListener;
import com.example.homeeats.TaskListener;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class UserPrimitiveDataDao extends Dao<UserPrimitiveData> {
    private static UserPrimitiveDataDao singletonObject;

    public static UserPrimitiveDataDao GetInstance(){
        if(singletonObject == null)
            singletonObject = new UserPrimitiveDataDao();
        return singletonObject;
    }

    private UserPrimitiveDataDao() {
        super("users");
    }

    @Override
    protected void parseDataSnapshot(DataSnapshot dataSnapshot, RetrievalEventListener retrievalEventListener) {
        UserPrimitiveData userPrimitiveData = new UserPrimitiveData();
        userPrimitiveData.id = dataSnapshot.getKey();
        userPrimitiveData.userType = UserType.getValue(dataSnapshot.child("userType").getValue().toString());
        for(DataSnapshot currentSnapshot : dataSnapshot.child("userTokens").getChildren())
            userPrimitiveData.userTokens.add(currentSnapshot.getValue().toString());
        retrievalEventListener.OnDataRetrieved(userPrimitiveData);
    }

    public void AddUserPrimitiveDataToken(UserPrimitiveData userPrimitiveData, String token, final TaskListener taskListener){
        userPrimitiveData.userTokens.add(token);
        save(userPrimitiveData, userPrimitiveData.id, new TaskListener() {
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
}
