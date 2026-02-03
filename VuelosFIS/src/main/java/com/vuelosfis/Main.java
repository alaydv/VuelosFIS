package com.vuelosfis;

import com.vuelosfis.controller.SistemaController;
import com.vuelosfis.view.ConsoleView;

public class Main {
    public static void main(String[] args) {
        ConsoleView view = new ConsoleView();
        SistemaController controller = new SistemaController(view);
        controller.iniciar();
    }
}
