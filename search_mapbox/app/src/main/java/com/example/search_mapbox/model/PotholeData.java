package com.example.search_mapbox.model;

import com.google.gson.annotations.SerializedName;

public class PotholeData {
    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("severity")
    private double severity;

    @SerializedName("user_id")
    private int userId;

    public PotholeData(double latitude, double longitude, double severity, int userId) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.severity = severity;
        this.userId = userId;
    }

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