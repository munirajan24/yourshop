package com.dvishapps.yourshop.models;

public class LimitData {
    public String loginUserId = "";
    public int limit = 0;
    public int offset = 10;
    public Boolean isConnected = false;
    public String shopId = "";
    public String orderBy = "";


    public LimitData(int limit, int offset) {
        this.limit = limit;
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "LimitData{" +
                "loginUserId='" + loginUserId + '\'' +
                ", limit='" + limit + '\'' +
                ", offset='" + offset + '\'' +
                ", isConnected=" + isConnected +
                ", shopId='" + shopId + '\'' +
                ", orderBy='" + orderBy + '\'' +
                '}';
    }
}
