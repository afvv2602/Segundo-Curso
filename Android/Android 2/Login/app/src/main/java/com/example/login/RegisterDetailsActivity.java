package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;


public class RegisterDetailsActivity extends AppCompatActivity {

    private TextView userTextView;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_details_activity);

        userTextView = findViewById(R.id.userTextView);
        Button themeButton = findViewById(R.id.themeSwitchBut);
        Button backButton = findViewById(R.id.backButton);
        username = getIntent().getStringExtra("username");

        // Muestra los valores de los campos en los TextViews
        userTextView.setText(String.format(" El usuario %s ha sido creado.", username));
        themeButton.setOnClickListener(v -> ThemeController.switchTheme(this));
        backButton.setOnClickListener(v -> welcome());
    }

    private void welcome(){
        Intent intent = new Intent(this,WelcomeActivity.class);
        intent.putExtra("username",username);
        startActivity(intent);
        finish();
    }
}
