package com.example.search_mapbox.model;

import com.google.gson.annotations.SerializedName;

public class PotholeData {
    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("severity")
    private String severity;

    @SerializedName("user_id")
    private int userId;

    private Integer id;

    public PotholeData(Integer id, double latitude, double longitude, String severity, int userId) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.severity = severity;
        this.userId = userId;
    }

    public Integer getId() {
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

    public void setSeverity(String severity) {
        this.severity = severity;
    }

}