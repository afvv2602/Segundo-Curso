package com.example.taskmanager.utils;

import android.util.Log;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DuplicateUtils {

    // Calcula el tiempo restante para la fecha limite de la tarea
    public static String remainingTime(Date deadline) {
        Date currentDate = new Date();
        long differenceMillis = deadline.getTime() - currentDate.getTime();
        long differenceMinutes = TimeUnit.MILLISECONDS.toMinutes(differenceMillis);
        long minutesInDay = TimeUnit.DAYS.toMinutes(1);
        long remainingDays = differenceMinutes / minutesInDay;
        long remainingHours = (differenceMinutes % minutesInDay) / 60;
        long remainingMinutes = differenceMinutes % 60;
        String remainingTime;

        if (differenceMillis <= 0) {
            remainingTime = "Se ha acabado el tiempo";
        } else if (remainingDays > 0) {
            remainingTime = "Quedan: " + remainingDays + " días";
        } else if (remainingHours > 0) {
            remainingTime = "Quedan: " + remainingHours + " horas";
        } else {
            remainingTime = "Quedan: " + remainingMinutes + " minutos";
        }
        return remainingTime;
    }

    // Misma funcion pero recibiendo el tiempo en milisegundos
    public static String getRemainingTimeText(long remainingTimeMillis) {
        long differenceMinutes = TimeUnit.MILLISECONDS.toMinutes(remainingTimeMillis);
        long minutesInDay = TimeUnit.DAYS.toMinutes(1);
        long remainingDays = differenceMinutes / minutesInDay;
        long remainingHours = (differenceMinutes % minutesInDay) / 60;
        long remainingMinutes = differenceMinutes % 60;

        String remainingTime;

        if (remainingTimeMillis <= 0) {
            remainingTime = "Se ha acabado el tiempo";
        } else if (remainingDays > 0) {
            remainingTime = "Quedan: " + remainingDays + " días";
        } else if (remainingHours > 0) {
            remainingTime = "Quedan: " + remainingHours + " horas";
        } else {
            remainingTime = "Quedan: " + remainingMinutes + " minutos";
        }
        return remainingTime;
    }



}
