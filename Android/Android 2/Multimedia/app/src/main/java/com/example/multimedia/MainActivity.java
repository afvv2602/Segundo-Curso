package com.example.multimedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button cameraBtn,videoBtn,galleryBtn,recorderBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cameraBtn = findViewById(R.id.cameraBtn);
        videoBtn = findViewById(R.id.videoPlayerBtn);
        galleryBtn = findViewById(R.id.galleryBtn);
        recorderBtn = findViewById(R.id.recorderBtn);

        cameraBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CameraActivity.class);
            startActivity(intent);
        });

        videoBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, VideoActivity.class);
            startActivity(intent);
        });

        galleryBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
            startActivity(intent);
        });

        recorderBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AudioActivity.class);
            startActivity(intent);
        });
    }
}