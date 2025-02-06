module com.example.cinema {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.jfoenix;
    requires kernel;
    requires layout;
    requires java.desktop;
    requires io;
    requires barcodes;


    opens com.example.cinema to javafx.fxml;
    exports com.example.cinema;
}