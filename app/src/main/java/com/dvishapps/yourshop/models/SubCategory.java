package com.dvishapps.yourshop.models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class SubCategory implements Serializable {

    @Expose
    private String id;

    @Expose
    private String shop_sub_category_parent_id;

    @Expose
    private String name;

    @Expose
    private String status;

    @Expose
    private String ordering;

    @Expose
    private String added_date;

    @Expose
    private String added_user_id;

    @Expose
    private String updated_date;

    @Expose
    private String updated_user_id;

    @Expose
    private String updated_flag;

    @Expose
    private String added_date_str;

    @Expose
    private Photo default_photo;


    @Expose
    private Photo default_icon;

    @Expose
    private String  sub_category_bussiness_type;

    @Expose
    private String  sub_category_open_time;

    @Expose
    private String  sub_category_close_time;


    @Expose
    private String  is_enable;



    public SubCategory() {
    }

    public SubCategory(String id, String shop_sub_category_parent_id, String name, String status, String ordering, String added_date, String added_user_id, String updated_date, String updated_user_id, String updated_flag, String added_date_str, Photo default_photo, Photo default_icon,
                       String  sub_category_bussiness_type,
                       String  sub_category_open_time,
                       String  sub_category_close_time,
                       String  is_enable
    ) {
        this.id = id;
        this.shop_sub_category_parent_id = shop_sub_category_parent_id;
        this.name = name;
        this.status = status;
        this.ordering = ordering;
        this.added_date = added_date;
        this.added_user_id = added_user_id;
        this.updated_date = updated_date;
        this.updated_user_id = updated_user_id;
        this.updated_flag = updated_flag;
        this.added_date_str = added_date_str;
        this.default_photo = default_photo;
        this.default_icon = default_icon;
        this.sub_category_bussiness_type = sub_category_bussiness_type;
        this.sub_category_open_time = sub_category_open_time;
        this.sub_category_close_time = sub_category_close_time;
        this.is_enable = is_enable;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShop_sub_category_parent_id() {
        return shop_sub_category_parent_id;
    }

    public void setShop_sub_category_parent_id(String shop_sub_category_parent_id) {
        this.shop_sub_category_parent_id = shop_sub_category_parent_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrdering() {
        return ordering;
    }

    public void setOrdering(String ordering) {
        this.ordering = ordering;
    }

    public String getAdded_date() {
        return added_date;
    }

    public void setAdded_date(String added_date) {
        this.added_date = added_date;
    }

    public String getAdded_user_id() {
        return added_user_id;
    }

    public void setAdded_user_id(String added_user_id) {
        this.added_user_id = added_user_id;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }

    public String getUpdated_user_id() {
        return updated_user_id;
    }

    public void setUpdated_user_id(String updated_user_id) {
        this.updated_user_id = updated_user_id;
    }

    public String getUpdated_flag() {
        return updated_flag;
    }

    public void setUpdated_flag(String updated_flag) {
        this.updated_flag = updated_flag;
    }

    public String getAdded_date_str() {
        return added_date_str;
    }

    public void setAdded_date_str(String added_date_str) {
        this.added_date_str = added_date_str;
    }

    public Photo getDefault_photo() {
        return default_photo;
    }

    public void setDefault_photo(Photo default_photo) {
        this.default_photo = default_photo;
    }

    public Photo getDefault_icon() {
        return default_icon;
    }

    public void setDefault_icon(Photo default_icon) {
        this.default_icon = default_icon;
    }

    public String getSub_category_bussiness_type() {
        return sub_category_bussiness_type;
    }

    public void setSub_category_bussiness_type(String sub_category_bussiness_type) {
        this.sub_category_bussiness_type = sub_category_bussiness_type;
    }

    public String getSub_category_open_time() {
        return sub_category_open_time;
    }

    public void setSub_category_open_time(String sub_category_open_time) {
        this.sub_category_open_time = sub_category_open_time;
    }

    public String getSub_category_close_time() {
        return sub_category_close_time;
    }

    public void setSub_category_close_time(String sub_category_close_time) {
        this.sub_category_close_time = sub_category_close_time;
    }

    public String getIs_enable() {
        return is_enable;
    }

    public void setIs_enable(String is_enable) {
        this.is_enable = is_enable;
    }

    @Override
    public String toString() {
        return "SubCategory{" +
                "id='" + id + '\'' +
                ", shop_sub_category_parent_id='" + shop_sub_category_parent_id + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", ordering='" + ordering + '\'' +
                ", added_date='" + added_date + '\'' +
                ", added_user_id='" + added_user_id + '\'' +
                ", updated_date='" + updated_date + '\'' +
                ", updated_user_id='" + updated_user_id + '\'' +
                ", updated_flag='" + updated_flag + '\'' +
                ", added_date_str='" + added_date_str + '\'' +
                ", default_photo='" + default_photo + '\'' +
                ", default_icon='" + default_icon + '\'' +
                ", sub_category_bussiness_type='" + sub_category_bussiness_type + '\'' +
                ", sub_category_open_time='" + sub_category_open_time + '\'' +
                ", sub_category_close_time='" + sub_category_close_time + '\'' +
                ", is_enable='" + is_enable + '\'' +
                '}';
    }
}

