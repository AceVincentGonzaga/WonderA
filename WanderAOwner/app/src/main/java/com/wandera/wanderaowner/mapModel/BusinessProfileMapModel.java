package com.wandera.wanderaowner.mapModel;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Keji's Lab on 26/11/2017.
 */

public class BusinessProfileMapModel {
    public String userId;
    public String name;
    public String address;
    public String contact;
    public String email;
    public String businessType;
    public String restoProfileImagePath;
    public boolean businessApproval;
    public String key;
    public String municipality;
    public String barangay;


    public BusinessProfileMapModel(){

    }
    public BusinessProfileMapModel(String userId, String name, String address,
                                   String contact, String email, String businessType,
                                   String restoProfileImagePath,String key,String municipality,String barangay){
        this.userId = userId;
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.email= email;
        this.businessType = businessType;
        this.restoProfileImagePath = restoProfileImagePath;
        this.key = key;
        this.municipality = municipality;
        this.barangay = barangay;
    }
    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("userId",userId);
        result.put("name",name);
        result.put("address",address);
        result.put("contact",contact);
        result.put("email",email);
        result.put("municipality",municipality);
        result.put("businessType",businessType);
        result.put("restoProfileImagePath",restoProfileImagePath);
        result.put("businessApproval",false);
        result.put("key",key);
        result.put("barangay",barangay);
        return result;
    }
}
