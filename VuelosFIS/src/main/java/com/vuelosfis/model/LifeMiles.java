package com.vuelosfis.model;

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
        this.saldoMillas += cantidad;
    }

    public boolean redimirMillas(int cantidad) {
        if (this.saldoMillas >= cantidad) {
            this.saldoMillas -= cantidad;
            return true;
        }
        return false;
    }

    public int getSaldo() {
        return saldoMillas;
    }

    public String verificarStatus() {
        return nivelStatus;
    }
}
