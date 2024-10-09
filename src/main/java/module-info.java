module com.demo.epromissorias {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires jakarta.persistence;

    opens com.demo.epromissorias to javafx.fxml;
    exports com.demo.epromissorias;
    exports com.demo.epromissorias.controllers;
    opens com.demo.epromissorias.controllers to javafx.fxml;
}