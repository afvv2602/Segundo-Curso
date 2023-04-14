package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;

    // Usuarios y contraseñas almacenados en código
    private final HashMap<String, String> users = new HashMap<String, String>() {{
        put("admin", "root");
        put("root", "admin");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        Button loginButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.registerButton);
        TextView forgotPass  = findViewById(R.id.forgotPasswordTextView);

        loginButton.setOnClickListener(v -> login());
        registerButton.setOnClickListener(v -> register());
        forgotPass.setOnClickListener(v -> forgotPass());
    }

    private boolean isValidUser(String username, String password) {
        String storedPassword = users.get(username);
        return storedPassword != null && storedPassword.equals(password);
    }

    private void login(){
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (isValidUser(username, password)) {
            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        } else {
            Toast.makeText(MainActivity.this, "Usuario o contraseña inválidos", Toast.LENGTH_SHORT).show();
        }
    }

    private void register(){
        Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(registerIntent);
    }

    private void forgotPass(){
        Intent forgotPasswordIntent = new Intent(MainActivity.this, ForgotPasswordActivity.class);
        String username = usernameEditText.getText().toString();
        forgotPasswordIntent.putExtra("username", username);
        startActivity(forgotPasswordIntent);
    }
}