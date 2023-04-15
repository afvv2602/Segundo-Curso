package com.example.chronometer;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    private TextView tvChronometer;
    private Button btnStart, btnReset;
    private boolean isRunning = false;
    private DecimalFormat df = new DecimalFormat("00");

    // Variables para enlazar el servicio Chronometer
    private Chronometer chronometer;
    private boolean isBound = false;

    // Objeto ServiceConnection para manejar la conexión entre el servicio y la actividad
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Chronometer.LocalBinder binder = (Chronometer.LocalBinder) service;
            chronometer = binder.getService();
            chronometer.setActivityListener(MainActivity.this);
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvChronometer = findViewById(R.id.tv_chronometer);
        btnStart = findViewById(R.id.btn_start);
        btnReset = findViewById(R.id.btn_reset);

        // Establecer el comportamiento al hacer clic en el botón de inicio
        btnStart.setOnClickListener(v -> playPause());

        // Establecer el comportamiento al hacer clic en el botón de reinicio
        btnReset.setOnClickListener(v -> resetChronometer());

        // Enlazar el servicio Chronometer cuando se crea la actividad
        Intent intent = new Intent(this, Chronometer.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }
    private void playPause(){
        if (!isRunning) {
            if (chronometer != null) {
                chronometer.startCronometer();
            }
            isRunning = true;
            btnStart.setText("Pause"); // Cambiar el texto del botón a "Pause"
        } else {
            if (chronometer != null) {
                chronometer.stopCronometer();
            }
            isRunning = false;
            btnStart.setText("Start"); // Cambiar el texto del botón a "Start"
        }
    }

    // Método para actualizar la UI del cronómetro
    public void updateChronometer(double time) {
        int hours = (int) (time / 3600);
        int minutes = (int) ((time % 3600) / 60);
        int seconds = (int) (time % 60);
        int milliseconds = (int) ((time - (int) time) * 100);
        String formattedTime = df.format(hours) + ":" + df.format(minutes) + ":" + df.format(seconds) + "." + df.format(milliseconds);
        tvChronometer.setText(formattedTime);
    }

    // Método para reiniciar el cronómetro
    private void resetChronometer() {
        if (!isRunning) {
            tvChronometer.setText("00:00:00");
            if (chronometer != null) {
                chronometer.resetTime();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Desenlazar el servicio Chronometer cuando se destruye la actividad
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
        chronometer.stopCronometer();
    }


}
