package com.vuelosfis.model;

import com.vuelosfis.exception.SaldoInsuficienteException;

public interface IPaymentStrategy {
    boolean procesarPago(double monto) throws SaldoInsuficienteException;
}
