package com.example.homeeats.Models;

public enum UserType  {
    FoodMaker, FoodBuyer, DeliveryBoy, Invalid;

    public static UserType getValue(String s) {
        switch (s)
        {
            case "FoodMaker":
                return FoodMaker;
            case "FoodBuyer":
                return FoodBuyer;
            case "DeliveryBoy":
                return DeliveryBoy;
            default:
                return Invalid;
        }
    }
}
