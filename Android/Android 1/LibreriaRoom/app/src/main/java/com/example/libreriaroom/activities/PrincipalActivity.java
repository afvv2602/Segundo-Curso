package com.example.libreriaroom.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.libreriaroom.R;
import com.example.libreriaroom.db.user.UserViewModel;

import org.w3c.dom.Text;


public class PrincipalActivity extends AppCompatActivity {
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_activity);
        username = getIntent().getStringExtra("username");
        TextView tv = findViewById(R.id.textView);
        if (username != null) {
            tv.setText("Bienvenido: "+username);
        }
    }
}

