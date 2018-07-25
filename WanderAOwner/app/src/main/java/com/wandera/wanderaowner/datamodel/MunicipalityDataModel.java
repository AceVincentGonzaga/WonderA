package com.wandera.wanderaowner.datamodel;

public class MunicipalityDataModel {

    private String municipality;
    private String key;

    public void setKey(String key) {
        this.key = key;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public String getKey() {
        return key;
    }

    public String getMunicipality() {
        return municipality;
    }
}

