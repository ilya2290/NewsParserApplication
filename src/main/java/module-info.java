module org.example.newsparserapplication {
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
    requires static lombok;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires org.slf4j;

    opens org.example.newsparserapplication.news to com.fasterxml.jackson.databind, com.fasterxml.jackson.datatype.jsr310;
    opens org.example.newsparserapplication.main to javafx.fxml;
    exports org.example.newsparserapplication.main;
    exports org.example.newsparserapplication.news;


}