package com.vuelosfis.exception;

public class CargaDatosException extends VuelosFisException {
    public CargaDatosException(String mensaje) {
        super(mensaje);
    }

    public CargaDatosException(String mensaje, Throwable causa) {
        super(mensaje + " Causa: " + causa.getMessage());
    }
}
