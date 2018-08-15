package com.wandera.wanderaowner.mapModel;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Keji's Lab on 26/11/2017.
 */

public class BarangayMapModel {
    public String key;
    public String munId;
    public String barangay;



    public BarangayMapModel(){

    }
    public BarangayMapModel(String key,
                            String munId,String barangay)
                          {
       this.key = key;
       this.barangay = barangay;
       this.munId = munId;

    }
    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();

        result.put("key", key);
        result.put("munId",munId);
        result.put("barangay",barangay);

        return result;
    }

}
