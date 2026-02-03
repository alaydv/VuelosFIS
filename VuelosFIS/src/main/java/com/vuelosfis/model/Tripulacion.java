package com.vuelosfis.model;

import java.util.Date;

public abstract class Tripulacion {
    protected String idEmpleado;
    protected String nombre;
    protected Date fechaContratacion;

    public Tripulacion(String idEmpleado, String nombre, Date fechaContratacion) {
        this.idEmpleado = idEmpleado;
        this.nombre = nombre;
        this.fechaContratacion = fechaContratacion;
    }

    public abstract void trabajar();

    public String getNombre() {
        return nombre;
    }
}
