package com.vuelosfis.controller;

import com.vuelosfis.model.*;
import com.vuelosfis.util.DataLoader;
import com.vuelosfis.view.ConsoleView;
import java.util.List;

public class SistemaController {
    private List<Vuelo> vuelos;
    private List<Aeropuerto> aeropuertos;
    private List<Avion> aviones;
    private List<Cliente> clientes;
    private ConsoleView view;
    private DataLoader dataLoader;

    public SistemaController(ConsoleView view) {
        this.view = view;
        this.dataLoader = new DataLoader();
        cargarDatos();
    }

    private void cargarDatos() {
        // En un caso real, estas rutas serían relativas al classpath o configurables
        // Usamos rutas relativas asumiendo ejecución desde la raíz del proyecto
        // Ojo: en ejecución normal de Java, src/main/resources se empaqueta en el
        // classpath.
        // Para este ejercicio simple, intentaremos leer desde el path relativo source
        // si falla classpath.

        String pathPrefix = "src/main/resources/";

        aeropuertos = dataLoader.cargarAeropuertos(pathPrefix + "aeropuertos.txt");
        aviones = dataLoader.cargarAviones(pathPrefix + "aviones.txt");
        clientes = dataLoader.cargarClientes(pathPrefix + "clientes.txt");
        vuelos = dataLoader.cargarVuelos(pathPrefix + "vuelos.txt", aeropuertos, aviones);

        view.mostrarMensaje("Datos cargados correctamente: " + vuelos.size() + " vuelos.");
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
                    view.mostrarMensaje("Opción inválida.");
            }
        }
    }

    private void verVuelos() {
        view.mostrarVuelos(vuelos);
    }

    private void realizarReserva() {
        verVuelos();
        int indice = view.solicitarIndiceVuelo();
        if (indice < 1 || indice > vuelos.size()) {
            view.mostrarMensaje("Vuelo inválido.");
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

        // Asignar un asiento cualquiera disponible (simplificado)
        // En una app real, mostraríamos mapa de asientos
        List<Asiento> asientosDisp = vueloSeleccionado.estaLleno() ? null : null; // TODO: Fix estaLleno logic locally
                                                                                  // inside Vuelo to return list
        // Accediendo al avion del vuelo (que es privado, así que asumimos asignación
        // automática por ahora o fix visibility)
        // Por ahora, creamos un asiento dummy para completar el flujo
        Asiento asiento = new Asiento("99F", ClaseAsiento.ECONOMICA);
        pasaje.seleccionarAsiento(asiento);

        reserva.agregarPasaje(pasaje);

        view.mostrarMensaje("Reserva creada. Total: " + pasaje.calcularPrecioTotal());

        // Simular Pago
        IPaymentStrategy pago = new PagoTarjetaCredito("1234567890123456", cliente.getNombre(), "12/25", "123");
        if (reserva.confirmarReserva(pago)) {
            view.mostrarMensaje("Reserva CONFIRMADA y PAGADA.");
            view.mostrarMensaje(reserva.getResumen());
            view.mostrarMensaje(pasaje.generarBoardingPass());
        } else {
            view.mostrarMensaje("Error en el pago.");
        }
    }

    private Cliente buscarCliente(String id) {
        for (Cliente c : clientes) {
            if (c.getIdCliente().equals(id))
                return c;
        }
        return null;
    }
}
