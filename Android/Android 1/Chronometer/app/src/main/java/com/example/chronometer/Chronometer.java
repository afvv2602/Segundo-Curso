package com.example.chronometer;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

public class Chronometer extends Service {
    private Timer timer = new Timer(); // Timer para controlar el cronómetro
    private Handler handler; // Manejador para actualizar la UI en el hilo principal
    private static final long INTERVAL = 10; // Intervalo de tiempo en milisegundos para actualizar el cronómetro
    private double time = 0; // Valor actual del tiempo del cronómetro
    public MainActivity activityListener; // Referencia a la actividad que escucha las actualizaciones del cronómetro

    private final IBinder binder = new LocalBinder(); // Binder para enlazar el servicio con la actividad

    public Chronometer() {
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (activityListener != null) {
                    activityListener.updateChronometer(time);
                }
            }
        };
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startCronometer(); // Iniciar el cronómetro al crear el servicio
        // Crear el manejador para actualizar la UI en el hilo principal
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (activityListener != null) {
                    activityListener.updateChronometer(time);
                }
            }
        };
    }

    @Override
    public void onDestroy() {
        stopCronometer(); // Detener el cronómetro al destruir el servicio
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder; // Devolver el binder para enlazar el servicio con la actividad
    }

    // Método para iniciar el cronómetro
    public void startCronometer() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                time += 0.01;
                handler.sendEmptyMessage(0);
                handler.postDelayed(this, INTERVAL);
            }
        }, INTERVAL);
    }

    // Método para detener el cronómetro
    public void stopCronometer() {
        handler.removeCallbacksAndMessages(null);
    }

    public void resetTime() {
        this.time = 0;
    }

    // Clase interna para el binder
    public class LocalBinder extends Binder {
        Chronometer getService() {
            return Chronometer.this;
        }
    }

    // Método para establecer la actividad que escuchará las actualizaciones del cronómetro
    public void setActivityListener(MainActivity actividad) {
        this.activityListener = actividad;
    }

}

