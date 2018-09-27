package com.wandera.wandera.datamodel;

public class TranspoDataModel {
   private String driverName;
   private String key;
   private String origin;
   private String contactNumber;
   private String price;
   private String capacity;
   private String vanImg;
   private String modeOfTranspo;
   private String operator;

    public void setModeOfTranspo(String modeOfTranspo) {
        this.modeOfTranspo = modeOfTranspo;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getModeOfTranspo() {
        return modeOfTranspo;
    }

    public String getOperator() {
        return operator;
    }

    public void setVanImg(String vanImg) {
        this.vanImg = vanImg;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getKey() {
        return key;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getCapacity() {
        return capacity;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getOrigin() {
        return origin;
    }

    public String getPrice() {
        return price;
    }

    public String getVanImg() {
        return vanImg;
    }
}
