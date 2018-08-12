package com.wandera.wandera.mapmodel;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Keji's Lab on 26/11/2017.
 */

public class RatingCommentMapModel {
   public String accountId;
   public String comment;
   public float rating;
   public String businessId;


    public RatingCommentMapModel(){

    }
    public RatingCommentMapModel(String accountId,String comment,float rating,
                                 String businessId)
                          {
       this.accountId = accountId;
       this.comment = comment;
       this.rating = rating;
       this.businessId = businessId;



    }
    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();

        result.put("accountId",accountId);
        result.put("businessId",businessId);
        result.put("rating",rating);
        result.put("comment",comment);

        return result;
    }

}
