package com.vuelosfis.model;

import java.time.LocalDateTime;

public class Pago {
    private String idPago;
    private double monto;
    private LocalDateTime fecha;
    private String estado;
    private IPaymentStrategy estrategia;

    public Pago(double monto, IPaymentStrategy estrategia) {
        this.monto = monto;
        this.estrategia = estrategia;
        this.idPago = "PAY-" + System.currentTimeMillis();
        this.fecha = LocalDateTime.now();
        this.estado = "PENDIENTE";
    }

    public boolean ejecutar() {
        boolean resultado = estrategia.procesarPago(monto);
        if (resultado) {
            this.estado = "APROBADO";
        } else {
            this.estado = "RECHAZADO";
        }
        return resultado;
    }

    public String generarRecibo() {
        return "RECIBO PAGO " + idPago + " - MONTO: " + monto + " - ESTADO: " + estado;
    }
}
