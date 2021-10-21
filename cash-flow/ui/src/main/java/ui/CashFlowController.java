package ui;


import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import json.CashFlowPersistence;

import java.io.IOException;

import core.User;
import core.AbstractAccount;
import core.BSUAccount;
import core.CheckingAccount;
import core.SavingsAccount;
import core.BankHelper;
import javafx.scene.control.ChoiceBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class CashFlowController {

@FXML TextField navnKonto, settBelop;
@FXML TextArea kontoer;
@FXML Button opprettKonto, detaljerOgOverforinger;
@FXML Text kontoOpprettet, feilmelding;
@FXML ChoiceBox<String> typeKonto;

private User user = new User(123456);
private CashFlowPersistence cfp = new CashFlowPersistence();
private BankHelper bankHelper = new BankHelper();

public void initialize() {
    kontoer.setEditable(false);
    setDropDownMenu();
    try {
        user = cfp.loadUser("SaveData.json");
    } catch (IllegalStateException e) {
        feilmelding.setText("Noe gikk galt! Fant ikke lagret brukerdata.");
    } catch (IOException e) {
        feilmelding.setText("Noe gikk galt! Fant ikke lagret brukerdata.");
    }
    updateAccountView();
}

private void setDropDownMenu() {
    typeKonto.getItems().clear();
    typeKonto.getItems().add("Brukskonto");
    typeKonto.getItems().add("Sparekonto");
    if (!bankHelper.hasBSU(user)){
        typeKonto.getItems().add("BSU-konto");
    }
}

@FXML
public void onCreateAccount() {
    String name = navnKonto.getText();
    String amount = settBelop.getText();
    if (typeKonto.getValue() == null){
        feilmelding.setText("Velg en kontotype!");
    }
    else if (name.isBlank() || amount.isBlank()) {
        clear();
        feilmelding.setText("Husk å fylle inn alle felt");
    }

    else if (checkValidNameAmount(name, null) == false) {
        clear();
        feilmelding.setText("Du kan ikke bruke tall eller tegn i navnet, og det må være mindre enn 20 bokstaver");
    }
    
    else if (checkValidNameAmount(null, amount) == false) {
        clear();
        feilmelding.setText("Beløpet må bestå av tall og kan ikke være mindre enn null");
    }

    
    else {
        clear();
        String type = (String) typeKonto.getValue();
        double balance = Double.parseDouble(settBelop.getText());
        AbstractAccount account = getAccountFromType(type, name, balance);
        user.addAccount(account);
        updateAccountView();
        kontoOpprettet.setText("Kontoen er opprettet");
        navnKonto.setText("");
        settBelop.setText("");
        save();
    } 
}

private boolean checkValidNameAmount(String name, String amount) {
    if (name == null && amount != null) {
        if (isNumeric(amount)) {
            return bankHelper.isPositiveAmount(Double.parseDouble(amount));
        }
        else {
            return false;
        }
        
    }
    else if (name != null && amount == null) {
        return bankHelper.isValidName(name);
    }
   else {
       return false;
   }
}

private AbstractAccount getAccountFromType(String type, String name, double balance){
    switch (type){
        case "Brukskonto":
            return new CheckingAccount(name, balance, user);
        case "Sparekonto":
            return new SavingsAccount(name, balance, user);
        case "BSU-konto":
            return new BSUAccount(name, balance, user);
    }
    return null;
}

@FXML
private void clear() {
    kontoOpprettet.setText("");
    feilmelding.setText("");
}

@FXML
private boolean isNumeric(String s){
    try {
        Double.parseDouble(s);
        return true;
    } catch (NumberFormatException e) {
        return false;
    }
}


@FXML
private void updateAccountView(){
    kontoer.setText("");
    for (AbstractAccount account : user.getAccounts()) {
        String type = "";
        if (account instanceof CheckingAccount){
            type = "Brukskonto";
        }
        else if (account instanceof SavingsAccount){
            type = "Sparekonto";
        }
        else if (account instanceof BSUAccount){
            type = "BSU-konto";
        }
        String name = account.getName();
        String balance = String.valueOf(account.getBalance());
        kontoer.setText(kontoer.getText() + "\n" + type + ": " + name + "\n" + "   Beløp: " + balance);
    }
    setDropDownMenu();
}

private void save() {
    try {
        cfp.saveUser(user);
    } catch (IllegalStateException e) {
        feilmelding.setText("Bankkontoene ble ikke lagret.");
    } catch (IOException e) {
        feilmelding.setText("Bankkontoene ble ikke lagret.");
    }
}

private void load() {
    try {
        user = cfp.loadUser();
    } catch (IllegalStateException e) {
        feilmelding.setText("Bankkontoene ble ikke funnet.");
    } catch (IOException e) {
        feilmelding.setText("Bankkontoene ble ikke funnet.");
    }
}

public void loadNewUser(String saveFile) {
    cfp.setSaveFilePath(saveFile);
    load();
    updateAccountView();
}

@FXML
private void onNextPage() throws IOException {
    if (!user.getAccounts().isEmpty()) {
        Stage stage = (Stage) detaljerOgOverforinger.getScene().getWindow();
        stage.close();
        Stage primaryStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Details.fxml"));
        Parent parent = fxmlLoader.load();
        primaryStage.setScene(new Scene(parent));
        primaryStage.show();
    }
    else {
        feilmelding.setText("Opprett en konto for å kunne se kontodetaljer og overføringer!");
    }
}   

}
