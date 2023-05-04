package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText forgotUsernameEditText;
    private Button recoverButton,backToLoginButton;

    DatabaseController db;

    private String username;
    private TextView recoveredPasswordTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView (R.layout.forgot_password_activity);

        forgotUsernameEditText = findViewById(R.id.forgotUsernameEditText);
        recoverButton = findViewById(R.id.recoverButton);
        backToLoginButton = findViewById(R.id.backToLoginButton);
        recoveredPasswordTextView = findViewById(R.id.recoveredPasswordTextView);
        Button themeButton = findViewById(R.id.themeSwitchBut);
        db = new DatabaseController(this);
        // Recupera el nombre de usuario y establece el valor en el campo de texto
        username = getIntent().getStringExtra("username");
        if (username != null && !username.isEmpty()) {
            forgotUsernameEditText.setText(String.format("Username:  %s", username));
        }

        // Botones
        themeButton.setOnClickListener(v -> ThemeController.switchTheme(this));
        recoverButton.setOnClickListener(v -> RecoverPass());
        backToLoginButton.setOnClickListener(v -> BackToLogin());
    }

    private void RecoverPass(){
        if (username.isEmpty()){
            username = ((EditText) findViewById(R.id.forgotUsernameEditText)).getText().toString();
        }
        String password = db.getPassword(username);
        if (!password.isEmpty() && password != null) {
            recoveredPasswordTextView.setText(String.format("Contraseña: %s", password));
        } else {
            recoveredPasswordTextView.setText("Error: Usuario no encontrado");
        }
    }

    private void BackToLogin() {
        Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
