package com.example.homeeats.Models;

import com.example.homeeats.R;

public enum UserType  {
    FoodMaker, FoodBuyer, DeliverBoy, Invalid;

    public static UserType getValue(String s) {
        switch (s)
        {
            case "FoodMaker":
                return FoodMaker;
            case "FoodBuyer":
                return FoodBuyer;
            case "DeliveryBoy":
                return DeliverBoy;
            default:
                return Invalid;
        }
    }
}
