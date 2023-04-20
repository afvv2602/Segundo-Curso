package com.example.moneyapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewController extends RecyclerView.Adapter<RecyclerViewController.ViewHolder>{

    // Lista de datos que se mostrarán en las parejas de EditText
    private List<String> data;

    public RecyclerViewController() {
        data = new ArrayList<>(); // Inicializar la lista de datos vacía
    }

    // Este método crea una nueva vista de pareja de TextViews inflándola desde el archivo XML
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pareja_edittext, parent, false);
        return new ViewHolder(view);
    }

    // Este método vincula los datos a las vistas de cada pareja de TextViews
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String text = data.get(position);
        holder.nombre.setText(text + " - 1");
        holder.cantidad.setText(text + " - 2");
    }

    // Este método devuelve la cantidad de elementos en la lista de datos
    @Override
    public int getItemCount() {
        return data.size();
    }

    // Este método agrega un nuevo elemento a la lista de datos y notifica al RecyclerView para que actualice la vista
    public void addItem(String item) {
        data.add(item);
        notifyItemInserted(data.size() - 1);
    }

    // Esta clase interna representa la vista de una pareja de EditText y contiene referencias a las vistas
    static class ViewHolder extends RecyclerView.ViewHolder {
        EditText nombre;
        EditText cantidad;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombre);
            cantidad = itemView.findViewById(R.id.cantidad);
        }
    }

    // Este método elimina un elemento de la lista de datos y notifica al RecyclerView para que actualice la vista
    public void removePair() {
        if (!data.isEmpty()) {
            data.remove(data.size() - 1);
            notifyItemRemoved(data.size());
        }
    }

    // Devuelve la lista
    public List<String> getData() {
        return data;
    }
}
