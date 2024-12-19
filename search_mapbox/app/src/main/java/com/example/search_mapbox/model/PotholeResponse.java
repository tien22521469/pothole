package com.example.search_mapbox.model;

import com.google.gson.annotations.SerializedName;

public class PotholeResponse {
    @SerializedName("id")
    private String id;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("severity")
    private String severity;

    @SerializedName("user_id")
    private int userId;

    public String getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
    public String getSeverity() {
        return severity;
    }

    public int getUserId() {
        return userId;
    }
}