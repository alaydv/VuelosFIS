package com.vuelosfis.util;

import com.vuelosfis.model.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataLoader {

    public List<Aeropuerto> cargarAeropuertos(String rutaArchivo) {
        List<Aeropuerto> aeropuertos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length >= 6) {
                    Aeropuerto a = new Aeropuerto(datos[0], datos[1], datos[2], datos[3],
                            Double.parseDouble(datos[4]), Double.parseDouble(datos[5]));
                    aeropuertos.add(a);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar aeropuertos: " + e.getMessage());
        }
        return aeropuertos;
    }

    public List<Avion> cargarAviones(String rutaArchivo) {
        List<Avion> aviones = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length >= 3) {
                    Avion a = new Avion(datos[0], datos[1], Integer.parseInt(datos[2].trim()));
                    // Inicializar asientos por defecto
                    for (int i = 1; i <= 10; i++) {
                        a.agregarAsiento(new Asiento(i + "A", i, 'A', ClaseAsiento.ECONOMICA, "Ventana"));
                        a.agregarAsiento(new Asiento(i + "B", i, 'B', ClaseAsiento.ECONOMICA, "Pasillo"));
                    }
                    aviones.add(a);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar aviones: " + e.getMessage());
        }
        return aviones;
    }

    public List<Cliente> cargarClientes(String rutaArchivo) {
        List<Cliente> clientes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length >= 3) {
                    Cliente c = new Cliente(datos[0], datos[1], datos[2]);
                    clientes.add(c);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar clientes: " + e.getMessage());
        }
        return clientes;
    }

    public List<Vuelo> cargarVuelos(String rutaArchivo, List<Aeropuerto> aeropuertos, List<Avion> aviones) {
        List<Vuelo> vuelos = new ArrayList<>();
        Map<String, Aeropuerto> aptMap = new HashMap<>();
        for (Aeropuerto a : aeropuertos)
            aptMap.put(a.getCodigoIATA(), a);

        Map<String, Avion> avionMap = new HashMap<>();
        for (Avion a : aviones)
            avionMap.put(a.getMatricula(), a);

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length >= 6) {
                    Aeropuerto origen = aptMap.get(datos[1]);
                    Aeropuerto destino = aptMap.get(datos[2]);
                    Avion avion = avionMap.get(datos[5]);

                    if (origen != null && destino != null && avion != null) {
                        LocalDateTime salida = LocalDateTime.parse(datos[3]);
                        LocalDateTime llegada = LocalDateTime.parse(datos[4]);
                        Vuelo v = new Vuelo(datos[0], origen, destino, salida, llegada, avion);
                        vuelos.add(v);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar vuelos: " + e.getMessage());
        }
        return vuelos;
    }
}
