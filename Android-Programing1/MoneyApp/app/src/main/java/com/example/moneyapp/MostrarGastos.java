package com.example.moneyapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class MostrarGastos extends AppCompatActivity {

    private LinearLayout linearLayout;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mostrar_gastos);

        linearLayout = findViewById(R.id.linear_layout);
        back = findViewById(R.id.backButton);

        List<Persona> Personas = (List<Persona>) getIntent().getSerializableExtra("list");
        List<String> Result =new ArrayList<String>();
        if (Personas!=null) {
            for (Persona actual : Personas) {
                Result.add(actual.getNombre() + " || " + actual.getDinero());
            }
            createTextViews(Result);
        }

        back.setOnClickListener(v -> backToMainActivity());
    }

    private void backToMainActivity() {
        Intent intent = new Intent(MostrarGastos.this, MainActivity.class);
        startActivity(intent); // Iniciar la actividad MostrarGastos con el Intent
    }

    private void createTextViews(List<String> elements) {
        for (String element : elements) {
            TextView textView = new TextView(this);
            textView.setText(element);
            textView.setTextSize(20); // Define el tamaño del texto aquí
            linearLayout.addView(textView);
        }
    }
}
