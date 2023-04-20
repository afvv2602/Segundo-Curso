package com.example.moneyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

        removeButton.setOnClickListener(v -> removePair());
        addButton.setOnClickListener( v -> addPair());
        mostrarGastosButton.setOnClickListener(v -> calcularYEnviar());
    }

    //Metodo para inicializar la lista con 4 pares automaticamente
    private void initializePairs() {
        for (int i = 1; i <= 4; i++) {
            String newItem = "";
            adapter.addItem(newItem);
        }
    }

    private void addPair() {
        String newItem = "";
        adapter.addItem(newItem);
    }

    private void removePair() {
        adapter.removePair();
    }

    //Metodo para calcular y enviar los datos de los EditText a la actividad MostrarGastos
    private void calcularYEnviar() {
        Intent intent = new Intent(MainActivity.this, MostrarGastos.class); // Crear un Intent para iniciar la actividad MostrarGastos
        StringBuilder data = new StringBuilder(); // Crear un StringBuilder para almacenar los datos como un solo String
        List<String> updatedData = adapter.getUpdatedData(); // Obtener los datos actualizados de los EditText utilizando el método getUpdatedData()
        for (int i = 0; i < updatedData.size(); i++) { // Iterar sobre todos los elementos de la lista updatedData
            data.append(updatedData.get(i)); // Agregar el elemento actual a 'data'
            Log.v("", "###" + updatedData.get(i)); // Imprimir el elemento actual en el Logcat (para depuración)
        }
        
        intent.putExtra("data", data.toString()); // Agregar el String 'data' como extra en el Intent
        startActivity(intent); // Iniciar la actividad MostrarGastos con el Intent
    }
}