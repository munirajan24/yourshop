package com.dvishapps.yourshop.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class TestImage implements Serializable {
    @NonNull
    @Expose
    private String args;
    @Expose
    private String data;
    @Expose
    private String files;
    @Expose
    private String form ="";
    @Expose
    private String headers;
    @Expose
    private String json ="";
    @Expose
    private String url ="";

       public TestImage() {
    }

    public TestImage(@NonNull String args, String files) {
        this.args = args;
        this.files = files;
    }

    public TestImage(@NonNull String args, String data, String files, String form, String headers, String json, String url) {
        this.args = args;
        this.data = data;
        this.files = files;
        this.form = form;
        this.headers = headers;
        this.json = json;
        this.url = url;

    }

    @Override
    public String toString() {
        return "Shop{" +
                "id='" + args + '\'' +
                ", shipping_id='" + data + '\'' +
                ", name='" + files + '\'' +
                ", individual_app='" + form + '\'' +
                ", description='" + headers + '\'' +
                ", city='" + json + '\'' +
                ", area='" + url +
                '}';
    }

    @NonNull
    public String getId() {
        return args;
    }

    public void setId(@NonNull String id) {
        this.args = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }


    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        if (json ==null){
            this.json ="";
        }else {
            this.json = json;
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        if (url ==null){
            this.url ="";
        }else {
            this.url = url;
        }
    }


}
