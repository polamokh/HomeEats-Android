package com.example.homeeats;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public abstract class Dao<T> {
    protected static final DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference();
    public abstract void get(String id, RetrievalEventListener<T> retrievalEventListener) ;
    public abstract void getAll(RetrievalEventListener<List<T>> retrievalEventListener);
    abstract void save(T t);
    abstract void delete(T t);
}
