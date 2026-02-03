package com.vuelosfis.model;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Vuelo {
    private String numeroVuelo;
    private LocalDateTime fechaSalida;
    private LocalDateTime fechaLlegada;
    private Aeropuerto origen;
    private Aeropuerto destino;
    private Avion avion;
    private EstadoVuelo estado;
    private List<Tripulacion> tripulacion;

    public Vuelo(String numero, Aeropuerto origen, Aeropuerto destino) {
        this.numeroVuelo = numero;
        this.origen = origen;
        this.destino = destino;
        this.estado = EstadoVuelo.PROGRAMADO;
        this.tripulacion = new ArrayList<>();
    }

    public Vuelo(String numero, Aeropuerto origen, Aeropuerto destino, LocalDateTime salida, LocalDateTime llegada,
            Avion avion) {
        this(numero, origen, destino);
        this.fechaSalida = salida;
        this.fechaLlegada = llegada;
        this.avion = avion;
    }

    public Duration getDuracionEstimada() {
        if (fechaSalida != null && fechaLlegada != null) {
            return Duration.between(fechaSalida, fechaLlegada);
        }
        return Duration.ZERO;
    }

    public void asignarTripulacion(Tripulacion miembro) {
        this.tripulacion.add(miembro);
    }

    public void actualizarEstado(EstadoVuelo nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public boolean estaLleno() {
        if (avion == null)
            return false;
        // Simplified Logic: assuming we track passengers somewhere else or check
        // available seats
        return avion.getAsientosDisponibles().isEmpty();
    }

    public String getNumeroVuelo() {
        return numeroVuelo;
    }

    public Aeropuerto getOrigen() {
        return origen;
    }

    public Aeropuerto getDestino() {
        return destino;
    }
}
