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
import core.Transaction;

public class DetailsController {

@FXML private TextField navnKonto, settBelop, overførBeløp;
@FXML private TextArea kontoer, kontoHistorikk;
@FXML private Button opprettKonto, detaljerOgOverforinger, tilHovedside, overfør;
@FXML private Text kontoOpprettet, feilmelding;
@FXML private ChoiceBox velgKonto, overførKonto;

private User user;
private AbstractAccount account;
private AbstractAccount accountToTransferTo;
private FileHandler fileHandler = new FileHandler();

public void initialize() {
    try {
        user = fileHandler.load();
    } catch (IllegalStateException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    updateTransferHistoryView();
    updateChooseAccountView();
}

private void updateTransferHistoryView() {
    String string = "";
    if (account != null) {
        for (Transaction transaction : account.getTransactionHistory()) {
            string += transaction.toString() + "\n";
        }
    }
    kontoHistorikk.setText(string);
}

private void updateChooseAccountView() {
    for (AbstractAccount account : user.getAccounts()) {
        velgKonto.getItems().add(account.getName() + ": " + account.getAccountNumber());
        overførKonto.getItems().add(account.getName() + ": " + account.getAccountNumber());
    }
}

@FXML
private void onChooseAccount() {
    String valueText = (String) velgKonto.getValue();
    int accountNumber = Integer.valueOf(valueText.split(": ")[1]);
    account = user.getAccount(accountNumber);
    updateTransferHistoryView();
}



@FXML
private void onTransfer() {
    if (account != null && accountToTransferTo != null) {
        account.transfer(accountToTransferTo, Integer.valueOf(overførBeløp.getText()));
        try {
            fileHandler.save(user);
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        updateTransferHistoryView();
    }
}

@FXML
private void onChooseAccountToTransferTo() {
    String valueText = (String) overførKonto.getValue();
    int accountNumber = Integer.valueOf(valueText.split(": ")[1]);
    accountToTransferTo = user.getAccount(accountNumber);
}

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
