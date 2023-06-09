package com.example.taskmanager.utils;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.taskmanager.R;
import com.example.taskmanager.db.task.Task;
import com.example.taskmanager.db.task.TaskViewModel;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class NotificationUtils extends BroadcastReceiver {
    public static final String NOTIFICATION_CHANNEL_ID = "task_notification_channel";

    private TaskViewModel taskViewModel;

    public void setTaskViewModel(TaskViewModel taskViewModel) {
        this.taskViewModel = taskViewModel;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Crea el canal de notificación
        createNotificationChannel(context);

        String taskName = intent.getStringExtra("taskName");
        int taskStatus = intent.getIntExtra("taskStatus", 0);
        boolean isTaskCompleted = false;
        String notificationContent;

        // Configura el contenido de la notificación segun el estado de la tarea
        if (taskStatus == Task.Status.IN_PROGRESS.ordinal()) {
            notificationContent = String.format("Te queda %s para completar la tarea %s",
                    DuplicateUtils.getRemainingTimeText(intent.getLongExtra("taskRemainingTime", 0)), taskName);
        } else if (taskStatus == Task.Status.COMPLETED.ordinal()) {
            notificationContent = String.format("Tu tarea %s ha sido completada", taskName);
            isTaskCompleted = true;
        } else {
            notificationContent = "Hubo un problema con tu tarea";
        }

        // Crea la notificación
        Notification notification = createNotification(context, taskName, notificationContent, isTaskCompleted);

        // Muestra la notificación
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(0, notification);
        }
    }

    private void createNotificationChannel(Context context) {
        // Crea el canal de notificacion si la versión de Android es compatible
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "TaskChannel";
            String description = "Channel for task notification";

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    // Crea la notificacion con el icono y contenido correspondiente segun el estado de la tarea
    private Notification createNotification(Context context, String taskName, String notificationContent, boolean isTaskCompleted) {
        if (isTaskCompleted) {
            return new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.baseline_done_24)
                    .setContentTitle(taskName)
                    .setContentText(notificationContent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build();
        } else {
            return new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.baseline_warning_24)
                    .setContentTitle(taskName)
                    .setContentText(notificationContent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build();
        }
    }

    // Programa las notificaciones de la tarea en funcion del tiempo restante hasta la fecha limite
    public static void scheduleTaskNotifications(Context context, Task task) {
        long timeRemainingMillis = task.getDeadline().getTime() - System.currentTimeMillis();
        long oneHourBeforeMillis = TimeUnit.HOURS.toMillis(1);
        long oneDayBeforeMillis = TimeUnit.DAYS.toMillis(1);
        long oneWeekBeforeMillis = TimeUnit.DAYS.toMillis(7);

        if (timeRemainingMillis > oneHourBeforeMillis) {
            scheduleNotification(context, task, timeRemainingMillis - oneHourBeforeMillis);
        }
        if (timeRemainingMillis > oneDayBeforeMillis) {
            scheduleNotification(context, task, timeRemainingMillis - oneDayBeforeMillis);
        }
        if (timeRemainingMillis > oneWeekBeforeMillis) {
            scheduleNotification(context, task, timeRemainingMillis - oneWeekBeforeMillis);
        }
    }

    // Programa la notificacion utilizando el AlarmaManager
    public static void scheduleNotification(Context context, Task task, long delayMillis) {
        Intent notificationIntent = new Intent(context, NotificationUtils.class);
        notificationIntent.putExtra("taskName", task.getName());
        notificationIntent.putExtra("taskId", task.getId());
        notificationIntent.putExtra("taskStatus", task.getStatus().ordinal());
        notificationIntent.putExtra("taskRemainingTime", task.getDeadline().getTime() - System.currentTimeMillis());

        int requestCode = task.getId();

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
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

    // Cancela las notificaciones programadas y las notificaciones mostradas de una tarea
    public static void cancelTaskNotifications(Context context, Task task) {
        Intent alarmIntent = new Intent(context, NotificationUtils.class);
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

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancel(task.getId());
        }
    }

    // Muestra una notificacion simple
    public static void showNotification(Context context, String title, String message) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("task_channel", "Tareas", NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "task_channel")
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.baseline_warning_24)
                    .setAutoCancel(true);

            notificationManager.notify(0, builder.build());
        }
    }

    // Reproduce un sonido de notificacion segun si la tarea esta completada o no
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
