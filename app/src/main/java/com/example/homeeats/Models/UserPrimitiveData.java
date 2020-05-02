package com.example.homeeats.Models;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

public class UserPrimitiveData {
    @Exclude
    public String id;
    public UserType userType;
    public List<String> userTokens;

    public UserPrimitiveData(){
        id = "";
        userTokens = new ArrayList<>();
    }

    public UserPrimitiveData(String id, UserType userType, List<String> userTokens) {
        this.id = id;
        this.userType = userType;
        this.userTokens = userTokens;
    }
}
