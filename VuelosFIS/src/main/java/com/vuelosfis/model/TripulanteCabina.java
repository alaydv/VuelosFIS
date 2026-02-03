package com.vuelosfis.model;

import java.util.Date;
import java.util.List;

public class TripulanteCabina extends Tripulacion {
    private List<String> idiomas;
    private List<String> certificaciones;

    public TripulanteCabina(String id, String nombre, Date fechaContratacion, List<String> idiomas) {
        super(id, nombre, fechaContratacion);
        this.idiomas = idiomas;
    }

    @Override
    public void trabajar() {
        System.out.println("Tripulante " + nombre + " está atentiendo pasajeros.");
    }

    public void servirAlimentos() {
        System.out.println("Tripulante " + nombre + " sirviendo alimentos.");
    }

    public void realizarDemostracionSeguridad() {
        System.out.println("Tripulante " + nombre + " realizando demostración de seguridad.");
    }
}
