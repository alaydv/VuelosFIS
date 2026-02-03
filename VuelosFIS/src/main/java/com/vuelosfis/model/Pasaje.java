package com.vuelosfis.model;

import java.util.ArrayList;
import java.util.List;

public class Pasaje {
    private String numeroTicket;
    private String codigoQR;
    private Pasajero pasajero;
    private Vuelo vuelo;
    private Asiento asiento;
    private List<ServicioAdicional> serviciosAdicionales;
    private double precioBase;
    private boolean emitido;

    public Pasaje(Pasajero pasajero, Vuelo vuelo) {
        this.pasajero = pasajero;
        this.vuelo = vuelo;
        this.serviciosAdicionales = new ArrayList<>();
        this.precioBase = 100.0; // Default base price
        this.emitido = false;
        this.numeroTicket = "TKT-" + System.currentTimeMillis();
    }

    public void seleccionarAsiento(Asiento asiento) {
        this.asiento = asiento;
        asiento.reservar();
    }

    public void agregarServicio(ServicioAdicional servicio) {
        this.serviciosAdicionales.add(servicio);
    }

    public double calcularPrecioTotal() {
        double total = precioBase;
        for (ServicioAdicional servicio : serviciosAdicionales) {
            total += servicio.calcularCosto();
        }
        return total;
    }

    public String generarBoardingPass() {
        if (!emitido)
            return "Pasaje no emitido.";
        return "BOARDING PASS:\nPassenger: " + pasajero.getNombre() +
                "\nFlight: " + vuelo.getNumeroVuelo() +
                "\nSeat: " + (asiento != null ? asiento.getCodigo() : "Assigned at Gate");
    }

    public void setEmitido(boolean emitido) {
        this.emitido = emitido;
    }

    public String getNumeroTicket() {
        return numeroTicket;
    }
}
