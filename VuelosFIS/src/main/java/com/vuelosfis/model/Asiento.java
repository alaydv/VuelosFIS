package com.vuelosfis.model;

public class Asiento {
    private String codigo;
    private int fila;
    private char letra;
    private ClaseAsiento clase;
    private String ubicacion;

    public int getFila() {
        return fila;
    }

    public char getLetra() {
        return letra;
    }

    public ClaseAsiento getClase() {
        return clase;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    private boolean disponible;

    public Asiento(String codigo, ClaseAsiento clase) {
        this.codigo = codigo;
        this.clase = clase;
        this.disponible = true;
        // Parsear fila y letra del codigo (e.g. "12A")
        // Simplemente asignamos valores por defecto a fila/letra si no se parsean
        this.ubicacion = "General";
    }

    public Asiento(String codigo, int fila, char letra, ClaseAsiento clase, String ubicacion) {
        this.codigo = codigo;
        this.fila = fila;
        this.letra = letra;
        this.clase = clase;
        this.ubicacion = ubicacion;
        this.disponible = true;
    }

    public void reservar() {
        this.disponible = false;
    }

    public void liberar() {
        this.disponible = true;
    }

    public boolean esVentana() {
        return ubicacion.toLowerCase().contains("ventana") || letra == 'A' || letra == 'F';
    }

    public String getCodigo() {
        return codigo;
    }

    public boolean isDisponible() {
        return disponible;
    }
}
