module cashflow.ui {

    requires com.fasterxml.jackson.databind;
    requires java.net.http;
    requires cashflow.core;

    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;


    opens ui to javafx.graphics, javafx.fxml;

}
