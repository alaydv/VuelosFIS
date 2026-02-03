package com.vuelosfis.model;

import com.vuelosfis.exception.SaldoInsuficienteException;

public class PagoPayPal implements IPaymentStrategy {
    private String emailCuenta;
    private String tokenSeguridad;

    public PagoPayPal(String emailCuenta, String tokenSeguridad) {
        this.emailCuenta = emailCuenta;
        this.tokenSeguridad = tokenSeguridad;
    }

    @Override
    public boolean procesarPago(double monto) throws SaldoInsuficienteException {
        System.out.println("Procesando pago de $" + monto + " con PayPal (" + emailCuenta + ")");
        return true;
    }
}
