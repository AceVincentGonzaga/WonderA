package com.wandera.wanderaowner.datamodel;

public class UserListDataModel {
    private String businessKey;
    private String key;
    private String userId;
    private boolean seen_user;
    private boolean seen_owner;

    public void setSeen_owner(boolean seen_owner) {
        this.seen_owner = seen_owner;
    }

    public void setSeen_user(boolean seen_user) {
        this.seen_user = seen_user;
    }

    public boolean isSeen_owner() {
        return seen_owner;
    }

    public boolean isSeen_user() {
        return seen_user;
    }

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

