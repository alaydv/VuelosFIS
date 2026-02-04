package com.vuelosfis.view.javafx;

import com.vuelosfis.model.Asiento;
import com.vuelosfis.model.Avion;
import com.vuelosfis.model.ClaseAsiento;
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
import javafx.scene.control.Label;

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

        int filasPorClase = 5;
        int columnas = 4;
        char[] letras = {'A', 'B', 'C', 'D'};

        ClaseAsiento[] clases = {
            ClaseAsiento.PRIMERA_CLASE,
            ClaseAsiento.EJECUTIVA,
            ClaseAsiento.ECONOMICA
        };

        int rowOffset = 0;

        for (ClaseAsiento clase : clases) {
            // Etiqueta de clase
            Label claseLabel = new Label(clase.toString().replace("_", " "));
            claseLabel.getStyleClass().add("class-label");
            seatGrid.add(claseLabel, 0, rowOffset, columnas, 1);
            rowOffset++;

            for (int fila = 1; fila <= filasPorClase; fila++) {
                for (int col = 0; col < columnas; col++) {
                    char letra = letras[col];
                    String codigo = fila + String.valueOf(letra); // ej: "1A", "2B"

                    // Buscar asiento en el modelo o crear uno nuevo
                    Asiento asiento = avion.getAsientos().stream()
                        .filter(a -> a.getCodigo().equals(codigo) && a.getClase() == clase)
                        .findFirst()
                        .orElse(new Asiento(codigo, fila, letra, clase, "General"));

                    Button seatBtn = new Button(codigo);
                    seatBtn.setPrefSize(60, 60);

                    // Estilo por clase
                    switch (clase) {
                        case PRIMERA_CLASE -> seatBtn.getStyleClass().add("seat-primera");
                        case EJECUTIVA -> seatBtn.getStyleClass().add("seat-ejecutiva");
                        case ECONOMICA -> seatBtn.getStyleClass().add("seat-economica");
                    }

                    if (asiento.isDisponible()) {
                        seatBtn.setOnAction(e -> {
                            asientoSeleccionado = asiento;

                            seatGrid.getChildren().forEach(node -> {
                                if (node instanceof Button) {
                                    node.getStyleClass().remove("seat-selected");
                                }
                            });

                            seatBtn.getStyleClass().add("seat-selected");
                        });
                    } else {
                        seatBtn.setDisable(true);
                        seatBtn.getStyleClass().add("seat-occupied");
                    }

                    seatGrid.add(seatBtn, col, rowOffset + fila - 1);
                }
            }

            rowOffset += filasPorClase;
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