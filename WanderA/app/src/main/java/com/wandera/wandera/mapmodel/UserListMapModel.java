package com.wandera.wandera.mapmodel;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class UserListMapModel {
    public String userId;
    public String key;
    public String businessKey;
    public boolean seen_owner;
    public boolean seen_user;

    public UserListMapModel(){

    }
    public UserListMapModel(String userId,
                            String key,
                            String businessKey){
        this.userId=userId;
        this.key=key;
        this.businessKey=businessKey;
    }
    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("key",key);
        result.put("businessKey", businessKey);
        result.put("seen_owner",false);
        result.put("seen_user",true);

        return result;
    }
}
