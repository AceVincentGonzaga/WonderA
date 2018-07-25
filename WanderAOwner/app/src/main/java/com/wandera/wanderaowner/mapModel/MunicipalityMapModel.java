package com.wandera.wanderaowner.mapModel;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Keji's Lab on 26/11/2017.
 */

public class MunicipalityMapModel {
    public String key;
    public String municipality;



    public MunicipalityMapModel(){

    }
    public MunicipalityMapModel(String municipality,
                                String key)
                          {
       this.key = key;
        this.municipality = municipality;

    }
    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();

        result.put("key", key);
        result.put("municipality", municipality);

        return result;
    }

}
