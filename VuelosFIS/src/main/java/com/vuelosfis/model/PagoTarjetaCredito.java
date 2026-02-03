package com.vuelosfis.model;

public class PagoTarjetaCredito implements IPaymentStrategy {
    private String numeroTarjeta;
    private String nombreTitular;
    private String fechaExpiracion;
    private String cvv;

    public PagoTarjetaCredito(String numeroTarjeta, String nombreTitular, String fechaExpiracion, String cvv) {
        this.numeroTarjeta = numeroTarjeta;
        this.nombreTitular = nombreTitular;
        this.fechaExpiracion = fechaExpiracion;
        this.cvv = cvv;
    }

    @Override
    public boolean procesarPago(double monto) {
        if (validarTarjeta()) {
            System.out.println("Procesando pago de $" + monto + " con Tarjeta de Crédito " + numeroTarjeta);
            return true;
        }
        return false;
    }

    private boolean validarTarjeta() {
        // Validación básica (simulada)
        return numeroTarjeta != null && numeroTarjeta.length() >= 13;
    }
}
