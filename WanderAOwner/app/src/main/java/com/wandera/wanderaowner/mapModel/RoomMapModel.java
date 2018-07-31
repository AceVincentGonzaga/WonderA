package com.wandera.wanderaowner.mapModel;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Keji's Lab on 26/11/2017.
 */

public class RoomMapModel {
    public String roomId;
    public String roomName;
    public int roomPrice;
    public String roomDescription;
    public String businessKey;
    public String roomImage;


    public RoomMapModel(){

    }
    public RoomMapModel(String roomId,String roomName,int roomPrice,String roomDescriptionm,String businessKey,String roomImage)
                          {
       this.roomId = roomId;
       this.roomName = roomName;
       this.roomPrice = roomPrice;
       this.roomDescription = roomDescriptionm;
       this.businessKey = businessKey;
       this.roomImage = roomImage;


    }
    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();

        result.put("roomId",roomId);
        result.put("roomName",roomName);
        result.put("roomPrice",roomPrice);
        result.put("roomDescription",roomDescription);
        result.put("businessKey",businessKey);
        result.put("roomImage",roomImage);

        return result;
    }

}
