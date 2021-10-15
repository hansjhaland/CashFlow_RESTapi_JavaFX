package ui;


import javafx.fxml.FXML;
import javafx.application.Application;


import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;
import json.CashFlowPersistence;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import core.User;
import core.AbstractAccount;
import core.CheckingAccount;

public class DetailsController {

@FXML private TextField navnKonto, settBelop, overførBeløp;
@FXML private TextArea kontoer, kontoHistorikk;
@FXML private Button opprettKonto, detaljerOgOverforinger, tilHovedside, overfør;
@FXML private Text kontoOpprettet, feilmelding;
@FXML private ChoiceBox velgKonto, overførKonto;

@FXML
private void onPreviousPage() throws IOException {
    Stage stage = (Stage) tilHovedside.getScene().getWindow();
    stage.close();
    Stage primaryStage = new Stage();
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CashFlow.fxml"));
    Parent parent = fxmlLoader.load();
    primaryStage.setScene(new Scene(parent));
    primaryStage.show();
    

}



}
