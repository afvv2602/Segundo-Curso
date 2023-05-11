package com.example.multimedia.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.multimedia.R;

public class VideoFragment extends Fragment {

    private Uri videoUri;
    private FragmentManager fragmentManager;
    private Button closeBtn;

    public VideoFragment(Uri videoUri) {
        this.videoUri = videoUri;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.video_fragment, container, false);
        VideoView videoView = rootView.findViewById(R.id.videoView);
        videoView.setVideoURI(videoUri);
        MediaController mediaController = new MediaController(getContext());
        mediaController.setMediaPlayer(videoView);
        videoView.setMediaController(mediaController);
        videoView.requestFocus();
        videoView.start();
        fragmentManager = getParentFragmentManager();
        closeBtn = rootView.findViewById(R.id.closeVideoBtn);
        closeBtn.setOnClickListener(v -> fragmentManager.popBackStack());
        return rootView;
    }
}
