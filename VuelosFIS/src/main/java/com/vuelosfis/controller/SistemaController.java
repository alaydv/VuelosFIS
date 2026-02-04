package com.vuelosfis.controller;

import com.vuelosfis.exception.*;
import com.vuelosfis.model.*;
import com.vuelosfis.util.DataLoader;
import java.util.List;
import java.util.Map;

public class SistemaController {

    private List<Vuelo> vuelos;
    private Map<String, Aeropuerto> aeropuertos;
    private Map<String, Avion> aviones;
    private List<Cliente> clientes;
    private DataLoader dataLoader;
    // New: Track all reservations for Admin view
    private java.util.List<Reserva> allReservas;

    public SistemaController() {
        this.dataLoader = new DataLoader();
        this.allReservas = new java.util.ArrayList<>();
        cargarDatos();
    }

    private void cargarDatos() {
        try {
            System.out.println("Iniciando carga de datos. Directorio de trabajo: " + System.getProperty("user.dir"));

            String[] possiblePaths = {
                    "src/main/resources/",
                    "VuelosFIS/src/main/resources/",
                    "../src/main/resources/",
                    "./"
            };

            String pathPrefix = null;
            for (String path : possiblePaths) {
                java.io.File testFile = new java.io.File(path + "aeropuertos.txt");
                if (testFile.exists()) {
                    pathPrefix = path;
                    System.out.println("Datos encontrados en: " + testFile.getAbsolutePath());
                    break;
                }
            }

            if (pathPrefix == null) {
                System.err.println("ADVERTENCIA: No se encontraron los archivos de datos en las rutas esperadas.");
                // Try minimal fallback or crash gracefully
                throw new java.io.FileNotFoundException("aeropuertos.txt no encontrado en ninguna ruta probable.");
            }

            aeropuertos = dataLoader.cargarAeropuertosMap(pathPrefix + "aeropuertos.txt");
            aviones = dataLoader.cargarAvionesMap(pathPrefix + "aviones.txt");
            clientes = dataLoader.cargarClientes(pathPrefix + "clientes.txt");
            vuelos = dataLoader.cargarVuelos(pathPrefix + "vuelos.txt", aeropuertos, aviones);

            System.out.println("Carga de datos exitosa. Vuelos cargados: " + (vuelos != null ? vuelos.size() : 0));

        } catch (Exception e) {
            System.err.println("Error crítico al cargar datos: " + e.getMessage());
            e.printStackTrace();
            // Initialize empty lists to prevent crashes
            if (vuelos == null)
                vuelos = new java.util.ArrayList<>();
            if (clientes == null)
                clientes = new java.util.ArrayList<>();
            if (aeropuertos == null)
                aeropuertos = new java.util.HashMap<>();
            if (aviones == null)
                aviones = new java.util.HashMap<>();
        }
    }

    public Reserva crearReserva(Vuelo vuelo, Cliente cliente, Pasajero pasajero, Asiento asiento) throws Exception {
        if (vuelo == null || cliente == null || pasajero == null || asiento == null) {
            throw new IllegalArgumentException("Datos de reserva incompletos.");
        }

        Reserva reserva = new Reserva(cliente);
        Pasaje pasaje = new Pasaje(pasajero, vuelo);

        // Verifica disponibilidad del asiento (implementar lógica si es necesario en
        // Vuelo/Avion)
        pasaje.seleccionarAsiento(asiento);
        reserva.agregarPasaje(pasaje);

        return reserva;
    }

    public void pagarReserva(Reserva reserva, IPaymentStrategy estrategiaPago)
            throws SaldoInsuficienteException {
        reserva.confirmarReserva(estrategiaPago);
        // Track completed reservation
        allReservas.add(reserva);
    }

    public List<Reserva> getTodasLasReservas() {
        return allReservas;
    }

    public Cliente buscarCliente(String id) {
        if (clientes == null) {
            return null;
        }
        for (Cliente c : clientes) {
            if (c.getIdCliente().equals(id)) {
                return c;
            }
        }
        return null;
    }

    public Cliente registrarClienteInvitado(String id, String nombre, String email) {
        Cliente nuevo = new Cliente(id, nombre, email);
        // Opcional: agregarlo a la lista de clientes si queremos persistencia en
        // memoria
        return nuevo;
    }

    public List<Vuelo> getVuelos() {
        return vuelos;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }
}
