package com.example.homeeats.Dao;

import android.graphics.Bitmap;
import android.util.Pair;

import androidx.annotation.NonNull;

import com.example.firbasedao.FirebaseDao;
import com.example.firbasedao.Listeners.RetrievalEventListener;
import com.example.firbasedao.Listeners.TaskListener;
import com.example.homeeats.FilesStorageDatabase;
import com.example.homeeats.Models.DeliveryBoy;
import com.example.homeeats.Models.FoodBuyer;
import com.example.homeeats.Models.Order;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeliveryBoyDao extends FirebaseDao<DeliveryBoy> {
    private static DeliveryBoyDao singletonObject;
    private Map<String, DatabaseReference> deliveryBoyIdRowReferences;
    private Map<String, List<Pair<RetrievalEventListener<DeliveryBoy>, ValueEventListener>>>  deliveryBorIdRetrievalEventListeners;
    private DeliveryBoyDao() {
        super("DeliveryBoys");
        deliveryBoyIdRowReferences = new HashMap<>();
        deliveryBorIdRetrievalEventListeners = new HashMap<>();
    }

    public static DeliveryBoyDao GetInstance()
    {
        if(singletonObject == null)
            singletonObject = new DeliveryBoyDao();
        return singletonObject;
    }

    public void save(final DeliveryBoy deliveryBoy, final String id, Bitmap bitmap, final TaskListener taskListener){
        if(bitmap == null){
            save(deliveryBoy, id, taskListener);
            return;
        }
        setDeliveryBoyImage(id, bitmap, new RetrievalEventListener<String>() {
            @Override
            public void OnDataRetrieved(String s) {
                deliveryBoy.photo = s;
                save(deliveryBoy, id, taskListener);
            }
        });
    }

    public void setDeliveryBoyImage(final String deliveryBoyId, Bitmap image, final RetrievalEventListener<String> retrievalEventListener){
        FilesStorageDatabase.GetInstance().uploadPhoto(image, String.format("delivery boys images/%s.jpg", deliveryBoyId),
                new RetrievalEventListener<String>() {
                    @Override
                    public void OnDataRetrieved(String uploadPath) {
                        dbReference.child(tableName).child(deliveryBoyId).child("photo").setValue(uploadPath);
                        retrievalEventListener.OnDataRetrieved(uploadPath);
                    }
                });
    }

    @Override
    protected void parseDataSnapshot(DataSnapshot dataSnapshot, RetrievalEventListener<DeliveryBoy> retrievalEventListener) {
        final DeliveryBoy deliveryBoy = new DeliveryBoy();
        deliveryBoy.id = dataSnapshot.getKey();
        deliveryBoy.name = dataSnapshot.child("name").getValue().toString();
        deliveryBoy.gender = dataSnapshot.child("gender").getValue().toString();
        deliveryBoy.emailAddress = dataSnapshot.child("emailAddress").getValue().toString();
        deliveryBoy.phone = dataSnapshot.child("phone").getValue().toString();
        deliveryBoy.photo = dataSnapshot.child("photo").getValue().toString();
        double latitude = Double.parseDouble(dataSnapshot.child("location").child("latitude").getValue().toString());
        double longitude = Double.parseDouble(dataSnapshot.child("location").child("longitude").getValue().toString());
        deliveryBoy.location = new LatLng(latitude, longitude);
        deliveryBoy.lastSeen = Long.parseLong(dataSnapshot.child("lastSeen").getValue().toString());
        deliveryBoy.availability = Boolean.parseBoolean(dataSnapshot.child("availability").getValue().toString());
        retrievalEventListener.OnDataRetrieved(deliveryBoy);
    }

    public void GetDeliveryBoyOrder(final String deliveryBoyId, final RetrievalEventListener<List<Order>> listRetrievalEventListener)
    {
        final List<Order> deliveryBoyOrders = new ArrayList<>();
        OrderDao.GetInstance().getAll(new RetrievalEventListener<List<Order>>() {
            @Override
            public void OnDataRetrieved(List<Order> orders) {
                for(Order order : orders)
                    if(order.deliveryBoyId.equals(deliveryBoyId))
                        deliveryBoyOrders.add(order);
                listRetrievalEventListener.OnDataRetrieved(deliveryBoyOrders);
            }
        });
    }

    public void AddDeliveryBoyLiveLocationListener(final String deliveryBoyId, final RetrievalEventListener<DeliveryBoy> retrievalEventListener){
        if(!deliveryBoyIdRowReferences.containsKey(deliveryBoyId)) {
            deliveryBoyIdRowReferences.put(deliveryBoyId, dbReference.child(tableName).child(deliveryBoyId));
            deliveryBorIdRetrievalEventListeners.put(deliveryBoyId, new ArrayList<Pair<RetrievalEventListener<DeliveryBoy>, ValueEventListener>>());
        }
        DatabaseReference rowReference = deliveryBoyIdRowReferences.get(deliveryBoyId);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                parseDataSnapshot(dataSnapshot, new RetrievalEventListener<DeliveryBoy>() {
                    @Override
                    public void OnDataRetrieved(DeliveryBoy deliveryBoy) {
                        retrievalEventListener.OnDataRetrieved(deliveryBoy);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        rowReference.addValueEventListener(valueEventListener);
        deliveryBorIdRetrievalEventListeners.get(deliveryBoyId).add(new Pair<>(retrievalEventListener, valueEventListener));
    }

    public void RemoveDeliveryBoyLiveLocationListener(String deliveryBoyId, RetrievalEventListener<DeliveryBoy> retrievalEventListener){
        if(deliveryBoyIdRowReferences.containsKey(deliveryBoyId) == false)
            return;
        DatabaseReference rowReference = deliveryBoyIdRowReferences.get(deliveryBoyId);
        List<Pair<RetrievalEventListener<DeliveryBoy>, ValueEventListener>> eventListeners = deliveryBorIdRetrievalEventListeners.get(deliveryBoyId);
        for(int i = 0; i < eventListeners.size(); i++){
            Pair<RetrievalEventListener<DeliveryBoy>, ValueEventListener> p = eventListeners.get(i);
            if(p.first == retrievalEventListener) {
                rowReference.removeEventListener(p.second);
                eventListeners.remove(i);
                i--;
            }
        }
        if(deliveryBorIdRetrievalEventListeners.get(deliveryBoyId).size() == 0){
            deliveryBorIdRetrievalEventListeners.remove(deliveryBoyId);
            deliveryBoyIdRowReferences.remove(deliveryBoyId);
        }
    }



    // Gets the first available delivery boy, if there is no available -> null.
    public void GetAvailableDeliveryBoy(final RetrievalEventListener<DeliveryBoy> deliveryBoyRetrievalEventListener){
        getAll(new RetrievalEventListener<List<DeliveryBoy>>() {
            @Override
            public void OnDataRetrieved(List<DeliveryBoy> deliveryBoys) {
                for(DeliveryBoy deliveryBoy: deliveryBoys){
                    if(deliveryBoy.availability == true ){
                        deliveryBoyRetrievalEventListener.OnDataRetrieved(deliveryBoy);
                        return;
                    }
                }
                deliveryBoyRetrievalEventListener.OnDataRetrieved(null);
            }
        });
    }
}
