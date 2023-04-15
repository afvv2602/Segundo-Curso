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
    private Button recoverButton;

    private String username;
    private Button backToLoginButton;
    private TextView recoveredPasswordTextView;

    private HashMap<String, String> users = new HashMap<String, String>() {{
        put("admin", "root");
        put("root", "admin");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView (R.layout.forgot_password_activity);

        forgotUsernameEditText = findViewById(R.id.forgotUsernameEditText);
        recoverButton = findViewById(R.id.recoverButton);
        // Recupera el nombre de usuario y establece el valor en el campo de texto
        username = getIntent().getStringExtra("username");
        if (username != null && !username.isEmpty()) {
            forgotUsernameEditText.setText(String.format("Username:  %s", username));
        }

        backToLoginButton = findViewById(R.id.backToLoginButton);
        recoveredPasswordTextView = findViewById(R.id.recoveredPasswordTextView);

        recoverButton.setOnClickListener(v -> RecoverPass());
        backToLoginButton.setOnClickListener(v -> BackToLogin());
    }

    private void RecoverPass(){
        if (username.isEmpty()){
            username = ((EditText) findViewById(R.id.forgotUsernameEditText)).getText().toString();
        }
        String password = users.get(username);

        if (password != null) {
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
