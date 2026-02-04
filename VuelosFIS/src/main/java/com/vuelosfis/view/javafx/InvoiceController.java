package com.vuelosfis.view.javafx;

import com.vuelosfis.model.Factura;
import com.vuelosfis.model.ServicioAdicional;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class InvoiceController {

    @FXML
    private Label pasajeroLabel;
    @FXML
    private Label documentoLabel;
    @FXML
    private Label vueloLabel;
    @FXML
    private Label rutaLabel;
    @FXML
    private Label fechaVueloLabel;
    @FXML
    private Label asientoLabel;
    @FXML
    private Label metodoPagoLabel;
    @FXML
    private Label totalLabel;
    @FXML
    private Label fechaEmisionLabel;
    @FXML
    private TextArea serviciosTextArea;
    @FXML
    private Button finalizarButton;

    public void setFactura(Factura factura) {
        pasajeroLabel.setText(factura.getNombrePasajero());
        documentoLabel.setText(factura.getDocumentoPasajero());
        vueloLabel.setText(factura.getNumeroVuelo());
        rutaLabel.setText(factura.getOrigen() + " → " + factura.getDestino());
        fechaVueloLabel.setText(factura.getFechaVuelo().toString());
        asientoLabel.setText(factura.getAsiento());
        metodoPagoLabel.setText(factura.getMetodoPago());
        totalLabel.setText("$" + String.format("%.2f", factura.getTotal()));
        fechaEmisionLabel.setText(factura.getFechaEmision().toString());

        StringBuilder servicios = new StringBuilder();
        for (ServicioAdicional s : factura.getServiciosAdicionales()) {
            servicios.append("- ").append(s.getDescripcion())
                     .append(" ($").append(String.format("%.2f", s.getCosto())).append(")\n");
        }
        serviciosTextArea.setText(servicios.toString());
    }

    @FXML
    private void initialize() {
        finalizarButton.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Welcome.fxml"));
                Parent root = loader.load();

                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

                Stage stage = (Stage) finalizarButton.getScene().getWindow();
                stage.setScene(scene);
                stage.show();

            } catch (IOException ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "No se pudo cargar la pantalla de bienvenida.");
                alert.showAndWait();
            }
        });
    }
}