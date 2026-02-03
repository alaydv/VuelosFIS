package com.vuelosfis.model;

public class PagoLifeMiles implements IPaymentStrategy {
    private LifeMiles cuenta;
    private double factorConversion; // Ejemplo: 100 millas = 1 USD -> factor = 0.01

    public PagoLifeMiles(LifeMiles cuenta, double factorConversion) {
        this.cuenta = cuenta;
        this.factorConversion = factorConversion;
    }

    @Override
    public boolean procesarPago(double monto) {
        int millasRequeridas = (int) (monto / factorConversion);
        if (cuenta.getSaldo() >= millasRequeridas) {
            cuenta.redimirMillas(millasRequeridas);
            System.out.println("Procesando pago de $" + monto + " con " + millasRequeridas + " millas.");
            return true;
        } else {
            System.out.println("Fondos insuficientes en LifeMiles. Requerido: " + millasRequeridas + ", Disponible: "
                    + cuenta.getSaldo());
            return false;
        }
    }
}
