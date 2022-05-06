package com.dvishapps.yourshop.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Country implements Serializable {

    @Expose
    private String id;
    @Expose
    private String name;
    @Expose
    private String status;
    @Expose
    private String added_date;
    @Expose
    private String updated_date;
    @Expose
    private String added_user_id;
    @Expose
    private String updated_user_i;
    @Expose
    private String updated_flag;
    @Expose
    private String added_date_str;

    public Country() {
    }

    public Country(@NonNull String id, String name, String status, String added_date, String updated_date, String added_user_id, String updated_user_i, String updated_flag, String added_date_str) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.added_date = added_date;
        this.updated_date = updated_date;
        this.added_user_id = added_user_id;
        this.updated_user_i = updated_user_i;
        this.updated_flag = updated_flag;
        this.added_date_str = added_date_str;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getAdded_user_id() {
        return added_user_id;
    }

    public void setAdded_user_id(String added_user_id) {
        this.added_user_id = added_user_id;
    }

    public String getUpdated_user_i() {
        return updated_user_i;
    }

    public void setUpdated_user_i(String updated_user_i) {
        this.updated_user_i = updated_user_i;
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

    @Override
    public String toString() {
        return "Country{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", added_date='" + added_date + '\'' +
                ", updated_date='" + updated_date + '\'' +
                ", added_user_id='" + added_user_id + '\'' +
                ", updated_user_i='" + updated_user_i + '\'' +
                ", updated_flag='" + updated_flag + '\'' +
                ", added_date_str='" + added_date_str + '\'' +
                '}';
    }
}
