package com.vuelosfis.model;

public class Cliente {
    private String idCliente;
    private String nombre;
    private String email;
    private String telefono;
    private String direccion;
    private LifeMiles cuentaLifeMiles;

    public Cliente(String idCliente, String nombre, String email) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.email = email;
    }

    public void setCuentaLifeMiles(LifeMiles cuenta) {
        this.cuentaLifeMiles = cuenta;
    }

    public LifeMiles getCuentaLifeMiles() {
        return cuentaLifeMiles;
    }

    public int consultarSaldoMillas() {
        if (cuentaLifeMiles != null) {
            return cuentaLifeMiles.getSaldo();
        }
        return 0;
    }

    public boolean validarDatos() {
        return email != null && email.contains("@");
    }

    public String getNombre() {
        return nombre;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
