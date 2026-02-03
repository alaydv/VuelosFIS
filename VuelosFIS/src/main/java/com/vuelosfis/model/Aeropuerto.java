package com.vuelosfis.model;

public class Aeropuerto {
    private String codigoIATA;
    private String nombre;
    private String ciudad;
    private String pais;
    private double latitud;
    private double longitud;

    public Aeropuerto(String codigoIATA, String nombre) {
        this.codigoIATA = codigoIATA;
        this.nombre = nombre;
    }

    public Aeropuerto(String codigoIATA, String nombre, String ciudad, String pais, double lat, double lon) {
        this.codigoIATA = codigoIATA;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.pais = pais;
        this.latitud = lat;
        this.longitud = lon;
    }

    public String getUbicacion() {
        return ciudad + ", " + pais;
    }

    public String getCodigoIATA() {
        return codigoIATA;
    }

    public String getNombre() {
        return nombre;
    }
}
