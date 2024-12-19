package com.example.search_mapbox;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import com.example.search_mapbox.MainActivity;
import com.example.search_mapbox.R;
import com.example.search_mapbox.model.PotholeData;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.widget.RemoteViews;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Optional;

public class PotholeProximityDetector {
    private static final double DETECTION_RADIUS_METERS = 100.0;
    private static final long MIN_UPDATE_INTERVAL = 5000; // 5 seconds

    private long lastUpdateTime = 0;
    private final Context context;
    private final List<PotholeData> potholes = new ArrayList<>();
    private final Set<String> detectedPotholeIds = new HashSet<>();
    private Location currentLocation;

    public PotholeProximityDetector(Context context) {
        this.context = context;
    }

    public void updatePotholes(List<PotholeData> newPotholes) {
        potholes.clear();
        potholes.addAll(newPotholes);
    }

    public void updateLocation(Location location) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastUpdateTime < MIN_UPDATE_INTERVAL) {
            return;
        }

        Log.d("PotholeProximity", "Updating location: " + location.getLatitude() + ", " + location.getLongitude());
        this.currentLocation = location;
        checkNearbyPotholes();
        lastUpdateTime = currentTime;
    }

    public void checkNearbyPotholes() {
        if (currentLocation == null) {
            Log.d("PotholeProximity", "Current location is null");
            return;
        }

        Log.d("PotholeProximity", "Checking " + potholes.size() + " potholes");

        for (PotholeData pothole : potholes) {
            float[] results = new float[1];
            Location.distanceBetween(
                    currentLocation.getLatitude(),
                    currentLocation.getLongitude(),
                    pothole.getLatitude(),
                    pothole.getLongitude(),
                    results
            );

            String potholeId = Optional.ofNullable(pothole.getId())
                    .map(Object::toString)
                    .orElse("unknown");
            Log.d("PotholeProximity", "Distance to pothole " + potholeId + ": " + results[0] + " meters");

            if (results[0] <= DETECTION_RADIUS_METERS) {
                if (!detectedPotholeIds.contains(potholeId)) {
                    Log.d("PotholeProximity", "New pothole detected! Sending notification");
                    detectedPotholeIds.add(potholeId);
                    sendNotificationAsync(pothole, results[0]);
                }
            }
        }
    }

    private void sendNotificationAsync(final PotholeData pothole, final float distance) {
        new Thread(() -> {
            try {
                sendNotification(pothole, distance);
            } catch (Exception e) {
                Log.e("PotholeProximity", "Error sending notification", e);
            }
        }).start();
    }

    private void sendNotification(PotholeData pothole, float distance) {
        try {
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            // Create notification channel for Android O and above
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        "pothole_channel",
                        "Pothole Alerts",
                        NotificationManager.IMPORTANCE_HIGH
                );
                channel.setDescription("Alerts for nearby potholes");
                channel.enableVibration(true);
                channel.setVibrationPattern(new long[]{0, 500, 200, 500});
                channel.enableLights(true);
                channel.setLightColor(android.graphics.Color.RED);
                channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
                notificationManager.createNotificationChannel(channel);
            }

            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            String potholeId = pothole.getId() != null ? pothole.getId().toString() : "unknown";

            NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle()
                    .setBigContentTitle("⚠️ CẢNH BÁO Ổ GÀ PHÍA TRƯỚC!")
                    .bigText(String.format(Locale.getDefault(),
                            "Phát hiện ổ gà cách vị trí của bạn %.1f mét.\nHãy giảm tốc độ và di chuyển cẩn thận!",
                            distance));

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "pothole_channel")
                    .setSmallIcon(R.drawable.img_1)
                    .setContentTitle("⚠️ CẢNH BÁO Ổ GÀ PHÍA TRƯỚC!")
                    .setContentText(String.format(Locale.getDefault(), "Cách %.1f mét", distance))
                    .setStyle(bigTextStyle)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                    .setAutoCancel(true)
                    .setVibrate(new long[]{0, 1000, 500, 1000})
                    .setLights(android.graphics.Color.RED, 3000, 3000)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setContentIntent(pendingIntent);

            notificationManager.notify(potholeId.hashCode(), builder.build());

            Log.d("PotholeProximity", "Notification sent for pothole " + potholeId);
        } catch (Exception e) {
            Log.e("PotholeProximity", "Error sending notification", e);
        }
    }

    public void reset() {
        detectedPotholeIds.clear();
    }
}

