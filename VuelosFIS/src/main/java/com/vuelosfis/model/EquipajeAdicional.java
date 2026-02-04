package com.vuelosfis.model;

public class EquipajeAdicional extends ServicioAdicional {
    private double pesoKg;
    private String dimensiones;

    public double getPesoKg() {
        return pesoKg;
    }

    public String getDimensiones() {
        return dimensiones;
    }

    public EquipajeAdicional(double pesoKg, String dimensiones) {
        super(pesoKg * 10, "Equipaje Adicional " + pesoKg + "kg"); // Ejemplo costo: $10 por kg
        this.pesoKg = pesoKg;
        this.dimensiones = dimensiones;
    }

    @Override
    public double calcularCosto() {
        return this.costo;
    }
}
