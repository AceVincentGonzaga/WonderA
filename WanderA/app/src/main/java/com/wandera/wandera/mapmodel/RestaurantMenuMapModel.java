package com.wandera.wandera.mapmodel;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Keji's Lab on 26/11/2017.
 */

public class RestaurantMenuMapModel {
    public String menuName;
    public int    menuPrice;
    public String key;
    public String menuCategory;
    public String menuIconPath;
    public String businesskey;



    public RestaurantMenuMapModel(){

    }
    public RestaurantMenuMapModel(String menuName, int menuPrice, String key, String menuCategory, String menuIconPath, String businesskey)
                          {
       this.key = key;
        this.menuName = menuName;
        this.menuPrice  =menuPrice;
        this.menuIconPath = menuIconPath;
        this.menuCategory  = menuCategory;
        this.businesskey = businesskey;

    }
    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("key",key);
        result.put("menuName",menuName);
        result.put("menuPrice",menuPrice);
        result.put("menuIconPath",menuIconPath);
        result.put("menuCategory",menuCategory);
        result.put("businesskey",businesskey);

        return result;
    }

}
