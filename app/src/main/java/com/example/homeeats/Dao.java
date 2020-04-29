package com.example.homeeats;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
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
                retrievalEventListener.OnDataRetrieved(parseDataSnapshot(dataSnapshot));
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

    public abstract T parseDataSnapshot(DataSnapshot dataSnapshot);

    public void getAll(final RetrievalEventListener<List<T>> retrievalEventListener){
        DatabaseReference rowReference = dbReference.child(tableName);
        rowReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<T> list = new ArrayList<T>();
                for(DataSnapshot currentSnapshot : dataSnapshot.getChildren())
                {
                    list.add(parseDataSnapshot(currentSnapshot));
                }
                retrievalEventListener.OnDataRetrieved(list);
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
