package com.vuelosfis.model;

import com.vuelosfis.exception.SaldoInsuficienteException;

public class PagoLifeMiles implements IPaymentStrategy {
    private LifeMiles cuenta;
    private double factorConversion;

    public PagoLifeMiles(LifeMiles cuenta, double factorConversion) {
        this.cuenta = cuenta;
        this.factorConversion = factorConversion;
    }

    @Override
    public boolean procesarPago(double monto) throws SaldoInsuficienteException {
        int millasRequeridas = (int) (monto / factorConversion);
        try {
            cuenta.redimirMillas(millasRequeridas);
            System.out.println("Procesando pago de $" + monto + " con " + millasRequeridas + " millas.");
            return true;
        } catch (SaldoInsuficienteException e) {
            System.out.println("Error en pago con millas: " + e.getMessage());
            throw e;
        }
    }
}
