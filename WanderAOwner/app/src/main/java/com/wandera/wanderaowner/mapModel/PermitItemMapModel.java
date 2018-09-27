package com.wandera.wanderaowner.mapModel;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Keji's Lab on 26/11/2017.
 */

public class PermitItemMapModel {

    public String categoryKey;
    public String imageUrl;
    public String key;
    public String businessKey;




    public PermitItemMapModel(){

    }
    public PermitItemMapModel(String key,String categoryKey,String imageUrl,String businessKey)
                          {
        this.key = key;
        this.categoryKey = categoryKey;
        this.businessKey =  businessKey;
        this.imageUrl = imageUrl;
    }
    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();

        result.put("categoryKey", categoryKey);
        result.put("key", key);
        result.put("imageUrl", imageUrl);
        result.put("businessKey",businessKey);

        return result;
    }

}
