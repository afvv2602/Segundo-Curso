package com.example.multimedia;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.multimedia.fragments.PhotoFragment;
import com.example.multimedia.fragments.VideoFragment;

public class CameraActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        Button recordVideoButton = findViewById(R.id.button_record_video);
        Button takePhotoButton = findViewById(R.id.button_take_photo);
        fragmentManager = getSupportFragmentManager();

        takePhotoButton.setOnClickListener(v -> dispatchTakePictureIntent());
        recordVideoButton.setOnClickListener(v -> dispatchTakeVideoIntent());
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            takePictureLauncher.launch(takePictureIntent);
        }
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            recordVideoLauncher.launch(takeVideoIntent);
        }
    }

    private final ActivityResultLauncher<Intent> takePictureLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    loadPhotoFragment(imageBitmap); // Modificado para cargar el fragmento de foto
                }
            });

    private final ActivityResultLauncher<Intent> recordVideoLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    Uri videoUri = data.getData();
                    loadVideoFragment(videoUri); // Modificado para cargar el fragmento de video
                }
            });

    private void loadPhotoFragment(Bitmap imageBitmap) {
        PhotoFragment photoFragment = new PhotoFragment(imageBitmap);
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, photoFragment)
                .addToBackStack(null)
                .commit();
    }

    private void loadVideoFragment(Uri videoUri) {
        VideoFragment videoFragment = new VideoFragment(videoUri);
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, videoFragment)
                .addToBackStack(null)
                .commit();
    }

    private void closeCurrentFragment() {
        fragmentManager.popBackStack();
    }
}
