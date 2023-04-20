package com.example.moneyapp;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewController extends RecyclerView.Adapter<RecyclerViewController.ViewHolder> {

    // Lista de datos que se mostrarán en las parejas de EditText
    private List<Persona> data;
    private RecyclerView recyclerView;

    public RecyclerViewController() {
        data = new ArrayList<>(); // Inicializar la lista de datos vacía
    }

    // Este metodo crea una nueva vista de pareja de TextViews añadiendola desde el archivo XML
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pareja_edittext, parent, false);
        return new ViewHolder(view);
    }

    // Este metodo vincula los datos a las vistas de cada pareja de TextViews
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Persona persona = data.get(position);
        holder.nombre.setText(persona.getNombre());
        holder.cantidad.setText(persona.getDinero() == 0 ? "" : String.valueOf(persona.getDinero()));
    }

    // Este metodo devuelve la cantidad de elementos en la lista de datos
    @Override
    public int getItemCount() {
        return data.size();
    }

    // Este metodo agrega un nuevo elemento a la lista de datos y notifica al RecyclerView para que actualice la vista
    public void addItem() {
        data.add(new Persona("", 0));
        notifyItemInserted(data.size() - 1);
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    // Este metodo elimina un elemento de la lista de datos y notifica al RecyclerView para que actualice la vista
    public void removePair() {
        if (!data.isEmpty()) {
            data.remove(data.size() - 1);
            notifyItemRemoved(data.size());
        }
    }

    // Metodo para obtener la lista actualizada de datos de los EditText
    public List<Persona> getUpdatedData() {
        List<Persona> updatedData = new ArrayList<>(); // Crear una lista vacía para almacenar los datos actualizados
        for (int i = 0; i < getItemCount(); i++) { // Iterar sobre todos los elementos del RecyclerView
            ViewHolder viewHolder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(i); // Obtener el ViewHolder en la posicion 'i'
            Persona p;
            if (viewHolder != null) { // Verificar si el ViewHolder no es nulo
                String nombre = viewHolder.nombre.getText().toString(); // Obtener el texto del EditText 'nombre'
                String cantidad = viewHolder.cantidad.getText().toString(); // Obtener el texto del EditText 'cantidad'
                if (cantidad.isEmpty()) {
                    p = new Persona(nombre, 0); // Creamos una persona y la añadimos a la lista
                } else {
                    p = new Persona(nombre, Double.parseDouble(cantidad)); // Creamos una persona y la añadimos a la lista
                }
                updatedData.add(p);
            } else {
                updatedData.add(data.get(i));
            }
        }
        return updatedData; // Devolver la lista actualizada de datos
    }

    // Esta clase representa la vista de una pareja de EditText y contiene referencias a las vistas
    class ViewHolder extends RecyclerView.ViewHolder {
        EditText nombre;
        EditText cantidad;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombre);
            cantidad = itemView.findViewById(R.id.cantidad);

            //Este apartado se encarga de guardar el valor del nombre en la lista data, y tambien la posicion en la que se encuentra
            nombre.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    data.get(getAdapterPosition()).setNombre(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            //Este apartado se encarga de guardar el valor del nombre en la lista data, y tambien la posicion en la que se encuentra
            cantidad.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    double dinero = s.toString().isEmpty() ? 0 : Double.parseDouble(s.toString());
                    data.get(getAdapterPosition()).setDinero(dinero);
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
    }

}

