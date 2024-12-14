package com.example.search_mapbox.api;

import com.example.search_mapbox.model.PotholeData;
import com.example.search_mapbox.model.ApiResponse;
import com.example.search_mapbox.model.PotholeResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PotholeApi {
    @POST("add_pothole.php")
    Call<ApiResponse> addPothole(@Body PotholeData data);

    @GET("get_potholes.php")
    Call<PotholeResponse> getPotholes();
} 