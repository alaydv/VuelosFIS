package com.vuelosfis.model;

public abstract class ServicioAdicional {
    protected double costo;
    protected String descripcion;

    public ServicioAdicional(double costo, String descripcion) {
        this.costo = costo;
        this.descripcion = descripcion;
    }

    public abstract double calcularCosto();

    public String getDescripcion() {
        return descripcion;
    }
}
