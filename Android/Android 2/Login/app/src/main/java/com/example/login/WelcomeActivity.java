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
        String username = getIntent().getStringExtra("username");
        DatabaseController db = new DatabaseController(this);
        String genre = db.getUserGenre(username);

        // Revisamos si es femenino o masculino
        if (genre.charAt(0) == 'f'){
            welcomeTextView.setText(String.format("Bienvenida, %s", username));
        }else{
            welcomeTextView.setText(String.format("Bienvenido, %s", username));
        }

        // Botones
        Button logoutButton = findViewById(R.id.logoutButton);
        Button themeButton = findViewById(R.id.themeSwitchBut);
        logoutButton.setOnClickListener(v -> finish());
        themeButton.setOnClickListener(v -> ThemeController.switchTheme(this));

        }

    }
