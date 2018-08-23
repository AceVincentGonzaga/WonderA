package com.wandera.wanderaowner.datamodel;

public class GalleryDataModel {

    private String key;
    private String imagePath;
    private String businesskey;

    public void setKey(String key) {
        this.key = key;
    }

    public void setBusinesskey(String businesskey) {
        this.businesskey = businesskey;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getKey() {
        return key;
    }

    public String getBusinesskey() {
        return businesskey;
    }

    public String getImagePath() {
        return imagePath;
    }

}
