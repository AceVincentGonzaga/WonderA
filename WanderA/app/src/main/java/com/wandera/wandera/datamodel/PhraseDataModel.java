package com.wandera.wandera.datamodel;

public class PhraseDataModel {
    private String key;
    private String category;
    private String english;
    private String karaya;


    public void setKey(String key) {
        this.key = key;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getKey() {
        return key;
    }

    public String getCategory() {
        return category;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public void setKaraya(String karaya) {
        this.karaya = karaya;
    }

    public String getEnglish() {
        return english;
    }

    public String getKaraya() {
        return karaya;
    }
}
