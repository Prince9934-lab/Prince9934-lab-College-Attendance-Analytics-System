package com.example.rungta.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.rungta.ui.defaulter.DefaulterActivity;

import java.util.Locale;

public class NotificationHelper {

    private static final String CHANNEL_ID = "defaulter_channel_id";
    private static final String CHANNEL_NAME = "Attendance Defaulter Alerts";
    private static final int NOTIFICATION_ID = 101;

    public static void showDefaulterNotification(Context context, String studentName, float percentage) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Alerts when student attendance falls below 75%");
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        Intent intent = new Intent(context, DefaulterActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        String percentageStr = String.format(Locale.getDefault(), "%.1f%%", percentage);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle("Attendance Warning (<75%)")
                .setContentText(studentName + " has low attendance: " + percentageStr)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Warning: " + studentName + " has attendance below the mandatory 75% threshold (" + percentageStr + "). Immediate action required."))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID + (int)(Math.random() * 1000), builder.build());
        }
    }
}
