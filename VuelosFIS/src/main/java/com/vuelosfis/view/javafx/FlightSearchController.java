package com.vuelosfis.view.javafx;

import com.vuelosfis.model.Vuelo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FlightSearchController implements Initializable {

    @FXML
    private ListView<Vuelo> flightListView;

    @FXML
    private Button bookButton;

    @FXML
    private Label statusLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Load flights from the main controller
        if (JavaFXApp.getController().getVuelos() != null) {
            flightListView.getItems().addAll(JavaFXApp.getController().getVuelos());
        } else {
            statusLabel.setText("No se pudieron cargar los vuelos.");
        }

        // Custom Cell Factory to make it look good
        flightListView.setCellFactory(new Callback<ListView<Vuelo>, ListCell<Vuelo>>() {
            @Override
            public ListCell<Vuelo> call(ListView<Vuelo> param) {
                return new ListCell<Vuelo>() {
                    @Override
                    protected void updateItem(Vuelo item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            // Create a "Ticket" styling layout
                            javafx.scene.layout.VBox card = new javafx.scene.layout.VBox(5);
                            card.getStyleClass().add("card");
                            card.setStyle(
                                    "-fx-background-color: white; -fx-padding: 10; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);");

                            javafx.scene.layout.HBox header = new javafx.scene.layout.HBox(10);
                            header.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

                            javafx.scene.control.Label routeLabel = new javafx.scene.control.Label(
                                    item.getOrigen().getCodigoIATA() + " ➝ " + item.getDestino().getCodigoIATA());
                            routeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

                            javafx.scene.control.Label timeLabel = new javafx.scene.control.Label(
                                    "Fecha: " + item.getFechaHoraSalida().toString().replace("T", " "));
                            timeLabel.setStyle("-fx-text-fill: #7f8c8d;");

                            javafx.scene.layout.Region spacer = new javafx.scene.layout.Region();
                            javafx.scene.layout.HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

                            header.getChildren().addAll(routeLabel, spacer, timeLabel);

                            javafx.scene.control.Label detailsLabel = new javafx.scene.control.Label(
                                    "Vuelo: " + item.getNumeroVuelo() + " | Duración: " + item.getDuracionEstimada());
                            detailsLabel.setStyle("-fx-text-fill: #34495e;");

                            card.getChildren().addAll(header, detailsLabel);

                            setGraphic(card);
                            setText(null);
                        }
                    }
                };
            }
        });

        // Enable book button only when selected
        flightListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            bookButton.setDisable(newVal == null);
        });
    }

    @FXML
    private void handleBack(ActionEvent event) {
        navigateTo(event, "/fxml/Welcome.fxml");
    }

    @FXML
    private void handleBookFlight(ActionEvent event) {
        Vuelo selected = flightListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            navigateToBooking(event, selected);
        }
    }

    private void navigateTo(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
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

    private void navigateToBooking(ActionEvent event, Vuelo vuelo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Booking.fxml"));
            Parent root = loader.load();

            // Pass the selected flight to the booking controller
            BookingController controller = loader.getController();
            controller.setFlight(vuelo);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
