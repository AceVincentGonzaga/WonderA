package com.wandera.wanderaowner.datamodel;

public class BarangayDataModel {
    private String key;
    private String munId;
    private String barangay;

    public void setKey(String key) {
        this.key = key;
    }

    public void setBarangay(String barangay) {
        this.barangay = barangay;
    }

    public void setMunId(String munId) {
        this.munId = munId;
    }

    public String getKey() {
        return key;
    }

    public String getBarangay() {
        return barangay;
    }

    public String getMunId() {
        return munId;
    }

}
