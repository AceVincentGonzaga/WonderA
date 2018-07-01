package com.wandera.wandera;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Keji's Lab on 26/11/2017.
 */

public class UserProfileMapModel {
    public String userId;
    public String userImage;


    public UserProfileMapModel(){

    }
    public UserProfileMapModel(String userId,
                               String userImage){
        this.userId=userId;
        this.userImage=userImage;
    }
    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("userId",userId);
        result.put("userImage",userImage);

        return result;
    }

}
