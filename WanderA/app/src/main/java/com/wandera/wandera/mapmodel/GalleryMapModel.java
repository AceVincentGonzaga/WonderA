package com.wandera.wandera.mapmodel;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Keji's Lab on 26/11/2017.
 */

public class GalleryMapModel {

    public String key;

    public String imagePath;
    public String businesskey;



    public GalleryMapModel(){

    }
    public GalleryMapModel(String key, String imagePath, String businesskey)
                          {
       this.key = key;
        this.imagePath = imagePath;
        this.businesskey = businesskey;

    }
    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("key",key);
        result.put("imagePath",imagePath);
        result.put("businesskey",businesskey);

        return result;
    }

}
