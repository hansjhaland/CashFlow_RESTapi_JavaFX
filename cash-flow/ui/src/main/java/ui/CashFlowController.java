package ui;

import core.AbstractAccount;
import core.BSUAccount;
import core.BankHelper;
import core.CheckingAccount;
import core.SavingsAccount;
import core.User;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;


//==============================================================================================
// Controller
//==============================================================================================

/**
 * A controller for creating an account and display it with its correspondig FXML file.
 */

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


//==============================================================================================
// Functional methods
//==============================================================================================



/**
 * Gives the user access to the Cashflow app by setting the user to its access.
 * @param cashFlowAccess tells weither its Remote or Locally (Rest API).
 */
    public void setCashFlowAccess(CashFlowAccess cashFlowAccess) {
        if (cashFlowAccess != null) {
            this.cashFlowAccess = cashFlowAccess;
            this.user = cashFlowAccess.getUser();
            updateAccountView();
        }
    }
/**
 * Returns either Remote or Direct access.
 * @return its access based on Remote or Locally (Rest API).
 */
    public CashFlowAccess getCashFlowAccess() {
        return this.cashFlowAccess;
    }

/**
 * Initializer runs the methods on the app if access is granted and updates the file.
 */
    @FXML
    public void initialize() {
        if (cashFlowAccess != null) {
            accounts.setEditable(false);
            setDropDownMenu();
            save();
            updateAccountView();
        }
    }

/**
 * Sets the drop-down menu for the accounts you can choose between
 * If BSU account type is not jet used, add it. Can not use BSU account type more than once.
 */
    private void setDropDownMenu() {
        accountType.getItems().clear();
        accountType.getItems().add("Brukskonto");
        accountType.getItems().add("Sparekonto");
        if (!BankHelper.hasBSU(user)) {
            accountType.getItems().add("BSU-konto");
        }
    }
/**
 * Method for creating an account with name and starting amount.
 * It checks that no fields are left empty.
 * If no fields are empty, it creates an account and is added to the user where the user has access, either on Remote or Locally (Rest API).
 */
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

        else if (!checkValidNameAmount(name, null)) {
            clear();
            errorMessage.setText("Du kan ikke bruke tall eller tegn i navnet, og det må være mindre enn 20 bokstaver");
        }

        else if (!checkValidNameAmount(null, amount)) {
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
            accountType.setValue("");
            setAmount.setText("");
            save();
        }
    }

/**
 * Checks if the number typed in is valid by being not null and if so, that the number
 * typed in is more than 0. parsDouble makes sure it is formatted correct.
 * Checks if the name typed in agrees with the valid method for names in Bankhelper with a lenght of 20 characters
 * and only contains letters and scaces.
 * @param name of account of the user.
 * @param amount in the account of the user.
 * @return false if number is null.
 * @return false if name is null, not valid by BankHelper class.
 */
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

    /**
     * Return the account with its name and its balance the user has created based on with type is presented.
     * @param type of the account.
     * @param name of the user for the account.
     * @param balance of the account the user has placed.
     * @return null if there is no account of any types.
     */

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
/**
 * Resets the popup-feedback on the display (CashFlow.fxml) when this method is used.
 */
    @FXML
    private void clear() {
        accountCreated.setText("");
        errorMessage.setText("");
    }


/**
 * Checks if the chosen amount is a numeric number.
 * @param s the written amount
 * @return {@code true} if the amount is numeric.
 */
    @FXML
    private boolean isNumeric(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

/**
 * Method for updating the accountview after changes.
 * Based on the accounts type, it gets its label from what kind of account it is.
 * Format numbers in a readble way.
 */

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
            accounts.setText(accounts.getText() + "\n" + type + ": " + name + "\n" + "   Beløp: " + balance + " kr");
        }
        setDropDownMenu();
    }

/**
 * The method saves the changes to its place, weither its in Remote or Locally (Rest API).
 * @throws IllegalStateException if an error occured and it could not save.
 * @throws IOException if an error occured and it could not save.
 */
    private void save() {
        try {
            cashFlowAccess.saveUser();
        } catch (IllegalStateException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

/**
 * Method opens a new FXML window when button detailsAndTransfers are pushed.
 * Method opens only if there is an account created
 * Loads the Details FXML file for the DetailsController.
 * @throws IOException if there is no account created before the onAction button is pushed.
 */
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

/**
 * When the choicebox is pressed and an item is choosen, it shows the restrictions for the choosen account 
 * based on its type.
 * Message is display with the restrictions.
 */
    @FXML
    public void onAccountType(){    
    errorMessage.setText("");
    String valueText = "";
    valueText = (String) accountType.getValue();
    if (valueText != null) {
      if (valueText.equals("Brukskonto")) {
        accountCreated.setText("Brukskonto er uten restriksjoner.");

      } else if (valueText.equals("Sparekonto")) {
        accountCreated.setText("Sparekonto kan kun ha maks 10 uttak.");

      } else if (valueText.equals("BSU-konto")) {
        accountCreated.setText("Du kan bare opprette én BSU-konto." + "\n"
            + "Maksbeløp som kan være på konto er 25 000kr" + "\n" 
            + "og du kan ikke gjøre noe uttak fra kontoen.");
      }
    }

  }

}
