package com.example.taskmanager.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taskmanager.R;
import com.example.taskmanager.user_fragments.LoginFragment;
import com.example.taskmanager.user_fragments.RegisterFragment;

public class MainActivity extends AppCompatActivity implements NavigationInterface {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Agrega el fragmento de inicio de sesion al contenedor principal del activity
        getSupportFragmentManager().beginTransaction()
                .add(R.id.LoginFragment, new LoginFragment())
                .commit();
    }

    @Override
    public void navigateToRegister() {
        // Reemplaza el fragmento de inicio de sesion por el fragmento de registro y se agrega a la pila
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.LoginFragment, new RegisterFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void navigateToPrincipalActivity(String username) {
        // Navega a la actividad PrincipalActivity y pasa el nombre de usuario como intent extra
        Intent intent = new Intent(this, PrincipalActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}
