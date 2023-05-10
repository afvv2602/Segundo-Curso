package com.example.multimedia;

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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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

        listVideosFromRaw();
        closeButton.setOnClickListener(v -> changeAppear());
    }

    // Crea un list view con el titulo de cada video
    // Los videos deberan de ir en la carpeta res/raw
    private void listVideosFromRaw() {
        List<String> videoFilesList = listMp4FilesFromRaw();
        if (videoFilesList != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, videoFilesList);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener((parent, view, position, id) -> {
                String videoFileNameComplete = videoFilesList.get(position) + "_mp4"; // Agregamos la extension "_mp4" de nuevo
                int videoResourceId = getResources().getIdentifier(videoFileNameComplete, "raw", getPackageName());
                Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + videoResourceId);
                videoView.setVideoURI(videoUri);
                videoView.start();
                changeAppear(videoFilesList.get(position));
            });
        }
    }

    // En esta funcion se recoge el nombre de cada video de la carpeta raw y se le quita la extension
    private List<String> listMp4FilesFromRaw() {
        Field[] fields = R.raw.class.getFields();
        List<String> mp4Files = new ArrayList<>();

        for (Field field : fields) {
            try {
                int resourceId = field.getInt(null);
                String fileName = getResources().getResourceEntryName(resourceId);
                if (fileName.endsWith("_mp4")) {
                    String displayName = fileName.substring(0, fileName.length() - 4); // Quitamos la extension "_mp4"
                    mp4Files.add(displayName);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return mp4Files;
    }

    private void changeAppear(String... title) {
        if (title.length > 0) {
            videoView.setVisibility(View.VISIBLE);
            closeButton.setVisibility(View.VISIBLE);
            videoTitle.setText(title[0]);
            videoTitle.setVisibility(View.VISIBLE);
        } else {
            videoView.stopPlayback();
            videoView.setVisibility(View.GONE);
            closeButton.setVisibility(View.GONE);
            videoTitle.setVisibility(View.GONE);
        }
    }
}
