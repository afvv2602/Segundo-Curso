package com.example.moneyapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MostrarGastos extends AppCompatActivity {

    private LinearLayout linearLayout;
    private List<Persona> Personas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mostrar_gastos);

        linearLayout = findViewById(R.id.linear_layout);

        Personas = (List<Persona>) getIntent().getSerializableExtra("list");
        repartirGastos(Personas);
    }

    public void repartirGastos(List<Persona> Personas) {
        // Lista para almacenar los mensajes de resultado que se mostraran en TextViews mas adelante
        List<String> Resultado = new ArrayList<>();

        // Calcular el total gastado por todas las personas
        double totalGastado = 0;
        for (Persona actual : Personas) {
            totalGastado += actual.getDinero();
        }

        // Calcular el promedio de gastos entre todos los participantes
        double promedioGasto = totalGastado / Personas.size();

        // Crear listas para separar a las personas que gastaron menos (morosos) y mas (acreedores) que el promedio
        List<Persona> morosos = new ArrayList<>();
        List<Persona> acreedores = new ArrayList<>();

        // Clasificar a las personas segun si han pagado más o menos que el promedio
        for (Persona actual : Personas) {
            if (actual.getDinero() < promedioGasto) {
                morosos.add(actual);
            } else if (actual.getDinero() > promedioGasto) {
                acreedores.add(actual);
            }
        }

        // Mientras haya morosos y acreedores, realizar transacciones para igualar los gastos
        while (!morosos.isEmpty() && !acreedores.isEmpty()) {
            // Tomar el primer moroso y acreedor de las listas
            Persona moroso = morosos.get(0);
            Persona acreedor = acreedores.get(0);

            // Calcular la deuda y el credito de cada persona
            double deuda = promedioGasto - moroso.getDinero();
            double credito = acreedor.getDinero() - promedioGasto;

            // Si la deuda es mayor que el credito, realizar la transaccion y mantener al moroso en la lista
            if (deuda > credito) {
                moroso.setDinero(moroso.getDinero() + credito);
                acreedor.setDinero(acreedor.getDinero() - credito);
                Resultado.add(String.format("%s debe pagar %.2f€  a %s%n", moroso.getNombre(), credito, acreedor.getNombre()));
                acreedores.remove(0);
            }
            // Si la deuda es menor o igual al credito, realizar la transacción y mantener al acreedor en la lista
            else {
                moroso.setDinero(moroso.getDinero() + deuda);
                acreedor.setDinero(acreedor.getDinero() - deuda);
                Resultado.add(String.format("%s debe pagar %.2f€  a %s%n", moroso.getNombre(), deuda, acreedor.getNombre()));
                morosos.remove(0);
                // Si la deuda es igual al credito, también eliminar al acreedor de la lista
                if (deuda == credito) {
                    acreedores.remove(0);
                }
            }
        }
        // Llamar al método para crear TextViews con los resultados almacenados en la lista Resultado
        createTextViews(Resultado);
    }

    private void createTextViews(List<String> elements) {
        // Por cada elemento que se esta en la lista se crea un text view
        for (String element : elements) {
            TextView textView = new TextView(this);
            textView.setText(element);
            textView.setTextSize(25);
            // Crear LayoutParams y establecer el margen izquierdo en 20
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(40, 20, 0, 0);
            textView.setLayoutParams(layoutParams);

            linearLayout.addView(textView);
        }
    }

}
