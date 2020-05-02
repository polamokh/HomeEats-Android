package com.example.homeeats.Models;

import com.google.firebase.database.Exclude;

import java.util.Map;
import java.util.TreeMap;

public class UserNotification {
    @Exclude
    private String id;
    private String userId;
    private String title;
    private String body;
    private String priority;
    private String toToken;
    private Map<String, String> data;
    public UserNotification(){
        title = "";
        body = "";
        priority = "high";
        toToken = "";
        userId = "";
        id = "";
        data = new TreeMap<>();
    }
    public UserNotification setTitle(String title){
        this.title = title;
        return this;
    }
    public UserNotification setBody(String body){
        this.body = body;
        return this;
    }
    public UserNotification setPriority(String priority) {
        this.priority = priority;
        return this;
    }
    public UserNotification setToToken(String toToken) {
        this.toToken = toToken;
        return this;
    }
    public UserNotification setId(String id) {
        this.id = id;
        return this;
    }
    public UserNotification setUserId(String userId) {
        this.userId = userId;
        return this;
    }
    public UserNotification addData(String key, String val){
        data.put(key, val);
        return this;
    }
    public String getTitle(){
        return title;
    }
    public String getBody(){
        return body;
    }
    public Map<String, String> getData(){
        return data;
    }
    public String getPriority() {
        return priority;
    }
    public String getToToken() {
        return toToken;
    }
    public String getId() {
        return id;
    }
    public String getUserId() {
        return userId;
    }
    public UserNotification Clone(){
        UserNotification userNotification = new UserNotification();
        userNotification.title = String.copyValueOf(title.toCharArray());
        userNotification.id = String.copyValueOf(id.toCharArray());
        userNotification.userId = String.copyValueOf(userId.toCharArray());
        userNotification.body = String.copyValueOf(body.toCharArray());
        userNotification.priority = String.copyValueOf(priority.toCharArray());
        userNotification.toToken = String.copyValueOf(toToken.toCharArray());
        userNotification.data = data;
        return userNotification;
    }
}
