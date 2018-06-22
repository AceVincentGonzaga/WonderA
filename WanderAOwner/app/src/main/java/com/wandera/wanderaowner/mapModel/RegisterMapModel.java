package com.wandera.wanderaowner.mapModel;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Keji's Lab on 26/11/2017.
 */

public class RegisterMapModel {
    public String userId;
    public String name;
    public String address;
    public String contact;

    public RegisterMapModel(){

    }
    public RegisterMapModel(String userId, String name,String address,String contact){
        this.userId = userId;
        this.name = name;
        this.address = address;
        this.contact = contact;
    }
    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("userId",userId);
        result.put("name",name);
        result.put("address",address);
        result.put("contact",contact);

        return result;
    }
}
