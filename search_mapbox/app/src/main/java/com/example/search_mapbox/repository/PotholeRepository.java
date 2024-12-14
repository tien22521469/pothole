package com.example.search_mapbox.repository;

import android.util.Log;
import android.widget.Toast;

import com.example.search_mapbox.MainActivity;
import com.example.search_mapbox.api.PotholeApi;
import com.example.search_mapbox.model.PotholeData;
import com.example.search_mapbox.model.PotholeResponse;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PotholeRepository {
    private static final String TAG = "PotholeRepository";
    private final PotholeApi potholeApi;
    private static PotholeRepository instance;

    public interface PotholeCallback {
        void onSuccess(List<PotholeData> potholes);
        void onError(String message);
    }

    private PotholeRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://nhom10.tanlamdevops.id.vn/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        potholeApi = retrofit.create(PotholeApi.class);
    }

    public static synchronized PotholeRepository getInstance() {
        if (instance == null) {
            instance = new PotholeRepository();
        }
        return instance;
    }

    public void getPotholes(PotholeCallback callback) {
        potholeApi.getPotholes().enqueue(new Callback<PotholeResponse>() {
            @Override
            public void onResponse(Call<PotholeResponse> call, Response<PotholeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PotholeResponse potholeResponse = response.body();
                    if ("success".equals(potholeResponse.getStatus()) && potholeResponse.getData() != null) {
                        List<PotholeData> potholes = new ArrayList<>();
                        for (PotholeResponse.PotholeItem item : potholeResponse.getData()) {
                            potholes.add(new PotholeData(
                                    item.getLatitude(),
                                    item.getLongitude(),
                                    item.getSeverity(),
                                    item.getUserId()
                            ));
                            Log.d("API",potholes.get(0).getLatitude()+" "+potholes.get(0).getLongitude());
                        }
                        callback.onSuccess(potholes);
                    } else {
                        callback.onError("Invalid response format");
                    }
                } else {
                    callback.onError("Failed to get potholes");
                }
            }

            @Override
            public void onFailure(Call<PotholeResponse> call, Throwable t) {
                Log.e(TAG, "Error getting potholes: " + t.getMessage());
                callback.onError(t.getMessage());
            }
        });
    }
} 