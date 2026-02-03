package com.vuelosfis.util;

import com.vuelosfis.exception.CargaDatosException;
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

    public Map<String, Aeropuerto> cargarAeropuertosMap(String rutaArchivo) throws CargaDatosException {
        Map<String, Aeropuerto> aeropuertos = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length >= 6) {
                    Aeropuerto a = new Aeropuerto(datos[0].trim(), datos[1].trim(), datos[2].trim(), datos[3].trim(),
                            Double.parseDouble(datos[4].trim()), Double.parseDouble(datos[5].trim()));
                    aeropuertos.put(a.getCodigoIATA(), a);
                }
            }
        } catch (IOException | NumberFormatException e) {
            throw new CargaDatosException("Error al cargar aeropuertos desde: " + rutaArchivo, e);
        }
        return aeropuertos;
    }

    public Map<String, Avion> cargarAvionesMap(String rutaArchivo) throws CargaDatosException {
        Map<String, Avion> aviones = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length >= 3) {
                    Avion a = new Avion(datos[0].trim(), datos[1].trim(), Integer.parseInt(datos[2].trim()));
                    for (int i = 1; i <= 10; i++) {
                        a.agregarAsiento(new Asiento(i + "A", i, 'A', ClaseAsiento.ECONOMICA, "Ventana"));
                        a.agregarAsiento(new Asiento(i + "B", i, 'B', ClaseAsiento.ECONOMICA, "Pasillo"));
                    }
                    aviones.put(a.getMatricula(), a);
                }
            }
        } catch (IOException | NumberFormatException e) {
            throw new CargaDatosException("Error al cargar aviones desde: " + rutaArchivo, e);
        }
        return aviones;
    }

    public List<Cliente> cargarClientes(String rutaArchivo) throws CargaDatosException {
        List<Cliente> clientes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length >= 3) {
                    Cliente c = new Cliente(datos[0].trim(), datos[1].trim(), datos[2].trim());
                    c.setCuentaLifeMiles(new LifeMiles(datos[0] + "-LM"));
                    c.getCuentaLifeMiles().acumularMillas(50000);
                    clientes.add(c);
                }
            }
        } catch (IOException e) {
            throw new CargaDatosException("Error al cargar clientes desde: " + rutaArchivo, e);
        }
        return clientes;
    }

    public List<Vuelo> cargarVuelos(String rutaArchivo, Map<String, Aeropuerto> aeropuertoMap,
            Map<String, Avion> avionMap) throws CargaDatosException {
        List<Vuelo> vuelos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length >= 6) {
                    Aeropuerto origen = aeropuertoMap.get(datos[1].trim());
                    Aeropuerto destino = aeropuertoMap.get(datos[2].trim());
                    Avion avion = avionMap.get(datos[5].trim());

                    if (origen != null && destino != null && avion != null) {
                        LocalDateTime salida = LocalDateTime.parse(datos[3].trim());
                        LocalDateTime llegada = LocalDateTime.parse(datos[4].trim());
                        Vuelo v = new Vuelo(datos[0].trim(), origen, destino, salida, llegada, avion);
                        vuelos.add(v);
                    } else {
                        System.err.println("Advertencia: Referencia inválida en vuelo " + datos[0]);
                    }
                }
            }
        } catch (IOException | java.time.format.DateTimeParseException e) {
            throw new CargaDatosException("Error al cargar vuelos desde: " + rutaArchivo, e);
        }
        return vuelos;
    }
}
