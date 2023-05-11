package com.example.multimedia;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.Manifest;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.multimedia.fragments.GalleryImage;

import java.io.File;
import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    private GridView gridView;
    private ImageView imageView;
    private ArrayList<File> imageFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        gridView = findViewById(R.id.gridView);
        imageView = findViewById(R.id.imageView);

        // Revisamos los permisos para ver si podemos acceder a la sd
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        } else {
            setUpGallery();
        }
    }

    // Si se han aceptado los permisos se carga la galeria
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setUpGallery();
            } else {
                Toast.makeText(this, "No se ha podido acceder a la sd debido a que se han denegado los permisos", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void setUpGallery() {
        imageFiles = listImages();
        GalleryImage adapter = new GalleryImage(this, imageFiles);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(GalleryActivity.this);
            // Mostramos la imagen en un imageView dentro de dialogo
            ImageView imageView = new ImageView(GalleryActivity.this);
            imageView.setImageURI(Uri.fromFile(imageFiles.get(position)));
            builder.setView(imageView);
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private ArrayList<File> listImages() {
        File imageFolder = new File("/sdcard/MultimediaPictures");
        File[] files = imageFolder.listFiles();
        ArrayList<File> imageFiles = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                if (file.getName().toLowerCase().endsWith(".jpg") || file.getName().toLowerCase().endsWith(".png")) {
                    imageFiles.add(file);
                }
            }
        }
        return imageFiles; // Cogemos todos los archivos que terminen en jpg y png del directorio
    }
}
