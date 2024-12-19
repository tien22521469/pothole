package com.example.search_mapbox.model;

import com.google.gson.annotations.SerializedName;

public class ApiResponse {


    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("id")
    private int id;

    public ApiResponse(String status, String message, int id) {
        this.status = status;
        this.message = message;
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }
}