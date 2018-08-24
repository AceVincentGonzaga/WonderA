package com.wandera.wandera.mapmodel;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Keji's Lab on 26/11/2017.
 */

public class SignalWifiMapModel {
    public String key;
    public boolean wifi;
    public String signal;



    public SignalWifiMapModel(){

    }
    public SignalWifiMapModel(String key,
                              boolean wifi, String signal)
                          {
       this.key = key;
    this.signal = signal;
    this.wifi = wifi;
    }
    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();

        result.put("key", key);
        result.put("signal",signal);
        result.put("wifi",wifi);

        return result;
    }

}
