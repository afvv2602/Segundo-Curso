package com.example.login;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);

        TextView welcomeTextView = findViewById(R.id.welcomeTextView);
        Button logoutButton = findViewById(R.id.logoutButton);
        String username = getIntent().getStringExtra("username");
        welcomeTextView.setText(String.format("Welcome, %s", username));
        Button themeButton = findViewById(R.id.themeSwitchBut);

        logoutButton.setOnClickListener(v -> logout());
        themeButton.setOnClickListener(v -> ThemeController.switchTheme(this));

        }

    private void logout(){
        finish();
    }

    }
