package com.example.search_mapbox.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PotholeResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private List<PotholeItem> data;

    public String getStatus() {
        return status;
    }

    public List<PotholeItem> getData() {
        return data;
    }

    public static class PotholeItem {
        @SerializedName("id")
        private String id;

        @SerializedName("latitude")
        private String latitude;

        @SerializedName("longitude")
        private String longitude;

        @SerializedName("severity")
        private String severity;

        @SerializedName("user_id")
        private String userId;

        @SerializedName("created_at")
        private String createdAt;

        public String getId() {
            return id;
        }

        public double getLatitude() {
            return Double.parseDouble(latitude);
        }

        public double getLongitude() {
            return Double.parseDouble(longitude);
        }

        public double getSeverity() {
            return Double.parseDouble(severity);
        }

        public int getUserId() {
            return Integer.parseInt(userId);
        }

        public String getCreatedAt() {
            return createdAt;
        }
    }
}