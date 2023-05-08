package com.example.libreriaroom;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.libreriaroom.user.LoginFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.LoginFragment, new LoginFragment())
                .commit();
    }
}
