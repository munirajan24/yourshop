package com.dvishapps.yourshop.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Order implements Serializable {
    @NonNull
    @Expose
    private String test;

    public Order(@NonNull String test) {
        this.test = test;
    }

    @NonNull
    public String getTest() {
        return test;
    }

    public void setTest(@NonNull String test) {
        this.test = test;
    }
}
