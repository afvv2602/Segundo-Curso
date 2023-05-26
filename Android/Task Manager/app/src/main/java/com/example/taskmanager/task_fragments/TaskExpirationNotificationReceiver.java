package com.example.taskmanager.task_fragments;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;


public class TaskExpirationNotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int taskId = intent.getIntExtra("taskId", -1);
            String taskName = intent.getStringExtra("taskName");
            // Notify the user
            // You need to create the notification here.
            // Use the notification manager to send the notification
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification = new NotificationCompat.Builder(context, "task_channel")
                    .setContentTitle("Tarea fallida")
                    .setContentText("No has completado la tarea '" + taskName + "' a tiempo")
                    .setSmallIcon(android.R.drawable.ic_dialog_alert)
                    .build();
            notificationManager.notify(taskId, notification);
        }
    }

