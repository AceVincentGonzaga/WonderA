package com.wandera.wanderaowner.datamodel;

public class UserListDataModel {
    private String businessKey;
    private String key;
    private String userId;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getUserId() {
        return userId;
    }

    public String getKey() {
        return key;
    }

    public String getBusinessKey() {
        return businessKey;
    }
}

