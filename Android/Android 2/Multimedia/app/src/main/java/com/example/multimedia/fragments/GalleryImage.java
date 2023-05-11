package com.example.multimedia.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.multimedia.R;

import java.io.File;
import java.util.ArrayList;

public class GalleryImage extends BaseAdapter {

    private Context context;
    private ArrayList<File> imageFiles;
    private int width;
    private int height;

    public GalleryImage(Context context, ArrayList<File> imageFiles) {
        this.context = context;
        this.imageFiles = imageFiles;
        this.width = 150;
        this.height = 200;
    }

    @Override
    public int getCount() {
        return imageFiles.size();
    }

    @Override
    public Object getItem(int position) {
        return imageFiles.get(position);
    }

    @Override
        public long getItemId(int position) {
        return position;
    }


    // Metodo para obtener la vista de cada elemento en la lista de archivos de imagen.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.gallery_image, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.gallery_image);

        // Reduce el tamaño de la imagen antes de cargarla
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFiles.get(position).getAbsolutePath(), options);

        // Calcula el factor de escala
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;

        if (height > this.height || width > this.width) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) >= this.height && (halfWidth / inSampleSize) >= this.width) {
                inSampleSize *= 2;
            }
        }

        // Decodifica la imagen con el factor de escala calculado
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(imageFiles.get(position).getAbsolutePath(), options);
        imageView.setImageBitmap(bitmap);

        return convertView;
    }
}
