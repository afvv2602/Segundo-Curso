package com.example.taskmanager.task_fragments;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class TaskNotificationReceiver extends BroadcastReceiver {
    public static final String NOTIFICATION_CHANNEL_ID = "task_notification_channel";
    public static final String NOTIFICATION_TITLE = "Task Deadline";
    public static final String NOTIFICATION_CONTENT = "You have a task deadline approaching.";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Create the NotificationChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "TaskChannel";
            String description = "Channel for task notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Create a notification
        Notification notification = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle(NOTIFICATION_TITLE)
                .setContentText(NOTIFICATION_CONTENT)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        // Get the NotificationManager and notify
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}
