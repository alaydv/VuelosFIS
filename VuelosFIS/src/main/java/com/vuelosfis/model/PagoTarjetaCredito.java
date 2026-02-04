package com.vuelosfis.model;

import com.vuelosfis.exception.SaldoInsuficienteException;

public class PagoTarjetaCredito implements IPaymentStrategy {
    private String numeroTarjeta;
    private String nombreTitular;
    private String fechaExpiracion;

    public String getNombreTitular() {
        return nombreTitular;
    }

    public String getFechaExpiracion() {
        return fechaExpiracion;
    }

    private String cvv;

    public String getCvv() {
        return cvv;
    }

    public PagoTarjetaCredito(String numeroTarjeta, String nombreTitular, String fechaExpiracion, String cvv) {
        this.numeroTarjeta = numeroTarjeta;
        this.nombreTitular = nombreTitular;
        this.fechaExpiracion = fechaExpiracion;
        this.cvv = cvv;
    }

    @Override
    public boolean procesarPago(double monto) throws SaldoInsuficienteException {
        if (validarTarjeta()) {
            System.out.println("Procesando pago de $" + monto + " con Tarjeta de Crédito " + numeroTarjeta);
            return true;
        }
        throw new SaldoInsuficienteException("Tarjeta inválida o rechazada.");
    }

    private boolean validarTarjeta() {
        return numeroTarjeta != null && numeroTarjeta.length() >= 13;
    }
}
