package com.example.moneyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerViewController adapter; // Adaptador personalizado para el RecyclerView

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Inicializar el RecyclerView
        recyclerView = findViewById(R.id.recyclerView);

        //Asignar el adaptador al RecyclerView antes de inicializar el adaptador con la referencia al RecyclerView
        adapter = new RecyclerViewController();
        recyclerView.setAdapter(adapter);

        //Inicializar el adaptador con la referencia al RecyclerView
        adapter.setRecyclerView(recyclerView);

        //Cargamos 4 pares directamente
        initializePairs();

        Button addButton = findViewById(R.id.addButton);
        Button removeButton = findViewById(R.id.removeButton);
        FloatingActionButton mostrarGastosButton = findViewById(R.id.mostrar_gastos);

        removeButton.setOnClickListener(v -> adapter.removePair());
        addButton.setOnClickListener( v -> adapter.addItem());
        mostrarGastosButton.setOnClickListener(v -> mostrarGastos());
    }

    //Metodo para inicializar la lista con 4 pares automaticamente
    private void initializePairs() {
        for (int i = 1; i <= 4; i++) {
            adapter.addItem();
        }
    }

    //Metodo para calcular y enviar los datos de los EditText a la actividad MostrarGastos
    private void mostrarGastos() {
        List<Persona> updatedData = adapter.getUpdatedData(); // Obtener los datos actualizados de los EditText utilizando el metodo getUpdatedData()

        if (validarCampos(updatedData)) {
            Intent intent = new Intent(MainActivity.this, MostrarGastos.class); // Crear un Intent para iniciar la actividad MostrarGastos
            intent.putExtra("list", (Serializable) updatedData);
            startActivity(intent); // Iniciar la actividad MostrarGastos con el Intent
        } else {
            Toast.makeText(MainActivity.this, "Por favor, ingrese un nombre en todos los campos.", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean validarCampos(List<Persona> dataList) {
        for (Persona persona : dataList) {
            if (persona.getNombre().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}