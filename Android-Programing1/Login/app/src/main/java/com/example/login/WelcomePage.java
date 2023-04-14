package com.example.login;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomePage extends AppCompatActivity {

    private TextView welcomeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);

        welcomeTextView = findViewById(R.id.welcomeTextView);

        String username = getIntent().getStringExtra("username");
        welcomeTextView.setText(String.format("Welcome, %s", username));
    }
}
