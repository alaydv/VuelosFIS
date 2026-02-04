module com.vuelosfis {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.vuelosfis.view.javafx to javafx.fxml;

    exports com.vuelosfis;
    exports com.vuelosfis.view.javafx;
    exports com.vuelosfis.model;
    exports com.vuelosfis.controller;
}
