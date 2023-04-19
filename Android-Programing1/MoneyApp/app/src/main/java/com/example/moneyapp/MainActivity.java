package com.example.moneyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private RecyclerViewController adapter; // Adaptador personalizado para el RecyclerView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar el RecyclerView y configurar el adaptador
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        adapter = new RecyclerViewController();
        recyclerView.setAdapter(adapter);
        initializePairs();

        Button addButton = findViewById(R.id.addButton);
        Button removeButton = findViewById(R.id.removeButton);
        FloatingActionButton mostrarGastosButton = findViewById(R.id.mostrar_gastos);

        removeButton.setOnClickListener(v -> removePair());
        addButton.setOnClickListener( v -> addPair());
        mostrarGastosButton.setOnClickListener(v -> calcularYEnviar());
    }

    // Método para inicializar la lista con 4 pares automáticamente
    private void initializePairs() {
        for (int i = 1; i <= 4; i++) {
            String newItem = "Pareja " + i;
            adapter.addItem(newItem);
        }
    }

    private void addPair() {
        String newItem = "Pareja " + (adapter.getItemCount() + 1);
        adapter.addItem(newItem);
    }

    private void removePair() {
        adapter.removePair();
    }

    private void calcularYEnviar() {
        Intent intent = new Intent(MainActivity.this, MostrarGastos.class);
        StringBuilder data = new StringBuilder();
        for (int i = 0; i < adapter.getItemCount(); i++) {
            data.append(adapter.getData().get(i)).append(" - 1\n");
            data.append(adapter.getData().get(i)).append(" - 2\n");
        }
        intent.putExtra("data", data.toString());
        startActivity(intent);
    }
}