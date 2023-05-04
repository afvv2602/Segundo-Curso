package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ThemeController.applySavedTheme(this);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);


        // Botones
        Button loginButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.registerButton);
        Button themeButton = findViewById(R.id.themeSwitchBut);
        TextView forgotPass  = findViewById(R.id.forgotPasswordTextView);

        loginButton.setOnClickListener(v -> login());
        registerButton.setOnClickListener(v -> register());
        forgotPass.setOnClickListener(v -> forgotPass());
        themeButton.setOnClickListener(v -> ThemeController.switchTheme(this));

    }

    private void login(){
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        DatabaseController db = new DatabaseController(this);
        if(db.loginUser(username,password)){
            Intent intent = new Intent(this,WelcomeActivity.class);
            intent.putExtra("username",username);
            startActivity(intent);
        }else{
            UtilsController.showMessage(this,"Algo ha ido mal, ¿Has olvidado tu contraseña?");
        }
    }

    private void register(){
        Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(registerIntent);
        finish();
    }

    private void forgotPass(){
        Intent forgotPasswordIntent = new Intent(MainActivity.this, ForgotPasswordActivity.class);
        String username = usernameEditText.getText().toString();
        forgotPasswordIntent.putExtra("username", username);
        startActivity(forgotPasswordIntent);
        finish();
    }

}