package com.wandera.wandera.mapmodel;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Keji's Lab on 26/11/2017.
 */

public class PhraseBookCategoryMapModel {
    public String key;
    public String category;

    public PhraseBookCategoryMapModel(){

    }
    public PhraseBookCategoryMapModel(String category,
                                      String key)
                          {
       this.key = key;
        this.category = category;


    }
    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();

        result.put("key", key);
        result.put("category", category);

        return result;
    }

}
