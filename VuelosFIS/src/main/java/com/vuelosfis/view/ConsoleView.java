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
        System.out.println("\n============ SISTEMA DE GESTIÓN DE VUELOS (FIS) ============");
        System.out.println("1. Ver Vuelos Disponibles");
        System.out.println("2. Realizar Reserva");
        System.out.println("3. Salir");
        System.out.println("============================================================");
        System.out.print("Seleccione una opción: ");
    }

    public int leerOpcion() {
        try {
            String input = scanner.nextLine();
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void mostrarVuelos(List<Vuelo> vuelos) {
        System.out.println("\n-------------------- VUELOS DISPONIBLES --------------------");
        if (vuelos == null || vuelos.isEmpty()) {
            System.out.println("No hay vuelos registrados en el sistema.");
        } else {
            System.out.printf("%-5s %-10s %-10s %-10s %-20s\n", "ID", "Vuelo", "Origen", "Destino", "Duración");
            for (int i = 0; i < vuelos.size(); i++) {
                Vuelo v = vuelos.get(i);
                System.out.printf("%-5d %-10s %-10s %-10s %-20s\n",
                        (i + 1), v.getNumeroVuelo(), v.getOrigen().getCodigoIATA(), v.getDestino().getCodigoIATA(),
                        v.getDuracionEstimada());
            }
        }
        System.out.println("------------------------------------------------------------");
    }

    public int solicitarIndiceVuelo() {
        System.out.print(">> Ingrese el número del vuelo a reservar: ");
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public String solicitarNombrePasajero() {
        System.out.print(">> Ingrese el nombre del pasajero: ");
        return scanner.nextLine().trim();
    }

    public String solicitarPasaportePasajero() {
        System.out.print(">> Ingrese el pasaporte del pasajero: ");
        return scanner.nextLine().trim();
    }

    public String solicitarClienteId() {
        System.out.print(">> Ingrese su ID de Cliente para la reserva: ");
        return scanner.nextLine().trim();
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println("[INFO] " + mensaje);
    }

    public void mostrarError(String error) {
        System.err.println("[ERROR] " + error);
    }
}
