package com.wandera.wanderaowner.mapModel;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Keji's Lab on 26/11/2017.
 */

public class ChatListMapModel {
    public String key;
    public String userId;
    public String businessId;



    public ChatListMapModel(){

    }
    public ChatListMapModel(String userId,
                            String key,
                            String businessId)
                          {
        this.userId=userId;
        this.key=key;
        this.businessId=businessId;

    }
    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("userId",userId);
       result.put("key", key);
        result.put("businessKey", businessId);

        return result;
    }

}
