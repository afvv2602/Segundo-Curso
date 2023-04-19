package com.example.moneyapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MostrarGastos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mostrar_gastos);

        TextView displayTextView = findViewById(R.id.displayTextView);
        String data = getIntent().getStringExtra("data");
        displayTextView.setText(data);
    }
}
