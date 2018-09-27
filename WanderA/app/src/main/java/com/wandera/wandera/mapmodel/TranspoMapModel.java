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
    public String seats;
    public String vanImg;
    public String modeoftranspo;
    public String operator;

    public TranspoMapModel(){

    }
    public TranspoMapModel(String driverName,
                           String key,
                           String origin,
                           String contactNumber,
                           String price,
                           String seats, String vanImg,String modeoftranspo,String operator){
        this.name = driverName;
        this.key = key;
        this.origin = origin;
        this.number = contactNumber;
        this.price = price;
        this.seats = seats;
        this.vanImg = vanImg;
        this.modeoftranspo = modeoftranspo;
        this.operator = operator;
    }
    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("name",name);
        result.put("key",key);
        result.put("origin",origin);
        result.put("price",price);
        result.put("seats", seats);
        result.put("number",number);
        result.put("vanImg",vanImg);
        result.put("modeoftranspo",modeoftranspo);
        result.put("operator",operator);

        return result;
    }
}
