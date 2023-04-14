package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
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

        //Llamamos a la funcion login y si el usuario y la contraseña estan en el hashmap entonces pasamos
        //A la siguiente actividad
        loginButton.setOnClickListener(v -> login());


    }

    private boolean isValidUser(String username, String password) {
        String storedPassword = users.get(username);
        return storedPassword != null && storedPassword.equals(password);
    }

    private void login(){
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (isValidUser(username, password)) {
            Intent intent = new Intent(MainActivity.this, WelcomePage.class);
            intent.putExtra("username", username);
            startActivity(intent);
        } else {
            Toast.makeText(MainActivity.this, "Usuario o contraseña inválidos", Toast.LENGTH_SHORT).show();
        }
    }
}