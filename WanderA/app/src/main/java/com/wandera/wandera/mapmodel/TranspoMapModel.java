package com.wandera.wandera.mapmodel;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class TranspoMapModel {
    public String name;
    public String key;
    public String origin;
    public String number;
    public String price;
    public String capacity;


    public TranspoMapModel(){

    }
    public TranspoMapModel(String driverName,
                           String key,
                           String origin,
                           String contactNumber,
                           String price,
                           String capacity){
        this.name = driverName;
        this.key = key;
        this.origin = origin;
        this.number = contactNumber;
        this.price = price;
        this.capacity = capacity;
    }
    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("name",name);
        result.put("key",key);
        result.put("origin",origin);
        result.put("price",price);
        result.put("capacity",capacity);
        result.put("number",number);

        return result;
    }
}
