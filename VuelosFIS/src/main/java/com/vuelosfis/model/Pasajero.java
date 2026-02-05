package com.vuelosfis.model;

import java.util.Date;

public class Pasajero {
    private String pasaporte;
    private String nroDocumento;
    private String nombre;
    private Date fechaNacimiento;
    private String nacionalidad;
    private String necesidadesEspeciales;

    public Pasajero(String nombre, String nroDocumento) {
        this.nombre = nombre;
        this.nroDocumento = nroDocumento;
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

    public String getNroDocumento() {
        return nroDocumento;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public String getNecesidadesEspeciales() {
        return necesidadesEspeciales;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }
}
