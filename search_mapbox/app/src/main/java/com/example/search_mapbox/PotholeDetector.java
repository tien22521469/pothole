package com.example.search_mapbox;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.util.Log;

import com.mapbox.geojson.Point;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.search_mapbox.model.PotholeData;
import com.example.search_mapbox.api.PotholeApi;
import com.example.search_mapbox.model.ApiResponse;

public class PotholeDetector implements SensorEventListener {
    private static final float POTHOLE_THRESHOLD = 15.0f; // Ngưỡng phát hiện ổ gà
    private final SensorManager sensorManager;
    private final Sensor accelerometer;
    private final List<Point> potholeLocations = new ArrayList<>();
    private final PotholeDetectorCallback callback;
    private Location currentLocation;

    public interface PotholeDetectorCallback {
        void onPotholeDetected(Point location);
    }

    public PotholeDetector(Context context, PotholeDetectorCallback callback) {
        this.callback = callback;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void start() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    public void updateLocation(Location location) {
        this.currentLocation = location;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float acceleration = (float) Math.sqrt(x * x + y * y + z * z);

            if (acceleration > POTHOLE_THRESHOLD && currentLocation != null) {
                Point potholeLocation = Point.fromLngLat(
                        currentLocation.getLongitude(),
                        currentLocation.getLatitude()
                );
                potholeLocations.add(potholeLocation);
                callback.onPotholeDetected(potholeLocation);
                sendPotholeData(potholeLocation, acceleration);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public List<Point> getPotholeLocations() {
        return potholeLocations;
    }

    private void sendPotholeData(Point location, double acceleration) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://nhom10.tanlamdevops.id.vn/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        String severityLevel;
        if (acceleration <= 10.0f) {
            severityLevel = "Low";
        } else if (acceleration <= 20.0f) {
            severityLevel = "Medium";
        } else {
            severityLevel = "High";
        }

        Log.d("PotholeDetector", "Acceleration: " + acceleration + ", Severity: " + severityLevel);

        PotholeData potholeData = new PotholeData(
                null,
                location.latitude(),
                location.longitude(),
                severityLevel,  // Sử dụng severity level đã chuyển đổi
                1  // Đảm bảo user_id hợp lệ
        );

        retrofit.create(PotholeApi.class)
                .addPothole(potholeData)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ApiResponse apiResponse = response.body();
                            if ("success".equals(apiResponse.getStatus())) {
                                Log.d("PotholeDetector", "Pothole saved successfully with id: " +
                                        apiResponse.getId() + ", severity: " + severityLevel);
                            } else {
                                Log.e("PotholeDetector", "Failed to save pothole: " +
                                        apiResponse.getMessage());
                                Log.e("PotholeDetector", "Response body: " + response.body());
                            }
                        } else {
                            try {

                                Log.e("PotholeDetector", "Error response: " +
                                        response.errorBody().string());
                            } catch (Exception e) {
                                Log.e("PotholeDetector", "Error reading error body", e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Log.e("PotholeDetector", "Network error: " + t.getMessage());
                    }
                });
    }
}