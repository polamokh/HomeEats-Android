package com.example.homeeats.Models;

import com.google.firebase.database.Exclude;

public class UserPrimitiveData {
    @Exclude
    public String id;
    public UserType userType;

    public UserPrimitiveData(){}

    public UserPrimitiveData(String id, UserType userType) {
        this.id = id;
        this.userType = userType;
    }
}
