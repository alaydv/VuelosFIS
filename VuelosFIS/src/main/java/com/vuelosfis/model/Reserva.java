package com.vuelosfis.model;

import com.vuelosfis.exception.SaldoInsuficienteException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Reserva {
    private String codigoReserva;
    private LocalDateTime fechaCreacion;
    private EstadoReserva estado;
    private Cliente cliente;
    private List<Pasaje> pasajes;
    private double total;
    private INotificationService notificador;

    private Pago pago;

    public Pago getPago() {
        return pago;
    }

    public Reserva(Cliente cliente) {
        this.cliente = cliente;
        this.codigoReserva = "RES-" + System.currentTimeMillis();
        this.fechaCreacion = LocalDateTime.now();
        this.estado = EstadoReserva.PENDIENTE;
        this.pasajes = new ArrayList<>();
    }

    public void agregarPasaje(Pasaje pasaje) {
        this.pasajes.add(pasaje);
        this.total += pasaje.calcularPrecioTotal();
    }

    public void confirmarReserva(IPaymentStrategy metodoPago) throws SaldoInsuficienteException {
        Pago nuevoPago = new Pago(this.total, metodoPago);
        if (nuevoPago.ejecutar()) {
            this.pago = nuevoPago;
            this.estado = EstadoReserva.PAGADA;
            for (Pasaje p : pasajes) {
                p.setEmitido(true);
            }
            if (notificador != null) {
                notificador.notificar("Reserva Confirmada " + codigoReserva, cliente.getEmail());
            }
        }
    }

    public void setNotificador(INotificationService notificador) {
        this.notificador = notificador;
    }

    public void cancelar() {
        this.estado = EstadoReserva.CANCELADA;
    }

    public String getResumen() {
        return "Reserva: " + codigoReserva + ", Cliente: " + cliente.getNombre() + ", Total: " + total + ", Estado: "
                + estado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<Pasaje> getPasajes() {
        return pasajes;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public EstadoReserva getEstado() {
        return estado;
    }
}
