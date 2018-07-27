package com.wandera.wanderaowner.datamodel;

public class CategoryDataModel {

    private String category;
    private String key;
    private String businessKey;

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

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

    public String getBusinessKey() {
        return businessKey;
    }
}

