package ui;

import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

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

    @FXML
    TextField nameAccount, setAmount;
    @FXML
    TextArea accounts;
    @FXML
    Button createAccount, detailsAndTransfers;
    @FXML
    Text accountCreated, errorMessage;
    @FXML
    ChoiceBox<String> accountType;

    private User user;

    private CashFlowAccess cashFlowAccess;

    public void setCashFlowAccess(CashFlowAccess cashFlowAccess) {
        if (cashFlowAccess != null) {
            this.cashFlowAccess = cashFlowAccess;
            this.user = cashFlowAccess.getUser();
            updateAccountView();
        }
    }

    public CashFlowAccess getCashFlowAccess() {
        return this.cashFlowAccess;
    }

    @FXML
    public void initialize() {
        if (cashFlowAccess != null) {
            accounts.setEditable(false);
            setDropDownMenu();
            save();
            updateAccountView();
        }
    }

    private void setDropDownMenu() {
        accountType.getItems().clear();
        accountType.getItems().add("Brukskonto");
        accountType.getItems().add("Sparekonto");
        if (!BankHelper.hasBSU(user)) {
            accountType.getItems().add("BSU-konto");
        }
    }

    @FXML
    public void onCreateAccount() {
        String name = nameAccount.getText();
        String amount = setAmount.getText();
        if (accountType.getValue() == null) {
            errorMessage.setText("Velg en kontotype!");
        } else if (name.isBlank() || amount.isBlank()) {
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
            cashFlowAccess.addAccount(account);
            updateAccountView();
            accountCreated.setText("Kontoen er opprettet");
            nameAccount.setText("");
            setAmount.setText("");
            save();
        }
    }

    private boolean checkValidNameAmount(String name, String amount) {
        if (name == null && amount != null) {
            if (isNumeric(amount)) {
                return BankHelper.isPositiveAmount(Double.parseDouble(amount));
            } else {
                return false;
            }

        } else if (name != null && amount == null) {
            return BankHelper.isValidName(name);
        } else {
            return false;
        }
    }

    private AbstractAccount getAccountFromType(String type, String name, double balance) {
        switch (type) {
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
        accountCreated.setText("");
        errorMessage.setText("");
    }

    @FXML
    private boolean isNumeric(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @FXML
    private void updateAccountView() {
        accounts.setText("");
        for (AbstractAccount account : user.getAccounts()) {
            String type = "";
            if (account instanceof CheckingAccount) {
                type = "Brukskonto";
            } else if (account instanceof SavingsAccount) {
                type = "Sparekonto";
            } else if (account instanceof BSUAccount) {
                type = "BSU-konto";
            }
            String name = account.getName();
            DecimalFormat df = new DecimalFormat("##.0", new DecimalFormatSymbols(Locale.UK));
            String balance = df.format(account.getBalance());
            accounts.setText(accounts.getText() + "\n" + type + ": " + name + "\n" + "   Beløp: " + balance);
        }
        setDropDownMenu();
    }

    private void save() {
        try {
            cashFlowAccess.saveUser();
        } catch (IllegalStateException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @FXML
    private void onNextPage() throws IOException {
        if (!user.getAccounts().isEmpty()) {
            Stage stage = (Stage) detailsAndTransfers.getScene().getWindow();
            stage.close();
            Stage primaryStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                    .getResource(cashFlowAccess instanceof DirectAccess ? "LocalDetails.fxml" : "RemoteDetails.fxml"));
            Parent parent = fxmlLoader.load();
            primaryStage.setScene(new Scene(parent));
            primaryStage.show();
        } else {
            errorMessage.setText("Opprett en konto for å kunne se kontodetaljer og overføringer!");
        }
    }

}
