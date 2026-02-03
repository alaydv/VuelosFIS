package com.vuelosfis.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.time.Duration;

public class Itinerario {
    private String codigoItinerario;
    private List<Vuelo> vuelos;
    private Date fechaGeneracion;

    public Itinerario(String codigoItinerario) {
        this.codigoItinerario = codigoItinerario;
        this.vuelos = new ArrayList<>();
        this.fechaGeneracion = new Date();
    }

    public void agregarVuelo(Vuelo vuelo) {
        this.vuelos.add(vuelo);
    }

    public int getEscalas() {
        return Math.max(0, vuelos.size() - 1);
    }

    public Duration getDuracionTotal() {
        Duration total = Duration.ZERO;
        for (Vuelo v : vuelos) {
            total = total.plus(v.getDuracionEstimada());
        }
        // Note: Logic for layover time is omitted for simplicity as it requires more
        // complex calculation
        return total;
    }

    public boolean esValido() {
        return !vuelos.isEmpty();
    }

    public List<Vuelo> getVuelos() {
        return vuelos;
    }
}
