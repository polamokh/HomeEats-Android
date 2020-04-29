package com.example.homeeats;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Timestamp;
import java.util.Date;

public class DeliveryBoy extends Client {
    public boolean availability;
    public LatLng location;

    public DeliveryBoy(String id, String name, String gender, String emailAddress, String phone, boolean availability, LatLng location, long lastSeen) {
        super(id, name, gender, emailAddress, phone);
        this.availability = availability;
        this.location = location;
        this.lastSeen = lastSeen;
    }

    public long lastSeen;

    public DeliveryBoy(){super();}

    public Timestamp GetLastSeenTimeStamp()
    {
        return new Timestamp(lastSeen);
    }
}
