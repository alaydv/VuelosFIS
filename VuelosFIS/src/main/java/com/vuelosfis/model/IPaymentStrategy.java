package com.vuelosfis.model;

public interface IPaymentStrategy {
    boolean procesarPago(double monto);
}
