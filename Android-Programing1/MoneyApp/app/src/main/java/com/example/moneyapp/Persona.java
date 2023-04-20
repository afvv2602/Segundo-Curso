package com.example.moneyapp;

public class Persona {

    private String nombre;
    private double dinero;

    public Persona(String nombre, double dinero){
        this.nombre = nombre;
        this.dinero = dinero;
    }

    public double getDinero() {
        return dinero;
    }

    public void setDinero(double dinero) {
        this.dinero = dinero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
