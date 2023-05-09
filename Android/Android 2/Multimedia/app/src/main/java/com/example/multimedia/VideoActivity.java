package com.example.multimedia;

import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;

public class VideoActivity extends AppCompatActivity {

    private VideoView videoView;
    private TextView videoTitle;
    private ListView listView;

    private Button closeButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        videoView = findViewById(R.id.videoView);
        videoTitle = findViewById(R.id.textView_video_title);
        listView = findViewById(R.id.listView_videos);

        closeButton = findViewById(R.id.closeVideoButton);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        listVideosFromFolder();
        closeButton.setOnClickListener(v -> changeAppear());

    }

    private void listVideosFromFolder() {
        AssetManager assetManager = getAssets();
        try {
            // Obtiene la lista de archivos de video en la carpeta "videos"
            String[] videoFiles = assetManager.list("videos_muestra");

            if (videoFiles != null) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, videoFiles);

                listView.setAdapter(adapter);
                listView.setOnItemClickListener((parent, view, position, id) -> {
                    String videoPath = "file:///android_asset/videos_muestra/" + videoFiles[position];
                    Uri videoUri = Uri.parse(videoPath);
                    videoView.setVideoURI(videoUri);
                    videoView.start();
                    changeAppear(videoFiles[position]);
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void changeAppear(String... title) {
        if(title.length>0){
            videoView.setVisibility(View.VISIBLE);
            closeButton.setVisibility(View.VISIBLE);
            videoTitle.setText(title[0]);
            videoTitle.setVisibility(View.VISIBLE);
        }else{
            videoView.stopPlayback();
            videoView.setVisibility(View.GONE);
            closeButton.setVisibility(View.GONE);
            videoTitle.setVisibility(View.GONE);
        }
    }
}