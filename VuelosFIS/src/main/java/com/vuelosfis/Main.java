package com.vuelosfis;

import com.vuelosfis.controller.SistemaController;
import com.vuelosfis.view.ConsoleView;
import com.vuelosfis.view.pantallaInicial;

public class Main {

    public static void main(String[] args) {
        // Inicializamos la vista de consola para ver logs
        ConsoleView cv = new ConsoleView();

        // Creamos el controlador (esto carga automáticamente los .txt)
        SistemaController controller = new SistemaController(cv);

        // Iniciamos la primera pantalla de la GUI
        java.awt.EventQueue.invokeLater(() -> {
            new pantallaInicial(controller).setVisible(true);
        });
    }
}
