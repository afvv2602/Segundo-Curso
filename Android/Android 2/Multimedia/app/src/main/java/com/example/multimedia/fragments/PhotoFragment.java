package com.example.multimedia.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.multimedia.R;

public class PhotoFragment extends Fragment {

    private Bitmap imageBitmap;
    private FragmentManager fragmentManager;
    private Button closeBtn;

    public PhotoFragment(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.photo_fragment, container, false);
        ImageView imageView = rootView.findViewById(R.id.imageView);
        imageView.setImageBitmap(imageBitmap);
        fragmentManager = getParentFragmentManager();
        closeBtn = rootView.findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(v -> fragmentManager.popBackStack());
        return rootView;
    }
}