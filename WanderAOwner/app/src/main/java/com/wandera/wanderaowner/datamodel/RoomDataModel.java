package com.wandera.wanderaowner.datamodel;

public class RoomDataModel {

    private String roomName;
    private int roomPrice;
    private String roomDescription;
    private String roomId;
    private String businessKey;
    private String roomImage;

    public void setRoomImage(String roomImage) {
        this.roomImage = roomImage;
    }

    public String getRoomImage() {
        return roomImage;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setRoomPrice(int roomPrice) {
        this.roomPrice = roomPrice;
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getRoomId() {
        return roomId;
    }

    public int getRoomPrice() {
        return roomPrice;
    }

}

