package com.example.taskmanager.utils;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.taskmanager.R;
import com.example.taskmanager.db.task.Task;
import com.example.taskmanager.task_fragments.TaskNotificationReceiver;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class NotificationUtils {

    // Programa las notificaciones de alarma para la tarea
    public static void scheduleTaskNotifications(Context context, Task task) {
        // Calcula los intervalos de tiempo para las notificaciones
        long timeRemainingMillis = task.getDeadline().getTime() - System.currentTimeMillis();
        long oneHourBeforeMillis = TimeUnit.HOURS.toMillis(1);
        long oneDayBeforeMillis = TimeUnit.DAYS.toMillis(1);
        long oneWeekBeforeMillis = TimeUnit.DAYS.toMillis(7);

        // Verifica si se pueden programar las notificaciones según el tiempo restante
        if (timeRemainingMillis > oneHourBeforeMillis) {
            // Programa la notificación una hora antes de que se acabe la tarea
            scheduleNotification(context, task, timeRemainingMillis - oneHourBeforeMillis);
        }
        if (timeRemainingMillis > oneDayBeforeMillis) {
            // Programa la notificación un día antes de que se acabe la tarea
            scheduleNotification(context, task, timeRemainingMillis - oneDayBeforeMillis);
        }
        if (timeRemainingMillis > oneWeekBeforeMillis) {
            // Programa la notificación una semana antes de que se acabe la tarea
            scheduleNotification(context, task, timeRemainingMillis - oneWeekBeforeMillis);
        }
    }

    // Programa una notificación de alarma para una tarea en un tiempo específico
    private static void scheduleNotification(Context context, Task task, long delayMillis) {
        Intent notificationIntent = new Intent(context, TaskNotificationReceiver.class);
        notificationIntent.putExtra("taskName", task.getName());
        notificationIntent.putExtra("taskId", task.getId());
        notificationIntent.putExtra("taskStatus", task.getStatus());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                task.getId(),
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long triggerAtMillis = System.currentTimeMillis() + delayMillis;
        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            }
        }
    }

    // Elimina las notificaciones y alarmas asociadas a una tarea
    public static void cancelTaskNotifications(Context context, Task task) {
        // Cancela las alarmas asociadas a la tarea
        Intent alarmIntent = new Intent(context, TaskNotificationReceiver.class);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(
                context,
                task.getId(),
                alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(alarmPendingIntent);
        }

        // Elimina las notificaciones asociadas a la tarea
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancel(task.getId());
        }
    }

    // Muestra una notificación
    public static void showNotification(Context context, String title, String message) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            // Crea un canal de notificación (solo es necesario en dispositivos con Android 8.0 y superior)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("task_channel", "Tareas", NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            // Crea y configura la notificación
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "task_channel")
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.soft_border)
                    .setAutoCancel(true);

            // Muestra la notificación
            notificationManager.notify(0, builder.build());
        }
    }

    // Reproduce un sonido de notificación
    public static void playNotificationSound(Context context, boolean isComplete) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            AssetFileDescriptor fileDescriptor;
            if (isComplete) {
                fileDescriptor = context.getResources().openRawResourceFd(R.raw.complete);
            } else {
                fileDescriptor = context.getResources().openRawResourceFd(R.raw.fail);
            }

            if (fileDescriptor != null) {
                mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
                fileDescriptor.close();
                mediaPlayer.setOnPreparedListener(MediaPlayer::start);
                mediaPlayer.setOnCompletionListener(MediaPlayer::release);
                mediaPlayer.prepareAsync();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
