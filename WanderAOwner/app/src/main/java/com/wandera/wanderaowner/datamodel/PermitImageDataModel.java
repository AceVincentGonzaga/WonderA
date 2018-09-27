package com.wandera.wanderaowner.datamodel;

public class PermitImageDataModel {

    private String categoryKey;
    private String imageUrl;
    private String key;
    private String businessKey;

    public String getImageUrl() {
        return imageUrl;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public String getCategoryKey() {
        return categoryKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setCategoryKey(String categoryKey) {
        this.categoryKey = categoryKey;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

