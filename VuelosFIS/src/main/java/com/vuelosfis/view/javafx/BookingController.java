package com.vuelosfis.view.javafx;

import com.vuelosfis.controller.SistemaController;
import com.vuelosfis.exception.SaldoInsuficienteException;
import com.vuelosfis.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class BookingController {

    @FXML
    private Label flightInfoLabel;
    @FXML
    private TextField clientIdField;
    @FXML
    private Label lifeMilesLabel;
    @FXML
    private Label lifeMilesMessageLabel;
    @FXML
    private TextField paxNameField;
    @FXML
    private TextField paxPassportField;

    // Payment UI
    @FXML
    private ToggleGroup paymentGroup;
    @FXML
    private RadioButton payCard;
    @FXML
    private RadioButton payPayPal;
    @FXML
    private RadioButton payLifeMiles;

    @FXML
    private VBox cardDetailsBox;
    @FXML
    private TextField cardNumberField;
    @FXML
    private TextField cardHolderField;
    @FXML
    private TextField cardExpiryField;
    @FXML
    private TextField cardCvvField;

    @FXML
    private VBox paypalDetailsBox;
    @FXML
    private TextField paypalEmailField;
    @FXML
    private PasswordField paypalTokenField;

    @FXML
    private VBox lifemilesDetailsBox;
    @FXML
    private TextField lifemilesAccountField;

    // Extras
    @FXML
    private CheckBox extraLuggageCheck;
    @FXML
    private CheckBox premiumFoodCheck;

    @FXML
    private Label messageLabel;

    private Vuelo selectedFlight;
    private SistemaController sistemaController;
    
    //Asiento escogido
    private Asiento asientoSeleccionado;

    public void setAsiento(Asiento asiento) {
    this.asientoSeleccionado = asiento;
    }

    private void updatePaymentVisibility() {
        cardDetailsBox.setVisible(payCard.isSelected());
        cardDetailsBox.setManaged(payCard.isSelected());

        paypalDetailsBox.setVisible(payPayPal.isSelected());
        paypalDetailsBox.setManaged(payPayPal.isSelected());

        lifemilesDetailsBox.setVisible(payLifeMiles.isSelected());
        lifemilesDetailsBox.setManaged(payLifeMiles.isSelected());
    }

    @FXML
    public void initialize() {
        sistemaController = JavaFXApp.getController();

        // Listeners for toggle
        paymentGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> updatePaymentVisibility());
        updatePaymentVisibility(); // init state
        
        // Estado inicial de LifeMiles
        lifeMilesLabel.setText("LifeMiles: ---");
        lifeMilesMessageLabel.setText("");
        payLifeMiles.setDisable(true);
    }
    
    @FXML
    private void onClientIdChanged() {

    String clientId = clientIdField.getText().trim();

    // Reset UI si está vacío
        if (clientId.isEmpty()) {
            lifeMilesLabel.setText("LifeMiles: ---");
            lifeMilesMessageLabel.setText("");
            payLifeMiles.setDisable(true);
        return;
        }

    // Buscar cliente
        Cliente cliente = sistemaController.buscarCliente(clientId);

        if (cliente == null || cliente.getCuentaLifeMiles() == null) {
            lifeMilesLabel.setText("LifeMiles: 0");
            lifeMilesMessageLabel.setText("Cliente sin cuenta LifeMiles");
            lifeMilesMessageLabel.setStyle("-fx-text-fill: red;");
            payLifeMiles.setDisable(true);
        return;
        }

        int millas = cliente.getCuentaLifeMiles().getSaldo();

        lifeMilesLabel.setText("LifeMiles: " + millas);

    // Precio base fijo por ahora (luego lo mejoramos)
        double precioEstimado = 100;

        if (millas >= precioEstimado) {
            lifeMilesMessageLabel.setText("✔ Millas suficientes para este vuelo");
            lifeMilesMessageLabel.setStyle("-fx-text-fill: green;");
            payLifeMiles.setDisable(false);
        } else {
            lifeMilesMessageLabel.setText("✖ Millas insuficientes");
            lifeMilesMessageLabel.setStyle("-fx-text-fill: red;");
            payLifeMiles.setDisable(true);
        }
}

    

    public void setFlight(Vuelo vuelo) {
        this.selectedFlight = vuelo;
        if (vuelo != null) {
            flightInfoLabel.setText(String.format("%s -> %s\nVuelo %s at %s\nPrecio Base: $100.00",
                    vuelo.getOrigen().getCodigoIATA(),
                    vuelo.getDestino().getCodigoIATA(),
                    vuelo.getNumeroVuelo(),
                    vuelo.getFechaHoraSalida()));
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
    private void handleConfirm(ActionEvent event) {
        
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Invoice.fxml"));
        Parent root = loader.load();

        // Si quieres pasar datos, puedes usar:
        // InvoiceController controller = loader.getController();
        // controller.setFactura(factura); // si tienes una factura lista

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Factura de Reserva");
        stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Error al cargar la vista de factura.");
        }

        messageLabel.setText("");
        messageLabel.setStyle("-fx-text-fill: red;");

        // 1. Validate Common Fields
        String paxName = paxNameField.getText();
        String passport = paxPassportField.getText();

        if (paxName.isEmpty() || passport.isEmpty()) {
            messageLabel.setText("Por favor complete los datos del pasajero.");
            return;
        }

        try {
            // 2. Prepare Entities (Ensure unique Guest ID)
            Cliente cliente = sistemaController.buscarCliente(clientIdField.getText());
            if (cliente == null) {
                // Generate a unique ID to FORCE creation of a new guest record with the entered
                // name
                String guestId = clientIdField.getText().isEmpty() ? "GUEST-" + System.currentTimeMillis()
                        : clientIdField.getText();
                cliente = sistemaController.registrarClienteInvitado(guestId, paxName, "guest@vuelosfis.com");
            } else {
                System.out.println("Cliente encontrado: " + cliente.getNombre());
            }

            Pasajero pasajero = new Pasajero(paxName, passport);

            // Asiento: For simplicity assign a default one or random
            Asiento asiento = asientoSeleccionado;
            if (asiento == null) {
            messageLabel.setText("No se ha seleccionado un asiento.");
            return;
            }

            // 3. Create Basic Reservation
            Reserva reserva = sistemaController.crearReserva(selectedFlight, cliente, pasajero, asiento);

            // Set Notification Service (Using previously unused INotificationService)
            reserva.setNotificador(new EmailNotificationService());

            // Assign Dummy Crew (Using previously unused classes
            // Piloto/TripulanteCabina/Tripulacion)
            try {
                // Constructor: id, nombre, fechaContratacion, licencia, rango
                Piloto dummyPilot = new Piloto("P-001", "Capitán Dummy", new java.util.Date(), "LIC-999", "Senior");
                TripulanteCabina dummyCrew = new TripulanteCabina("TC-001", "Tripulante Mock", new java.util.Date(),
                        new java.util.ArrayList<>());
                // We check if flight already has crew to avoid duplicates
                if (selectedFlight.getTripulacion().isEmpty()) {
                    selectedFlight.asignarTripulacion(dummyPilot);
                    selectedFlight.asignarTripulacion(dummyCrew);
                }
                System.out.println("Tripulación asignada: " + dummyPilot.getNombre() + ", " + dummyCrew.getNombre());
            } catch (Exception ignored) {
            }

            // 4. Add Services (Using previously unused classes
            // ComidaPremium/EquipajeAdicional)
            Pasaje pasaje = reserva.getPasajes().get(0); // The one we just added
            if (extraLuggageCheck.isSelected()) {
                pasaje.agregarServicio(new EquipajeAdicional(23.0, "Standard Check-in"));
            }
            if (premiumFoodCheck.isSelected()) {
                pasaje.agregarServicio(new ComidaPremium("Gourmet Dinner", false));
            }

            // 5. Payment Strategy Logic (Using unused IPaymentStrategy implementations)
            IPaymentStrategy strategy = null;
            if (payCard.isSelected()) {
                if (cardNumberField.getText().isEmpty())
                    throw new IllegalArgumentException("Datos de tarjeta requeridos");
                strategy = new PagoTarjetaCredito(cardNumberField.getText(), cardHolderField.getText(),
                        cardExpiryField.getText(), cardCvvField.getText());
            } else if (payPayPal.isSelected()) {
                if (paypalEmailField.getText().isEmpty())
                    throw new IllegalArgumentException("Email de PayPal requerido");
                strategy = new PagoPayPal(paypalEmailField.getText(), paypalTokenField.getText());
            } else if (payLifeMiles.isSelected()) {

                Cliente clienteLifeMiles = sistemaController.buscarCliente(clientIdField.getText());

                if (clienteLifeMiles == null || clienteLifeMiles.getCuentaLifeMiles() == null) {
                    throw new IllegalArgumentException("El cliente no tiene cuenta LifeMiles");
                }

                strategy = new PagoLifeMiles(
                        clienteLifeMiles.getCuentaLifeMiles(),
                        0.01 // conversión: 1 milla = $0.01
                );
            }
 

            // 6. Execute Payment
            if (strategy == null) {
                throw new IllegalStateException("Debe seleccionar un método de pago");
            }
            sistemaController.pagarReserva(reserva, strategy);

            // 7. Use Itinerario Class (Robustness)
            Itinerario itinerario = new Itinerario("IT-" + System.currentTimeMillis());
            itinerario.agregarVuelo(selectedFlight);

            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("¡Reserva Confirmada para " + cliente.getNombre() + "!\nTotal: $"
                    + String.format("%.2f", reserva.getPago().getMonto()));

            // Check LifeMiles accumulation (Using previously "unused" method)
            if (cliente.getCuentaLifeMiles() != null) {
                cliente.getCuentaLifeMiles().acumularMillas((int) reserva.getPago().getMonto());
            }

        } catch (SaldoInsuficienteException e) {
            messageLabel.setText("Error de pago: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error: " + e.getMessage());
        }
    }
}
