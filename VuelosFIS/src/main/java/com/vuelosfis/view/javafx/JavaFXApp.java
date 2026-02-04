package com.vuelosfis.view.javafx;

import com.vuelosfis.controller.SistemaController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JavaFXApp extends Application {

    private static SistemaController sistemaController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize the controller one time
        sistemaController = new SistemaController();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Welcome.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

        primaryStage.setTitle("VuelosFIS - Sistema de Gestión de Vuelos");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static SistemaController getController() {
        return sistemaController;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
