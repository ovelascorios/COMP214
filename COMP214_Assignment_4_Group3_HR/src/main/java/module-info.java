module com.opsdevelop.comp214_assignment_4_group3_hr {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;

    opens com.opsdevelop.comp214_assignment_4_group3_hr to javafx.fxml;
    exports com.opsdevelop.comp214_assignment_4_group3_hr;
}