package com.example.homeeats;

import com.google.android.gms.maps.model.LatLng;

public class DeliveryBoy extends Client {
    public boolean availability;

    public DeliveryBoy(String id, String name, String gender, String emailAddress, String phone, boolean availability) {
        super(id, name, gender, emailAddress, phone);
        this.availability = availability;
    }

    //todo : get from database
    public LatLng GetLiveLocation()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
