package com.example.search_mapbox.model;

import com.google.gson.annotations.SerializedName;

public class PotholeResponse {
    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}