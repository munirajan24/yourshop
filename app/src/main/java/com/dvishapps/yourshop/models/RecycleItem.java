package com.dvishapps.yourshop.models;

public class RecycleItem {
    private String id;
    private String name;
    private String imageUrl;
    private String subcategory_need = "";
    private String status = "";
    private String shopId = "";
    private String loggedUser = "";
    private String openTime = "";
    private String closeTime = "";
    private String is_enable = "";

    public RecycleItem() {
    }

    public RecycleItem(String id, String imageUrl) {
        this.id = id;
        this.imageUrl = imageUrl;
    }

    public RecycleItem(String id, String name, String imageUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public RecycleItem(String id, String name, String imageUrl, String subcategory_need) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.subcategory_need = subcategory_need;
    }

    public RecycleItem(String id, String name, String imageUrl, String status, String openTime, String closeTime) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.status = status;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    public RecycleItem(String id, String name, String imageUrl, String subcategory_need, String status) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.subcategory_need = subcategory_need;
        this.status = status;
    }

    public RecycleItem(String id, String name, String imageUrl, String subcategory_need, String status, String openTime, String closeTime) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.subcategory_need = subcategory_need;
        this.status = status;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    public RecycleItem(String id, String name, String imageUrl, String subcategory_need, String status, String openTime, String closeTime, String is_enable) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.subcategory_need = subcategory_need;
        this.status = status;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.is_enable = is_enable;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSubcategory_need() {
        if (subcategory_need == null) {
            subcategory_need = "";
        }
        return subcategory_need;
    }

    public void setSubcategory_need(String subcategory_need) {
        this.subcategory_need = subcategory_need;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(String loggedUser) {
        this.loggedUser = loggedUser;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getIs_enable() {
        return is_enable;
    }

    public void setIs_enable(String is_enable) {
        this.is_enable = is_enable;
    }
}
