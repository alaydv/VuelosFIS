package com.vuelosfis.view.javafx;

import com.vuelosfis.model.Asiento;
import com.vuelosfis.model.Avion;
import com.vuelosfis.model.Vuelo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.scene.Node;

public class SeatSelectionController {

    @FXML
    private GridPane seatGrid;

    @FXML
    private Button confirmarButton;

    @FXML
    private Button continuarButton;

    private Avion avion;
    private Vuelo vuelo;
    private Asiento asientoSeleccionado;

    public void setVuelo(Vuelo vuelo) {
        this.vuelo = vuelo;
        setAvion(vuelo.getAvion());
    }

    public void setAvion(Avion avion) {
        this.avion = avion;
        seatGrid.getChildren().clear();

        int row = 0;
        int col = 0;

        // Recorremos todos los asientos del avión
        for (Asiento a : avion.getAsientos()) {
            // El texto del botón será el código del asiento (ej: 1A, 1B...)
            Button seatBtn = new Button(a.getCodigo());
            seatBtn.setPrefSize(50, 50);

            if (a.isDisponible()) {
                seatBtn.getStyleClass().add("seat-available");
                seatBtn.setOnAction(e -> {
                    asientoSeleccionado = a;
                    // reset estilos de todos los botones
                    seatGrid.getChildren().forEach(node -> {
                        node.getStyleClass().remove("seat-selected");
                        if (node instanceof Button && !((Button) node).isDisabled()) {
                            if (!node.getStyleClass().contains("seat-available")) {
                                node.getStyleClass().add("seat-available");
                            }
                        }
                    });
                    seatBtn.getStyleClass().remove("seat-available");
                    seatBtn.getStyleClass().add("seat-selected");
                });
            } else {
                seatBtn.setDisable(true);
                seatBtn.getStyleClass().add("seat-occupied");
            }

            // Añadimos el botón al GridPane
            seatGrid.add(seatBtn, col, row);

            col++;
            if (col == 6) { // ejemplo: 6 columnas (A-F)
                col = 0;
                row++;
            }
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FlightSearch.fxml"));
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
    private void initialize() {
        confirmarButton.setOnAction(e -> {
            if (asientoSeleccionado != null) {
                asientoSeleccionado.reservar();
                confirmarButton.setDisable(true);
                Alert alert = new Alert(Alert.AlertType.INFORMATION,
                        "Asiento reservado: " + asientoSeleccionado.getCodigo());
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING,
                        "Selecciona un asiento disponible antes de confirmar.");
                alert.showAndWait();
            }
        });

        continuarButton.setOnAction(e -> {
            if (asientoSeleccionado != null) {
                try {
                    asientoSeleccionado.reservar(); // asegura reserva antes de continuar

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Booking.fxml"));
                    Parent root = loader.load();

                    BookingController controller = loader.getController();
                    controller.setFlight(vuelo);
                    controller.setAsiento(asientoSeleccionado);

                    Scene scene = new Scene(root);
                    scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

                    Stage bookingStage = (Stage) continuarButton.getScene().getWindow();
                    bookingStage.setScene(scene);
                    bookingStage.show();

                } catch (IOException ex) {
                    ex.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR,
                            "Error al cargar la pantalla de reserva.");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING,
                        "Debes seleccionar un asiento antes de continuar.");
                alert.showAndWait();
            }
        });
    }
}