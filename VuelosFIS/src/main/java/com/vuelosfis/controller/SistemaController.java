package com.vuelosfis.controller;

import com.vuelosfis.exception.*;
import com.vuelosfis.model.*;
import com.vuelosfis.util.DataLoader;
import com.vuelosfis.view.ConsoleView;
import java.util.List;
import java.util.Map;

public class SistemaController {
    private List<Vuelo> vuelos;
    private Map<String, Aeropuerto> aeropuertos;
    private Map<String, Avion> aviones;
    private List<Cliente> clientes;
    private ConsoleView view;
    private DataLoader dataLoader;

    public SistemaController(ConsoleView view) {
        this.view = view;
        this.dataLoader = new DataLoader();
        cargarDatos();
    }

    private void cargarDatos() {
        try {
            String pathPrefix = "src/main/resources/";
            aeropuertos = dataLoader.cargarAeropuertosMap(pathPrefix + "aeropuertos.txt");
            aviones = dataLoader.cargarAvionesMap(pathPrefix + "aviones.txt");
            clientes = dataLoader.cargarClientes(pathPrefix + "clientes.txt");
            vuelos = dataLoader.cargarVuelos(pathPrefix + "vuelos.txt", aeropuertos, aviones);

            view.mostrarMensaje("Datos cargados correctamente: " + vuelos.size() + " vuelos.");
        } catch (CargaDatosException e) {
            view.mostrarError("Error crítico al cargar datos: " + e.getMessage());
        }
    }

    public void iniciar() {
        boolean salir = false;
        while (!salir) {
            view.mostrarMenu();
            int opcion = view.leerOpcion();
            switch (opcion) {
                case 1:
                    verVuelos();
                    break;
                case 2:
                    realizarReserva();
                    break;
                case 3:
                    salir = true;
                    view.mostrarMensaje("Saliendo del sistema...");
                    break;
                default:
                    view.mostrarError("Opción inválida, intente de nuevo.");
            }
        }
    }

    private void verVuelos() {
        view.mostrarVuelos(vuelos);
    }

    private void realizarReserva() {
        verVuelos();
        if (vuelos == null || vuelos.isEmpty())
            return;

        int indice = view.solicitarIndiceVuelo();
        if (indice < 1 || indice > vuelos.size()) {
            view.mostrarError("Vuelo inválido.");
            return;
        }
        Vuelo vueloSeleccionado = vuelos.get(indice - 1);

        String idCliente = view.solicitarClienteId();
        Cliente cliente = buscarCliente(idCliente);
        if (cliente == null) {
            view.mostrarMensaje("Cliente no encontrado (Use IDs: 1001, 1002...). Creando reserva como invitado.");
            cliente = new Cliente(idCliente, "Invitado", "invitado@email.com");
        }

        Reserva reserva = new Reserva(cliente);

        String nombrePax = view.solicitarNombrePasajero();
        String pasaportePax = view.solicitarPasaportePasajero();
        Pasajero pasajero = new Pasajero(nombrePax, pasaportePax);

        Pasaje pasaje = new Pasaje(pasajero, vueloSeleccionado);

        try {
            Asiento asiento = new Asiento("99F", ClaseAsiento.ECONOMICA);
            pasaje.seleccionarAsiento(asiento);
            reserva.agregarPasaje(pasaje);

            view.mostrarMensaje("Reserva creada. Total a pagar: " + pasaje.calcularPrecioTotal());

            IPaymentStrategy pago = new PagoTarjetaCredito("1234567890123456", cliente.getNombre(), "12/25", "123");

            reserva.confirmarReserva(pago);

            view.mostrarMensaje("Reserva CONFIRMADA y PAGADA.");
            view.mostrarMensaje(reserva.getResumen());
            view.mostrarMensaje(pasaje.generarBoardingPass());

        } catch (SaldoInsuficienteException e) {
            view.mostrarError("La reserva no pudo ser pagada: " + e.getMessage());
            reserva.cancelar();
        } catch (Exception e) {
            view.mostrarError("Error inesperado al procesar la reserva: " + e.getMessage());
        }
    }

    private Cliente buscarCliente(String id) {
        if (clientes == null)
            return null;
        for (Cliente c : clientes) {
            if (c.getIdCliente().equals(id))
                return c;
        }
        return null;
    }
}
