package com.example.homeeats.Dao;

import androidx.annotation.NonNull;

import com.example.homeeats.RetrievalEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public abstract class Dao<T> {
    protected static final DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference();
    protected String tableName;
    public Dao(String tableName)
    {
        this.tableName = tableName;
    }

    public void get(String id, final RetrievalEventListener<T> retrievalEventListener) {
        DatabaseReference rowReference = dbReference.child(tableName).child(id);
        rowReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                parseDataSnapshot(dataSnapshot, new RetrievalEventListener<T>() {
                    @Override
                    public void OnDataRetrieved(T t) {
                        retrievalEventListener.OnDataRetrieved(t);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public String GetNewKey()
    {
        return dbReference.child(tableName).push().getKey();
    }

    protected abstract void parseDataSnapshot(DataSnapshot dataSnapshot, RetrievalEventListener<T> retrievalEventListener);

    public void getAll(final RetrievalEventListener<List<T>> retrievalEventListener){
        DatabaseReference rowReference = dbReference.child(tableName);
        rowReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<T> list = new ArrayList<T>();
                final long len = dataSnapshot.getChildrenCount();
                RetrievalEventListener<T> listRetrievalEventListener = new RetrievalEventListener<T>() {
                    @Override
                    public void OnDataRetrieved(T t) {
                        list.add(t);
                        if(list.size() == len){
                            retrievalEventListener.OnDataRetrieved(list);
                        }
                    }
                };
                for(DataSnapshot currentDataSnapshot : dataSnapshot.getChildren())
                    parseDataSnapshot(dataSnapshot, listRetrievalEventListener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void save(T t, String id){
        dbReference.child(tableName).child(id).setValue(t);
    }
    abstract void delete(T t);
}