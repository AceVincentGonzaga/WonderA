package com.wandera.wanderaowner.datamodel;

public class UserProfileDataModel {
    private String userId;
    private String userName;
    private String userImage;
    private String contactNumber;
    private String email;

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

