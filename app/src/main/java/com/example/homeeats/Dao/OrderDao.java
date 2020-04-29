package com.example.homeeats.Dao;

import com.example.homeeats.Models.DeliveryBoy;
import com.example.homeeats.Models.FoodBuyer;
import com.example.homeeats.Models.FoodMaker;
import com.example.homeeats.Models.Order;
import com.example.homeeats.Models.OrderStatus;
import com.example.homeeats.RetrievalEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;

public class OrderDao extends Dao<Order> {
    private static OrderDao orderDao;
    private OrderDao() {
        super("Orders");
    }
    public static OrderDao GetInstance()
    {
        if(orderDao == null)
            orderDao = new OrderDao();
        return orderDao;
    }
    @Override
    protected void parseDataSnapshot(final DataSnapshot dataSnapshot, final RetrievalEventListener<Order> retrievalEventListener) {
        final Order order = new Order();
        order.id = dataSnapshot.getKey().toString();
        order.review = dataSnapshot.child("review").getValue().toString();
        order.rating = Integer.parseInt(dataSnapshot.child("rating").getValue().toString());
        order.totalPrice = Double.parseDouble(dataSnapshot.child("totalPrice").getValue().toString());
        order.orderStatus = OrderStatus.getValue(dataSnapshot.child("orderStatus").getValue().toString());
        double latitude = Double.parseDouble(dataSnapshot.child("location").child("latitude").getValue().toString());
        double longitude = Double.parseDouble(dataSnapshot.child("location").child("longitude").getValue().toString());
        order.buyerLocation = new LatLng(latitude, longitude);
        FoodMakerDao.GetInstance().get(dataSnapshot.child("foodMakerId").getValue().toString(), new RetrievalEventListener<FoodMaker>() {
            @Override
            public void OnDataRetrieved(FoodMaker foodMaker) {
                order.foodMaker = foodMaker;
                FoodBuyerDao.GetInstance().get(dataSnapshot.child("foodBuyerId").getValue().toString(), new RetrievalEventListener<FoodBuyer>() {
                    @Override
                    public void OnDataRetrieved(FoodBuyer foodBuyer) {
                        order.foodBuyer = foodBuyer;
                        DeliveryBoyDao.GetInstance().get(dataSnapshot.child("deliveryBoyId").getValue().toString(), new RetrievalEventListener<DeliveryBoy>() {
                            @Override
                            public void OnDataRetrieved(DeliveryBoy deliveryBoy) {
                                order.deliveryBoy = deliveryBoy;
                                retrievalEventListener.OnDataRetrieved(order);
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    void delete(Order order) {

    }
}
