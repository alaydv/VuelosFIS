package com.vuelosfis.model;

import com.vuelosfis.exception.SaldoInsuficienteException;
import java.util.Date;

public class LifeMiles {
    private String numeroCuenta;
    private int saldoMillas;
    private String nivelStatus;
    private Date fechaVencimiento;

    public LifeMiles(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
        this.saldoMillas = 0;
        this.nivelStatus = "Básico";
        this.fechaVencimiento = new Date();
    }

    public void acumularMillas(int cantidad) {
        if (cantidad > 0) {
            this.saldoMillas += cantidad;
        }
    }

    public void redimirMillas(int cantidad) throws SaldoInsuficienteException {
        if (this.saldoMillas >= cantidad) {
            this.saldoMillas -= cantidad;
        } else {
            throw new SaldoInsuficienteException(
                    "Saldo de millas insuficiente. Requerido: " + cantidad + ", Disponible: " + this.saldoMillas);
        }
    }

    public int getSaldo() {
        return saldoMillas;
    }

    public String verificarStatus() {
        return nivelStatus;
    }
}
