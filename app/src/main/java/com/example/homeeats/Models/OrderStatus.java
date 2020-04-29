package com.example.homeeats.Models;

public enum OrderStatus {
    Making, Delivering, Delivered, Accepted, Rejected, invalid;
    public static OrderStatus getValue(String s)
    {
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
            default:
                return invalid;
        }
    }
}
