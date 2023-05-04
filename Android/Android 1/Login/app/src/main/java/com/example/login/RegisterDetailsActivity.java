package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class RegisterDetailsActivity extends AppCompatActivity {

    private TextView userTextView;
    private TextView emailTextView;
    private TextView phoneTextView;
    private TextView genreTextView;
    private TextView conditionsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_details_activity);

        userTextView = findViewById(R.id.userTextView);
        emailTextView = findViewById(R.id.emailTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        genreTextView = findViewById(R.id.genreTextView);
        conditionsTextView = findViewById(R.id.conditionsTextView);
        Button themeButton = findViewById(R.id.themeSwitchBut);
        String user = getIntent().getStringExtra("user");
        String email = getIntent().getStringExtra("email");
        String phone = getIntent().getStringExtra("phone");
        String genre = getIntent().getStringExtra("genre");
        boolean conditionsAccepted = getIntent().getBooleanExtra("conditionsAccepted", false);

        // Muestra los valores de los campos en los TextViews
        userTextView.setText(String.format("Username:  %s", user));
        emailTextView.setText(String.format("Email: %s", email));
        phoneTextView.setText(String.format("Phone:  %s", phone));
        genreTextView.setText(String.format("Genre:  %s", genre));
        conditionsTextView.setText(conditionsAccepted ? "Condiciones aceptadas" : "Condiciones no aceptadas");

        themeButton.setOnClickListener(v -> ThemeController.switchTheme(this));
    }
}
