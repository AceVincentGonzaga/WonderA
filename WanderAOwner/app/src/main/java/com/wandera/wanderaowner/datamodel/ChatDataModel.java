package com.wandera.wanderaowner.datamodel;

public class ChatDataModel {
    private String userId;
    private String userName;
    private String timeStamp;
    private String message;
    private String businessId;
    private String userImage;
    private String senderId;

    public String getUserId() {
        return userId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public String getMessage() {
        return message;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getUserImage() {
        return userImage;

    }

    public String getSenderId() {
        return senderId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
}
