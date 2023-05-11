package com.example.multimedia;


import android.Manifest;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;

public class AudioActivity extends AppCompatActivity {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private Button recordButton, stopButton, playButton;
    private String audioFilePath;
    private boolean isRecording;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        recordButton = findViewById(R.id.recordBtn);
        stopButton = findViewById(R.id.stopBtn);
        playButton = findViewById(R.id.playBtn);

        audioFilePath = getExternalFilesDir(null).getAbsolutePath() + "/test.3gp";
        isRecording = false;

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_RECORD_AUDIO_PERMISSION);

        recordButton.setOnClickListener(v -> {
            if (isRecording) {
                stopRecording();
            } else {
                startRecording();
            }
            isRecording = !isRecording;
        });

        stopButton.setOnClickListener(v -> stopPlaying());

        playButton.setOnClickListener(v -> startPlaying());
    }

    private void startRecording() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(audioFilePath);
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stopButton.setVisibility(View.GONE);
        recordButton.setText("Detener");
    }

    private void stopRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
        stopButton.setVisibility(View.VISIBLE);
        recordButton.setText("Grabar");
    }

    private void startPlaying() {
        mediaPlayer = new MediaPlayer();
        playButton.setVisibility(View.GONE);
        recordButton.setVisibility(View.GONE);
        try {
            mediaPlayer.setDataSource(audioFilePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopPlaying() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            playButton.setVisibility(View.VISIBLE);
            recordButton.setVisibility(View.VISIBLE);
        }
    }
}
