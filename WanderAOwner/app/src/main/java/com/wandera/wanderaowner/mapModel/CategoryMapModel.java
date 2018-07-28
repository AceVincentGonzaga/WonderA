package com.wandera.wanderaowner.mapModel;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Keji's Lab on 26/11/2017.
 */

public class CategoryMapModel {
    public String key;
    public String category;
    public String businessKey;



    public CategoryMapModel(){

    }
    public CategoryMapModel(String category,
                            String key,String businessKey)
                          {
        this.key = key;
        this.category = category;
        this.businessKey =  businessKey;
    }
    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();

        result.put("key", key);
        result.put("category", category);
        result.put("businessKey",businessKey);

        return result;
    }

}
