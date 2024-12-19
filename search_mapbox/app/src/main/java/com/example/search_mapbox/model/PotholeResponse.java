package com.example.search_mapbox.model;

import com.google.gson.annotations.SerializedName;

public class PotholeResponse {
    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("severity")
    private double severity;

    @SerializedName("user_id")
    private int userId;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
    public double getSeverity() {
        return severity;
    }

    public int getUserId() {
        return userId;
    }
}