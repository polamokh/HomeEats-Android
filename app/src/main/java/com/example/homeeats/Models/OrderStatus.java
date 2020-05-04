package com.example.homeeats.Models;

public enum OrderStatus {

    Pending, Accepted, Rejected, Making, ReadyForDelivery, Delivering, Delivered, Invalid;

    public static OrderStatus getValue(String s) {
        switch (s)
        {
            case "Making":
                return Making;
            case "Delivering":
                return Delivering;
            case "Delivered":
                return Delivered;
            case "Accepted":
                return Accepted;
            case "Rejected":
                return Rejected;
            case "Pending":
                return Pending;
            case "ReadyForDelivery":
                return ReadyForDelivery;
            default:
                return Invalid;
        }
    }
}
