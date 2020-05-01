package com.example.homeeats.Dao;

import com.example.homeeats.Models.UserPrimitiveData;
import com.example.homeeats.Models.UserType;
import com.example.homeeats.RetrievalEventListener;
import com.google.firebase.database.DataSnapshot;

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
        retrievalEventListener.OnDataRetrieved(userPrimitiveData);
    }
}
