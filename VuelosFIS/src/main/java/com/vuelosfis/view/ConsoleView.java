package com.vuelosfis.view;

import com.vuelosfis.model.Vuelo;
import java.util.List;
import java.util.Scanner;

public class ConsoleView {
    private Scanner scanner;

    public ConsoleView() {
        this.scanner = new Scanner(System.in);
    }

    public void mostrarMenu() {
        System.out.println("\n--- SISTEMA DE GESTIÓN DE VUELOS (FIS) ---");
        System.out.println("1. Ver Vuelos Disponibles");
        System.out.println("2. Realizar Reserva");
        System.out.println("3. Salir");
        System.out.print("Seleccione una opción: ");
    }

    public int leerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void mostrarVuelos(List<Vuelo> vuelos) {
        System.out.println("\n--- VUELOS DISPONIBLES ---");
        if (vuelos.isEmpty()) {
            System.out.println("No hay vuelos disponibles.");
        } else {
            for (int i = 0; i < vuelos.size(); i++) {
                Vuelo v = vuelos.get(i);
                System.out.printf("%d. Vuelo %s: %s -> %s (Duración: %s)\n",
                        (i + 1), v.getNumeroVuelo(), v.getOrigen().getCodigoIATA(), v.getDestino().getCodigoIATA(),
                        v.getDuracionEstimada());
            }
        }
    }

    public int solicitarIndiceVuelo() {
        System.out.print("Ingrese el número del vuelo a reservar: ");
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public String solicitarNombrePasajero() {
        System.out.print("Ingrese el nombre del pasajero: ");
        return scanner.nextLine();
    }

    public String solicitarPasaportePasajero() {
        System.out.print("Ingrese el pasaporte del pasajero: ");
        return scanner.nextLine();
    }

    public String solicitarClienteId() {
        System.out.print("Ingrese su ID de Cliente para la reserva: ");
        return scanner.nextLine();
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }
}
