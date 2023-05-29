package com.example.taskmanager.task_fragments;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.taskmanager.R;
import com.example.taskmanager.db.task.Task;
import com.example.taskmanager.db.task.TaskViewModel;

// BroadcastReceiver para recibir el evento de alarma y generar la notificación de tarea
public class TaskNotificationReceiver extends BroadcastReceiver {
    // Constantes para la notificación
    public static final String NOTIFICATION_CHANNEL_ID = "task_notification_channel";

    private TaskViewModel taskViewModel;

    public void setTaskViewModel(TaskViewModel taskViewModel) {
        this.taskViewModel = taskViewModel;
    }

    // Método que se llama cuando se recibe la alarma
    @Override
    public void onReceive(Context context, Intent intent) {
        createNotificationChannel(context);

        String taskName = intent.getStringExtra("taskName");
        int taskStatus = intent.getIntExtra("taskStatus", 0);

        String notificationContent;
        if (taskStatus == 0) {
            notificationContent = String.format("Tu tarea %s no ha sido completada en el tiempo estimado", taskName);
        } else if (taskStatus == 1) {
            notificationContent = String.format("Tu tarea %s ha sido completada", taskName);
        } else {
            // Aquí puedes manejar cualquier caso de error inesperado
            notificationContent = "Hubo un problema con tu tarea";
        }

        Notification notification = createNotification(context, taskName, notificationContent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "TaskChannel";
            String description = "Channel for task notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private Notification createNotification(Context context, String taskName, String notificationContent) {
        return new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle(taskName)
                .setContentText(notificationContent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();
    }
}
