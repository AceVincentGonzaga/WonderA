package com.wandera.wandera.datamodel;

public class BusinessProfileModel {
    private String userId;
    private String name;
    private String address;
    private String contact;
    private String email;
    private String businessType;
    private String restoProfileImagePath;
    private String key;

    public String getKey() {
        return key;
    }

    public String getRestoProfileImagePath() {
        return restoProfileImagePath;
    }

    public String getUserId() {
        return userId;
    }
    public String getName(){
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getContact() {
        return contact;
    }

    public String getEmail() {
        return email;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public void setRestoProfileImagePath(String restoProfileImagePath) {
        this.restoProfileImagePath = restoProfileImagePath;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
