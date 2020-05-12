package com.example.homeeats.Helpers;

import com.example.firbasedao.Listeners.RetrievalEventListener;
import com.example.firbasedao.Listeners.TaskListener;
import com.example.gmailsender.GmailSender;
import com.example.homeeats.Dao.DeliveryBoyDao;
import com.example.homeeats.Dao.FoodBuyerDao;
import com.example.homeeats.Dao.FoodMakerDao;
import com.example.homeeats.Dao.UserPrimitiveDataDao;
import com.example.homeeats.Models.DeliveryBoy;
import com.example.homeeats.Models.FoodBuyer;
import com.example.homeeats.Models.FoodMaker;
import com.example.homeeats.Models.UserPrimitiveData;
import com.example.homeeats.Models.UserType;

public class EmailHelper {
    public static void SendUserEmail(final String id, final String title, final String body){
        UserPrimitiveDataDao.GetInstance().get(id, new RetrievalEventListener<UserPrimitiveData>() {
            @Override
            public void OnDataRetrieved(UserPrimitiveData userPrimitiveData) {
                //save the userNotification to database
                if(userPrimitiveData.userType == UserType.DeliveryBoy){
                    DeliveryBoyDao.GetInstance().get(id, new RetrievalEventListener<DeliveryBoy>() {
                        @Override
                        public void OnDataRetrieved(DeliveryBoy deliveryBoy) {
                            GmailSender.sendEmail(deliveryBoy.emailAddress, title, body);
                        }
                    });
                }
                else if(userPrimitiveData.userType == UserType.FoodBuyer){
                    FoodBuyerDao.GetInstance().get(id, new RetrievalEventListener<FoodBuyer>() {
                        @Override
                        public void OnDataRetrieved(FoodBuyer foodBuyer) {
                            GmailSender.sendEmail(foodBuyer.emailAddress, title, body);
                        }
                    });
                }
                else if(userPrimitiveData.userType == UserType.FoodMaker){
                    FoodMakerDao.GetInstance().get(id, new RetrievalEventListener<FoodMaker>() {
                        @Override
                        public void OnDataRetrieved(FoodMaker foodMaker) {
                            GmailSender.sendEmail(foodMaker.emailAddress, title, body);
                        }
                    });
                }
            }
        });
    }
}
