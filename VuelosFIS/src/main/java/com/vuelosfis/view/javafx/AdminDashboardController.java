package com.vuelosfis.view.javafx;

import com.vuelosfis.model.Reserva;
import java.io.File;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.stage.FileChooser;

public class AdminDashboardController implements Initializable {

    @FXML
    private TableView<Reserva> reservationTable;
    @FXML
    private TableColumn<Reserva, String> colId;
    @FXML
    private TableColumn<Reserva, String> colCliente;
    @FXML
    private TableColumn<Reserva, String> colVuelo;

    @FXML
    private TableColumn<Reserva, String> colEstado;
    @FXML
    private TableColumn<Reserva, String> colEstadoVuelo;
    @FXML
    private TableColumn<Reserva, String> colAvion;
    @FXML
    private TableColumn<Reserva, String> colTripulacion;
    @FXML
    private TableColumn<Reserva, String> colAsiento;
    @FXML
    private TableColumn<Reserva, String> colClase;
    @FXML
    private TableColumn<Reserva, String> colItinerario;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTable();
        loadData();
    }

    private void setupTable() {
        // 1. Reserva Info
        colId.setCellValueFactory(cellData -> new SimpleStringProperty("RES-" + cellData.getValue().hashCode()));
        colCliente.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getCliente().getNombre()));

        // colFecha removed as requested/replaced by detailed columns
        colEstado.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEstado().toString()));

        // 2. Vuelo Info (via First Pasaje)
        colVuelo.setCellValueFactory(cellData -> {
            if (!cellData.getValue().getPasajes().isEmpty()) {
                return new SimpleStringProperty(cellData.getValue().getPasajes().get(0).getVuelo().getNumeroVuelo());
            }
            return new SimpleStringProperty("-");
        });

        colEstadoVuelo.setCellValueFactory(cellData -> {
            if (!cellData.getValue().getPasajes().isEmpty()) {
                return new SimpleStringProperty(
                        cellData.getValue().getPasajes().get(0).getVuelo().getEstado().toString());
            }
            return new SimpleStringProperty("-");
        });

        colAvion.setCellValueFactory(cellData -> {
            if (!cellData.getValue().getPasajes().isEmpty()) {
                if (cellData.getValue().getPasajes().get(0).getVuelo().getAvion() != null) {
                    return new SimpleStringProperty(
                            cellData.getValue().getPasajes().get(0).getVuelo().getAvion().getModelo());
                }
            }
            return new SimpleStringProperty("-");
        });

        colTripulacion.setCellValueFactory(cellData -> {
            if (!cellData.getValue().getPasajes().isEmpty()) {
                java.util.List<com.vuelosfis.model.Tripulacion> trip = cellData.getValue().getPasajes().get(0)
                        .getVuelo().getTripulacion();
                if (trip != null && !trip.isEmpty()) {
                    return new SimpleStringProperty(trip.get(0).getNombre() + "...");
                }
            }
            return new SimpleStringProperty("Sin Asignar");
        });

        // 3. Pasaje Info (Asiento/Clase)
        colAsiento.setCellValueFactory(cellData -> {
            if (!cellData.getValue().getPasajes().isEmpty()) {
                return new SimpleStringProperty(cellData.getValue().getPasajes().get(0).getAsiento().getCodigo());
            }
            return new SimpleStringProperty("-");
        });

        colClase.setCellValueFactory(cellData -> {
            if (!cellData.getValue().getPasajes().isEmpty()) {
                return new SimpleStringProperty(
                        cellData.getValue().getPasajes().get(0).getAsiento().getClase().toString());
            }
            return new SimpleStringProperty("-");
        });

        // 4. Itinerario (Calculated or Mocked)
        colItinerario.setCellValueFactory(cellData -> {
            if (!cellData.getValue().getPasajes().isEmpty()) {
                com.vuelosfis.model.Vuelo v = cellData.getValue().getPasajes().get(0).getVuelo();
                return new SimpleStringProperty(v.getOrigen().getCodigoIATA() + "-" + v.getDestino().getCodigoIATA()
                        + " (" + v.getDuracionEstimada().toHours() + "h)");
            }
            return new SimpleStringProperty("-");
        });
    }

    private void loadData() {
        if (JavaFXApp.getController().getTodasLasReservas() != null) {
            reservationTable.getItems().setAll(JavaFXApp.getController().getTodasLasReservas());
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Welcome.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleGenerateReport(ActionEvent event) {
        System.out.println("---- REPORTE DE RESERVAS ----");
        for (Reserva r : reservationTable.getItems()) {
            System.out.println("Reserva: " + r.getResumen());
        }
        System.out.println("-----------------------------");
    }

    @FXML
    private void handleUploadCSV(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importar Reservas");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            procesarCSV(file);
        }
    }

    private void procesarCSV(File file) {
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(file))) {
            String line;
            boolean isFirstLine = true;
            int count = 0;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                try {
                    String[] data = line.split(",");
                    if (data.length < 10) {
                        continue;
                    }

                    com.vuelosfis.model.Cliente cliente = new com.vuelosfis.model.Cliente(data[0].trim(), data[1].trim(), "cliente@vuelos.com");

                    com.vuelosfis.model.Aeropuerto ori = new com.vuelosfis.model.Aeropuerto("ORI", "Origen");
                    com.vuelosfis.model.Aeropuerto des = new com.vuelosfis.model.Aeropuerto("DES", "Destino");
                    com.vuelosfis.model.Avion avion = new com.vuelosfis.model.Avion("MAT-" + data[3], data[5].trim());

                    com.vuelosfis.model.Vuelo vuelo = new com.vuelosfis.model.Vuelo(
                            data[3].trim(), ori, des,
                            java.time.LocalDateTime.now(),
                            java.time.LocalDateTime.now().plusHours(2),
                            avion
                    );

                    String claseStr = data[8].trim().toUpperCase().replace("CLASS", "CLASE");
                    com.vuelosfis.model.ClaseAsiento claseAsiento = com.vuelosfis.model.ClaseAsiento.valueOf(claseStr);
                    com.vuelosfis.model.Asiento asiento = new com.vuelosfis.model.Asiento(data[7].trim(), claseAsiento);

                    com.vuelosfis.model.Pasajero pasajero = new com.vuelosfis.model.Pasajero(data[1].trim(), "cliente@vuelos.com");
                    com.vuelosfis.model.Pasaje pasaje = new com.vuelosfis.model.Pasaje(pasajero, vuelo);
                    pasaje.seleccionarAsiento(asiento);

                    com.vuelosfis.model.Reserva reserva = new com.vuelosfis.model.Reserva(cliente);
                    reserva.agregarPasaje(pasaje);

                    javafx.application.Platform.runLater(() -> {
                        reservationTable.getItems().add(reserva);
                    });

                    count++;

                } catch (Exception e) {
                    System.err.println("Error en línea [" + line + "]: " + e.getMessage());
                }
            }

            int finalCount = count;
            javafx.application.Platform.runLater(() -> {
                mostrarAlerta("Éxito", "Se cargaron " + finalCount + " reservas correctamente.");
            });

        } catch (Exception e) {
            mostrarAlerta("Error", "Error al leer el archivo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
