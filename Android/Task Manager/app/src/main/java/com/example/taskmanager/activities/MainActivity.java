package com.example.taskmanager.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taskmanager.R;
import com.example.taskmanager.user_fragments.LoginFragment;
import com.example.taskmanager.user_fragments.RegisterFragment;

public class MainActivity extends AppCompatActivity implements NavigationInterface {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.LoginFragment, new LoginFragment())
                .commit();
    }

    @Override
    public void navigateToRegister() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.LoginFragment, new RegisterFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void navigateToPrincipalActivity(String username) {
        Intent intent = new Intent(this, PrincipalActivity.class);
        intent.putExtra("username",username);
        startActivity(intent);
    }
}
