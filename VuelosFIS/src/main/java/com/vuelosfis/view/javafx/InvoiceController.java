package com.vuelosfis.view.javafx;

import com.vuelosfis.model.Reserva;
import com.vuelosfis.model.Pasaje;
import com.vuelosfis.model.Vuelo;
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

    public void setReserva(Reserva reserva) {

    Pasaje p = reserva.getPasajes().get(0);
    Vuelo v = p.getVuelo();
    double total = p.calcularPrecioTotal();
    pasajeroLabel.setText(p.getPasajero().getNombre());
    documentoLabel.setText(p.getPasajero().getNroDocumento());

    vueloLabel.setText(v.getNumeroVuelo());
    rutaLabel.setText(
        v.getOrigen().getCodigoIATA() + " → " +
        v.getDestino().getCodigoIATA()
    );

    fechaVueloLabel.setText(v.getFechaHoraSalida().toString());
    asientoLabel.setText(p.getAsiento().getCodigo());

    metodoPagoLabel.setText(reserva.getPago().getEstado());
    totalLabel.setText("$" + String.format("%.2f", total));
    fechaEmisionLabel.setText(java.time.LocalDate.now().toString());

    StringBuilder servicios = new StringBuilder();
    for (ServicioAdicional s : p.getServiciosAdicionales()) {
        servicios.append("- ")
                 .append(s.getDescripcion())
                 .append(" ($")
                 .append(String.format("%.2f", s.getCosto()))
                 .append(")\n");
    }

    serviciosTextArea.setText(
        servicios.length() == 0 ? "Sin servicios adicionales" : servicios.toString()
    );
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