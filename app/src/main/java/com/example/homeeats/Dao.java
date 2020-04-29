package com.example.homeeats;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Method;
import java.util.List;

public abstract class Dao<T> {
    protected static final DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference();
    public abstract void get(AppCompatActivity appCompatActivity, String id, Method callback) ;
    public abstract List<T> getAll() throws Exception;
    abstract void save(T t);
    abstract void delete(T t);
}
