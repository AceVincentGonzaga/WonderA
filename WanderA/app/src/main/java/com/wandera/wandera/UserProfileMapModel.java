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
    public String userName;
    public String contactNumber;
    public String email;

    public UserProfileMapModel(){

    }
    public UserProfileMapModel(String userId,
                               String userImage, String userName, String contactNumber, String email){
        this.userId=userId;
        this.userImage=userImage;
        this.userName= userName;
        this.contactNumber= contactNumber;
        this.email=email;
    }
    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("userId",userId);
        result.put("userImage",userImage);
        result.put("userName", userName);
        result.put("number", contactNumber);
        result.put("email", email);


        return result;
    }

}
