package com.wandera.wanderaowner.datamodel;

public class ChatsDataModel {

    private String userId;
    private String userName;
    private String timeStamp;
    private String message;
    private String businessId;
    private String senderId;

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public void setSenderId(String senderId){ this.senderId= senderId;}


    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public String getBusinessId() {
        return businessId;
    }

    public String getSenderId() {
        return senderId;
    }
}
