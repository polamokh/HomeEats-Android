package com.example.homeeats;

import com.google.firebase.database.Exclude;

public abstract class Client {
    @Exclude
    public String id;
    public String name;
    public String gender;
    public String emailAddress;
    public String phone;

    public Client(String id, String name, String gender, String emailAddress, String phone) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.emailAddress = emailAddress;
        this.phone = phone;
    }
}
