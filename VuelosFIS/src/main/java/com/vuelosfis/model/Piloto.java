package com.vuelosfis.model;

import java.util.Date;

public class Piloto extends Tripulacion {
    private String nroLicencia;
    private int horasVueloAcumuladas;
    private String rango;

    public Piloto(String id, String nombre, Date fechaContratacion, String licencia, String rango) {
        super(id, nombre, fechaContratacion);
        this.nroLicencia = licencia;
        this.horasVueloAcumuladas = 0;
        this.rango = rango;
    }

    @Override
    public void trabajar() {
        System.out.println("Piloto " + nombre + " está pilotando el avión.");
    }

    public void planificarRuta() {
        System.out.println("Piloto " + nombre + " planificando ruta.");
    }

    public void reportarIncidente() {
        System.out.println("Piloto " + nombre + " reportando incidente.");
    }

    public String getNroLicencia() {
        return nroLicencia;
    }

    public int getHorasVueloAcumuladas() {
        return horasVueloAcumuladas;
    }

    public String getRango() {
        return rango;
    }
}
