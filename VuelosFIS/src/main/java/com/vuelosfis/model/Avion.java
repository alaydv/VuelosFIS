package com.vuelosfis.model;

import java.util.ArrayList;
import java.util.List;

public class Avion {
    private String matricula;
    private String modelo;
    private String fabricante;
    private int anioFabricacion;
    private int capacidadTotal;
    private List<Asiento> asientos;

    public Avion(String matricula, String modelo) {
        this.matricula = matricula;
        this.modelo = modelo;
        this.asientos = new ArrayList<>();
    }

    public Avion(String matricula, String modelo, int capacidad) {
        this(matricula, modelo);
        this.capacidadTotal = capacidad;
    }
    
    public List<Asiento> getAsientos() {
    return asientos;
    }

    public List<Asiento> getAsientosDisponibles() {
        List<Asiento> disponibles = new ArrayList<>();
        for (Asiento a : asientos) {
            if (a.isDisponible()) {
                disponibles.add(a);
            }
        }
        return disponibles;
    }

    public void realizarMantenimiento() {
        System.out.println("Realizando mantenimiento al avión " + matricula);
    }

    public void agregarAsiento(Asiento asiento) {
        this.asientos.add(asiento);
    }

    public String getMatricula() {
        return matricula;
    }

    public String getModelo() {
        return modelo;
    }

    public String getFabricante() {
        return fabricante;
    }

    public int getAnioFabricacion() {
        return anioFabricacion;
    }

    public int getCapacidadTotal() {
        return capacidadTotal;
    }
}
