package com.wandera.wandera;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Keji's Lab on 26/11/2017.
 */

public class ChatMessageMapModel {
    public String userId;
    public String userName;
    public String timeStamp;
    public String message;
    public String businessId;
    public String userImage;
    public String senderId;


    public ChatMessageMapModel(){

    }
    public ChatMessageMapModel(String userId,
                               String userName,
                               String timeStamp,
                               String message,
                               String businessId,
                               String userImage,
                               String senderId){
        this.userId=userId;
        this.userName=userName;
        this.timeStamp=timeStamp;
        this.message=message;
        this.businessId=businessId;
        this.userImage=userImage;
        this.senderId=senderId;
    }
    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("userId",userId);
        result.put("categoryName", userName);
        result.put("timeStamp", timeStamp);
        result.put("message", message);
        result.put("businessKey", businessId);
        result.put("senderId", senderId);

        return result;
    }

}
