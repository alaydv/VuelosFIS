package com.vuelosfis.model;

import java.util.Date;

public class Pasajero {
    private String pasaporte;
    private String nroDocumento;
    private String nombre;
    private Date fechaNacimiento;
    private String nacionalidad;
    private String necesidadesEspeciales;

    public Pasajero(String nombre, String pasaporte) {
        this.nombre = nombre;
        this.pasaporte = pasaporte;
    }

    public Pasajero(String nombre, String pasaporte, String documento, Date nacimiento) {
        this.nombre = nombre;
        this.pasaporte = pasaporte;
        this.nroDocumento = documento;
        this.fechaNacimiento = nacimiento;
    }

    public boolean esMenorDeEdad() {
        // Lógica simplificada
        if (fechaNacimiento == null)
            return false;
        Date now = new Date();
        long diff = now.getTime() - fechaNacimiento.getTime();
        long ageInMillis = 1000L * 60 * 60 * 24 * 365 * 18; // aprox 18 years
        return diff < ageInMillis;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPasaporte() {
        return pasaporte;
    }
}
