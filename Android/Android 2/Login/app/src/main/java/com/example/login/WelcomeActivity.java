package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

        //Mostramos la ultima fecha de inicio de sesion
        String ultimoInicio = db.getLastLoginTime(username);
        if (ultimoInicio != null) {
            TextView lastLoginTextView = findViewById(R.id.lastLoginTv);
            lastLoginTextView.setText(String.format("Última conexion: %s", ultimoInicio));
        }

        //Añadimos la hora de inicio de sesion
        Date currentDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentTime = formatter.format(currentDate);
        db.storeLoginTime(username, currentTime);

        // Botones
        Button logoutButton = findViewById(R.id.logoutButton);
        Button themeButton = findViewById(R.id.themeSwitchBut);
        Button profileButton = findViewById(R.id.profileButton);

        logoutButton.setOnClickListener(v -> finish());
        profileButton.setOnClickListener(v -> perfil());
        themeButton.setOnClickListener(v -> ThemeController.switchTheme(this));
        }

    private void perfil() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("username",getIntent().getStringExtra("username"));
        startActivity(intent);
        finish();
    }

}
