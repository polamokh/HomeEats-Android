package com.example.homeeats.Dao;

import com.example.homeeats.Models.Order;
import com.example.homeeats.Models.OrderItem;
import com.example.homeeats.Models.OrderStatus;
import com.example.homeeats.Listeners.RetrievalEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

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
        double latitude = Double.parseDouble(dataSnapshot.child("buyerLocation").child("latitude").getValue().toString());
        double longitude = Double.parseDouble(dataSnapshot.child("buyerLocation").child("longitude").getValue().toString());
        order.buyerLocation = new LatLng(latitude, longitude);
        order.orderItems = new ArrayList<OrderItem>();
        order.foodMakerId = dataSnapshot.child("foodMakerId").getValue().toString();
        order.foodBuyerId = dataSnapshot.child("foodBuyerId").getValue().toString();
        order.deliveryBoyId = dataSnapshot.child("deliveryBoyId").getValue().toString();

        order.orderItems = new ArrayList<>();
        for(DataSnapshot currentSnapshot : dataSnapshot.child("orderItems").getChildren())
            order.orderItems.add(parseOrderItemDataSnapShot(currentSnapshot));
    }

    protected OrderItem parseOrderItemDataSnapShot(DataSnapshot dataSnapshot)
    {
        final OrderItem orderItem = new OrderItem();
        orderItem.quantity = Integer.parseInt(dataSnapshot.child("quantity").getValue().toString());
        orderItem.notes = dataSnapshot.child("notes").getValue().toString();
        orderItem.rating = Integer.parseInt(dataSnapshot.child("rating").getValue().toString());
        orderItem.mealItemId = dataSnapshot.child("mealItemId").getValue().toString();
        orderItem.totalPrice = Double.parseDouble(dataSnapshot.child("totalPrice").getValue().toString());
        return orderItem;
    }
}
