package com.example.homeeats.Dao;

import com.example.homeeats.Models.DeliveryBoy;
import com.example.homeeats.RetrievalEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;

public class DeliveryBoyDao extends Dao<DeliveryBoy> {
    private static DeliveryBoyDao singletonObject;
    private DeliveryBoyDao() {
        super("DeliveryBoys");
    }
    public static DeliveryBoyDao GetInstance()
    {
        if(singletonObject == null)
            singletonObject = new DeliveryBoyDao();
        return singletonObject;
    }

    @Override
    protected void parseDataSnapshot(DataSnapshot dataSnapshot, RetrievalEventListener<DeliveryBoy> retrievalEventListener) {
        final DeliveryBoy deliveryBoy = new DeliveryBoy();
        deliveryBoy.id = dataSnapshot.getKey();
        deliveryBoy.name = dataSnapshot.child("name").getValue().toString();
        deliveryBoy.gender = dataSnapshot.child("gender").getValue().toString();
        deliveryBoy.emailAddress = dataSnapshot.child("emailAddress").getValue().toString();
        deliveryBoy.phone = dataSnapshot.child("phone").getValue().toString();
        double latitude = Double.parseDouble(dataSnapshot.child("location").child("latitude").getValue().toString());
        double longitude = Double.parseDouble(dataSnapshot.child("location").child("longitude").getValue().toString());
        deliveryBoy.location = new LatLng(latitude, longitude);
        deliveryBoy.lastSeen = Long.parseLong(dataSnapshot.child("lastSeen").getValue().toString());
        retrievalEventListener.OnDataRetrieved(deliveryBoy);
    }

    @Override
    void delete(DeliveryBoy deliveryBoy) {

    }
}
