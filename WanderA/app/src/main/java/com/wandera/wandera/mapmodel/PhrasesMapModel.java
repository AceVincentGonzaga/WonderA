package com.wandera.wandera.mapmodel;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Keji's Lab on 26/11/2017.
 */

public class PhrasesMapModel {
    public String key;
    public String category;
    public String english;
    public String karaya;

    public PhrasesMapModel(){

    }
    public PhrasesMapModel(String category,
                           String key,String english,String karaya)
                          {
       this.key = key;
        this.category = category;
        this.english = english;
        this.category = category;


    }
    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();

        result.put("key", key);
        result.put("category", category);
        result.put("englis",english);
        result.put("karaya",karaya);

        return result;
    }

}
