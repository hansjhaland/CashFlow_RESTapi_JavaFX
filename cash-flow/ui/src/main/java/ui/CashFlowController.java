package ui;


import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import json.CashFlowPersistence;

import java.io.IOException;
import java.text.DecimalFormat;

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

/** 
 * Controller for creating an account
 */

public class CashFlowController {

@FXML TextField nameAccount, setAmount;
@FXML TextArea accounts;
@FXML Button createAccount, detailsAndTransfers;
@FXML Text accountCreated, errorMessage;
@FXML ChoiceBox<String> accountType;

private User user = new User(123456);
private CashFlowPersistence cfp = new CashFlowPersistence();
private BankHelper bankHelper = new BankHelper();

public void initialize() {
    accounts.setEditable(false);
    setDropDownMenu();
    try {
        user = cfp.loadUser("SaveData.json");
    } catch (IllegalStateException e) {
        errorMessage.setText("Noe gikk galt! Fant ikke lagret brukerdata.");
    } catch (IOException e) {
        errorMessage.setText("Noe gikk galt! Fant ikke lagret brukerdata.");
    }
    updateAccountView();
}

/**
 * Sets the drop-down menu for the accounts you can choose between
 */
private void setDropDownMenu() {
    accountType.getItems().clear();
    accountType.getItems().add("Brukskonto");
    accountType.getItems().add("Sparekonto");
    if (!bankHelper.hasBSU(user)){
        accountType.getItems().add("BSU-konto");
    }
}

/**
 * Method for creating account.
 */
@FXML
public void onCreateAccount() {
    String name = nameAccount.getText();
    String amount = setAmount.getText();
    if (accountType.getValue() == null){
        errorMessage.setText("Velg en kontotype!");
    }
    else if (name.isBlank() || amount.isBlank()) {
        clear();
        errorMessage.setText("Husk å fylle inn alle felt");
    }

    else if (checkValidNameAmount(name, null) == false) {
        clear();
        errorMessage.setText("Du kan ikke bruke tall eller tegn i navnet, og det må være mindre enn 20 bokstaver");
    }
    
    else if (checkValidNameAmount(null, amount) == false) {
        clear();
        errorMessage.setText("Beløpet må bestå av tall og kan ikke være mindre enn null");
    }

    
    else {
        clear();
        String type = (String) accountType.getValue();
        double balance = Double.parseDouble(setAmount.getText());
        AbstractAccount account = getAccountFromType(type, name, balance);
        clear();
        errorMessage.setText("Nå ble ikke kontotypen satt rett. Prøv på nytt");
        user.addAccount(account);
        updateAccountView();
        accountCreated.setText("Kontoen er opprettet");
        nameAccount.setText("");
        nameAccount.setText("");
        save();
    } 
}


/**
 * Method for checking if the accountname is valid.
 * @param name the name to be checked
 * @param amount the amount to be checked
 * @return {@code true} if the name and amount satisfies the rules
 */
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

/**
 * Checks which account type is chosen
 * @param type the chosen account type
 * @param name the chosen account name
 * @param balance the chosen balance
 * @return {@code null} if the account type is not found
 */
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
    //Clears all comments on the page
    accountCreated.setText("");
    errorMessage.setText("");
}

/**
 * Checks if the chosen amount is a numeric number
 * @param amount the written amount
 * @return {@code true} if the amount is numeric
 */
@FXML
private boolean isNumeric(String amount){
    try {
        Double.parseDouble(amount);
        return true;
    } catch (NumberFormatException e) {
        return false;
    }
}

@FXML
private void updateAccountView(){
    //Method for updating the accountview after changes
    accounts.setText("");
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
        DecimalFormat df = new DecimalFormat("#.##");
        String balance = df.format(account.getBalance());
        accounts.setText(accounts.getText() + "\n" + type + ": " + name + "\n" + "   Beløp: " + balance);
    }
    setDropDownMenu();
}

private void save() {
    //Saves the new user
    try {
        cfp.saveUser(user);
    } catch (IllegalStateException e) {
        errorMessage.setText("Bankkontoene ble ikke lagret.");
    } catch (IOException e) {
        errorMessage.setText("Bankkontoene ble ikke lagret.");
    }
}

private void load() {
    //checks if the user accounts is found
    try {
        user = cfp.loadUser();
    } catch (IllegalStateException e) {
        errorMessage.setText("Bankkontoene ble ikke funnet.");
    } catch (IOException e) {
        errorMessage.setText("Bankkontoene ble ikke funnet.");
    }
}

public void loadNewUser(String saveFile) {
    cfp.setSaveFilePath(saveFile);
    load();
    updateAccountView();
}

@FXML
private void onNextPage() throws IOException {
    //Method for accessing the next page
    if (!user.getAccounts().isEmpty()) {
        Stage stage = (Stage) detailsAndTransfers.getScene().getWindow();
        stage.close();
        Stage primaryStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Details.fxml"));
        Parent parent = fxmlLoader.load();
        primaryStage.setScene(new Scene(parent));
        primaryStage.show();
    }
    else {
        errorMessage.setText("Opprett en konto for å kunne se kontodetaljer og overføringer!");
    }
}   

}
