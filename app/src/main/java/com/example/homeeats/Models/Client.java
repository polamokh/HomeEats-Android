package com.example.homeeats.Models;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public abstract class Client {
    @Exclude
    public String id;
    public String name;
    public String gender;
    public String emailAddress;
    public String phone;
    public String photo;

    public Client(String id, String name, String gender, String emailAddress, String phone, String photo) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.emailAddress = emailAddress;
        this.phone = phone;
        this.photo = photo;
    }

    public Client(){}
}
