package com.dvishapps.yourshop.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;

public class ProductSpecs {
    @Expose
    @NonNull
    private String id;

    @Expose
    private String product_id;

    @Expose
    private String name;

    @Expose
    private String description;

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
    private String is_empty_object;

    public ProductSpecs(@NonNull String id, String product_id, String name, String description, String added_date, String added_user_id, String updated_date, String updated_user_id, String updated_flag, String is_empty_object) {
        this.id = id;
        this.product_id = product_id;
        this.name = name;
        this.description = description;
        this.added_date = added_date;
        this.added_user_id = added_user_id;
        this.updated_date = updated_date;
        this.updated_user_id = updated_user_id;
        this.updated_flag = updated_flag;
        this.is_empty_object = is_empty_object;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getIs_empty_object() {
        return is_empty_object;
    }

    public void setIs_empty_object(String is_empty_object) {
        this.is_empty_object = is_empty_object;
    }

    @Override
    public String toString() {
        return "ProductSpecs{" +
                "id='" + id + '\'' +
                ", product_id='" + product_id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", added_date='" + added_date + '\'' +
                ", added_user_id='" + added_user_id + '\'' +
                ", updated_date='" + updated_date + '\'' +
                ", updated_user_id='" + updated_user_id + '\'' +
                ", updated_flag='" + updated_flag + '\'' +
                ", is_empty_object='" + is_empty_object + '\'' +
                '}';
    }
}
