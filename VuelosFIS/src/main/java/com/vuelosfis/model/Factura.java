package com.vuelosfis.model;

import java.time.LocalDate;
import java.util.List;

public class Factura {

    private String nombrePasajero;
    private String documentoPasajero;
    private String numeroVuelo;
    private String origen;
    private String destino;
    private LocalDate fechaVuelo;
    private String asiento;
    private List<ServicioAdicional> serviciosAdicionales;
    private String metodoPago;
    private double total;
    private LocalDate fechaEmision;

    public Factura(String nombrePasajero, String documentoPasajero, String numeroVuelo,
                   String origen, String destino, LocalDate fechaVuelo, String asiento,
                   List<ServicioAdicional> serviciosAdicionales, String metodoPago, double total) {
        this.nombrePasajero = nombrePasajero;
        this.documentoPasajero = documentoPasajero;
        this.numeroVuelo = numeroVuelo;
        this.origen = origen;
        this.destino = destino;
        this.fechaVuelo = fechaVuelo;
        this.asiento = asiento;
        this.serviciosAdicionales = serviciosAdicionales;
        this.metodoPago = metodoPago;
        this.total = total;
        this.fechaEmision = LocalDate.now();
    }

    // Getters
    public String getNombrePasajero() {
        return nombrePasajero;
    }

    public String getDocumentoPasajero() {
        return documentoPasajero;
    }

    public String getNumeroVuelo() {
        return numeroVuelo;
    }

    public String getOrigen() {
        return origen;
    }

    public String getDestino() {
        return destino;
    }

    public LocalDate getFechaVuelo() {
        return fechaVuelo;
    }

    public String getAsiento() {
        return asiento;
    }

    public List<ServicioAdicional> getServiciosAdicionales() {
        return serviciosAdicionales;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public double getTotal() {
        return total;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    // Método para mostrar resumen
    public String generarResumen() {
        StringBuilder sb = new StringBuilder();
        sb.append("Factura de vuelo\n");
        sb.append("Pasajero: ").append(nombrePasajero).append(" (").append(documentoPasajero).append(")\n");
        sb.append("Vuelo: ").append(numeroVuelo).append(" | ").append(origen).append(" → ").append(destino).append("\n");
        sb.append("Fecha: ").append(fechaVuelo).append(" | Asiento: ").append(asiento).append("\n");
        sb.append("Método de pago: ").append(metodoPago).append("\n");
        sb.append("Servicios adicionales:\n");
        for (ServicioAdicional servicio : serviciosAdicionales) {
            sb.append("- ").append(servicio.getDescripcion()).append(" ($").append(servicio.getCosto()).append(")\n");
        }
        sb.append("Total pagado: $").append(String.format("%.2f", total)).append("\n");
        sb.append("Fecha de emisión: ").append(fechaEmision).append("\n");
        return sb.toString();
    }
}