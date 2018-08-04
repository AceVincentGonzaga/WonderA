package com.wandera.wandera.datamodel;

public class RestaurantMenuDataModel {

    private String menuName;
    private int    menuPrice;
    private String key;
    private String menuCategory;
    private String menuIconPath;

    public void setKey(String key) {
        this.key = key;
    }

    public void setMenuCategory(String menuCategory) {
        this.menuCategory = menuCategory;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public void setMenuPrice(int menuPrice) {
        this.menuPrice = menuPrice;
    }

    public void setMenuIconPath(String menuIconPath) {
        this.menuIconPath = menuIconPath;
    }

    public String getKey() {
        return key;
    }

    public int getMenuPrice() {
        return menuPrice;
    }

    public String getMenuCategory() {
        return menuCategory;
    }

    public String getMenuIconPath() {
        return menuIconPath;
    }

    public String getMenuName() {
        return menuName;
    }

}

