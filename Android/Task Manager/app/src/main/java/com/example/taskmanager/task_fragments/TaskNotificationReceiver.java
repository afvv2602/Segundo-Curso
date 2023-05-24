package com.example.taskmanager.task_fragments;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

// BroadcastReceiver para recibir el evento de alarma y generar la notificación de tarea
public class TaskNotificationReceiver extends BroadcastReceiver {
    // Constantes para la notificacion
    public static final String NOTIFICATION_CHANNEL_ID = "task_notification_channel";

    // Método que se llama cuando se recibe la alarma
    @Override
    public void onReceive(Context context, Intent intent) {
        // Crear el canal de notificacion (necesario para versiones de Android Oreo y posteriores)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "TaskChannel";
            String description = "Channel for task notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Registrar el canal en el sistema
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Crear la notificacion
        String taskName = intent.getStringExtra("taskName");
        Notification notification = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle(taskName)
                .setContentText(String.format("Tu tarea %s esta a punto de terminar",taskName))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        // Obtener el administrador de notificaciones y lanzar la notificacion
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}
