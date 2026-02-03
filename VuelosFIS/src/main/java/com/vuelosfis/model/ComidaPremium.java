package com.vuelosfis.model;

public class ComidaPremium extends ServicioAdicional {
    private String menu;
    private boolean esVegetariano;

    public ComidaPremium(String menu, boolean esVegetariano) {
        super(25.0, "Comida Premium: " + menu);
        this.menu = menu;
        this.esVegetariano = esVegetariano;
    }

    @Override
    public double calcularCosto() {
        if (esVegetariano) {
            return this.costo + 5.0; // Recargo vegetariano
        }
        return this.costo;
    }
}
