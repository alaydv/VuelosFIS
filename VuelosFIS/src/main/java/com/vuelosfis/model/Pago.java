package com.vuelosfis.model;

import com.vuelosfis.exception.SaldoInsuficienteException;
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

    public boolean ejecutar() throws SaldoInsuficienteException {
        try {
            boolean resultado = estrategia.procesarPago(monto);
            if (resultado) {
                this.estado = "APROBADO";
                return true;
            }
        } catch (SaldoInsuficienteException e) {
            this.estado = "RECHAZADO";
            throw e;
        }
        this.estado = "RECHAZADO";
        return false;
    }

    public String generarRecibo() {
        return "RECIBO PAGO " + idPago + " - MONTO: " + monto + " - ESTADO: " + estado;
    }
}
