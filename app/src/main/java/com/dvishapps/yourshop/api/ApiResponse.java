package com.dvishapps.yourshop.api;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class ApiResponse implements Serializable {
    @Expose
    private String status;
    @Expose
    private String message;

    public ApiResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
