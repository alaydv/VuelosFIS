package com.vuelosfis.view.javafx;

import com.vuelosfis.model.Reserva;
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
                if (cellData.getValue().getPasajes().get(0).getVuelo().getAvion() != null)
                    return new SimpleStringProperty(
                            cellData.getValue().getPasajes().get(0).getVuelo().getAvion().getModelo());
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
}
