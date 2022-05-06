package com.dvishapps.yourshop.models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Category implements Serializable {

    @Expose
    private String id;
    @Expose
    private String shop_category_parent_id;
    @Expose
    private String name;
    @Expose
    private String added_user_id;
    @Expose
    private String updated_user_id;
    @Expose
    private String subcategory_need;
    @Expose
    private String status;
    @Expose
    private String updated_flag;
    @Expose
    private String touch_count;
    @Expose
    private String added_date;
    @Expose
    private String updated_date;
    @Expose
    private Photo default_photo;
    @Expose
    private Photo default_icon;

    @Expose
    private String category_bussiness_type;


    @Expose
    private String category_open_time;


    @Expose
    private String category_close_time;


    @Expose
    private String is_enable;


    public Category() {

    }

    public Category(String id, String name) {
        this.id = id;
        this.name = name;
    }


    public Category(String id, String shop_category_parent_id, String name,
                    String added_user_id,
                    String updated_user_id,
                    String subcategory_need,
                    String status, String updated_flag,
                    String touch_count, String added_date, String updated_date,
                    Photo default_photo,
                    Photo default_icon,
                    String category_bussiness_type,
                    String category_open_time,
                    String category_close_time,
                    String is_enable
    ) {
        this.id = id;
        this.shop_category_parent_id = shop_category_parent_id;
        this.name = name;
        this.added_user_id = added_user_id;
        this.updated_user_id = updated_user_id;
        this.subcategory_need = subcategory_need;
        this.status = status;
        this.updated_flag = updated_flag;
        this.touch_count = touch_count;
        this.added_date = added_date;
        this.updated_date = updated_date;
        this.default_icon = default_icon;
        this.default_photo = default_photo;
        this.category_bussiness_type = category_bussiness_type;
        this.category_open_time = category_open_time;
        this.category_close_time = category_close_time;
        this.is_enable = is_enable;
    }

    public String getId() {
        return id;
    }

    public String getShop_category_parent_id() {
        return shop_category_parent_id;
    }

    public void setShop_category_parent_id(String shop_category_parent_id) {
        this.shop_category_parent_id = shop_category_parent_id;
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

    public String getAdded_user_id() {
        return added_user_id;
    }

    public void setAdded_user_id(String added_user_id) {
        this.added_user_id = added_user_id;
    }

    public String getUpdated_user_id() {
        return updated_user_id;
    }

    public void setUpdated_user_id(String updated_user_id) {
        this.updated_user_id = updated_user_id;
    }

    public String getSubcategory_need() {
        return subcategory_need;
    }

    public void setSubcategory_need(String subcategory_need) {
        this.subcategory_need = subcategory_need;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdated_flag() {
        return updated_flag;
    }

    public void setUpdated_flag(String updated_flag) {
        this.updated_flag = updated_flag;
    }

    public String getTouch_count() {
        return touch_count;
    }

    public void setTouch_count(String touch_count) {
        this.touch_count = touch_count;
    }

    public String getAdded_date() {
        return added_date;
    }

    public void setAdded_date(String added_date) {
        this.added_date = added_date;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
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

    public String getCategory_bussiness_type() {
        return category_bussiness_type;
    }

    public void setCategory_bussiness_type(String category_bussiness_type) {
        this.category_bussiness_type = category_bussiness_type;
    }

    public String getCategory_open_time() {
        return category_open_time;
    }

    public void setCategory_open_time(String category_open_time) {
        this.category_open_time = category_open_time;
    }

    public String getCategory_close_time() {
        return category_close_time;
    }

    public void setCategory_close_time(String category_close_time) {
        this.category_close_time = category_close_time;
    }

    public String getIs_enable() {
        return is_enable;
    }

    public void setIs_enable(String is_enable) {
        this.is_enable = is_enable;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id='" + id + '\'' +
                ", shop_category_parent_id='" + shop_category_parent_id + '\'' +
                ", name='" + name + '\'' +
                ", added_user_id='" + added_user_id + '\'' +
                ", updated_user_id='" + updated_user_id + '\'' +
                ", subcategory_need='" + subcategory_need + '\'' +
                ", status='" + status + '\'' +
                ", updated_flag='" + updated_flag + '\'' +
                ", touch_count='" + touch_count + '\'' +
                ", added_date='" + added_date + '\'' +
                ", updated_date='" + updated_date + '\'' +
                ", default_photo=" + default_photo +
                ", default_icon=" + default_icon +
                ", category_bussiness_type=" + category_bussiness_type +
                ", category_open_time=" + category_open_time +
                ", category_close_time=" + category_close_time +
                ", is_enable=" + is_enable +
                '}';
    }
}
