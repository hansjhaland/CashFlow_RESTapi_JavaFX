package ui;

import core.AbstractAccount;
import core.BSUAccount;
import core.BankHelper;
import core.CheckingAccount;
import core.SavingsAccount;
import core.Transaction;
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
 * A controller for details and transfer of accounts for the user and display it with its correspondig FXML file.
 */

public class DetailsController {

  @FXML private TextField nameAccount;
  @FXML private TextField transferAmount;
  @FXML private TextField setBalance;
  @FXML private TextArea accountHistory;
  @FXML private TextArea accounts;
  @FXML private Button createAccount;
  @FXML private Button detailsAndTransfers;
  @FXML private Button toMainPage;
  @FXML private Button transfer;
  @FXML private Button deleteButton;
  @FXML private Text accountCreated;
  @FXML private Text feedback;
  @FXML private ChoiceBox<String> chooseAccount;
  @FXML private ChoiceBox<String> transferAccount;

    private User user;
    private AbstractAccount account;
    private AbstractAccount accountToTransferTo;
    private CashFlowAccess cashFlowAccess;


/**
 * Gives the user access to the Cashflow app by setting the user to its access.
 * @param cashFlowAccess tells weither its Remote or Locally (Rest API).
 */
    public void setCashFlowAccess(CashFlowAccess cashFlowAccess) {
        if (cashFlowAccess != null) {
            this.cashFlowAccess = cashFlowAccess;
            this.user = cashFlowAccess.getUser();
            updateTransferHistoryView();
            updateChooseAccountView();
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
 * Initializer runs the methods on the app if access is granted and updates the history of the actions taken by
 * the account and balance.
 */
    @FXML
    public void initialize() {
        accountHistory.setEditable(false);
        setBalance.setEditable(false);
    }

/**
 * The method updates the transaction history if there is an account.
 * Than it formats the transactiondetails in a representable way with from-account, to-account and balance
 * that was transfered. 
 */
    private void updateTransferHistoryView() {
        String string = "";
        StringBuffer sb = new StringBuffer();
        if (account != null) {
            DecimalFormat df = new DecimalFormat("##.0", new DecimalFormatSymbols(Locale.UK));
            String amount = "";
            for (Transaction transaction : account.getTransactionHistory()) {
                amount = transaction.getAmount() == 0.0 ? "0.0" : df.format(transaction.getAmount());
                string = "Til: " + transaction.getRecipient() + "\n" + "Fra: " + transaction.getPayer() + "\n"
                        + "Beløp: " + amount + " kr\n" + "\n";
                sb.append(string);
            }
        }
        accountHistory.setText(sb.toString());
    }

/**
 * Updates the pageview when changes occur, such as a transfer.
 * The method also format the data from the account in the pageview in a representable form.
 */
    private void updateChooseAccountView() {
        chooseAccount.getItems().clear();
        transferAccount.getItems().clear();
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
            chooseAccount.getItems().add(type + "; " + account.getName() + ", kontonummer: " + account.getAccountNumber());
            transferAccount.getItems().add(type + "; " + account.getName() + ": " + account.getAccountNumber());
        }
    }


/**
 * Method that happens when choosing an account and formats its sccount and balance in the choicebox.
 * If there is no accounts in the account choicebox, it does not do anything and set account null.
 * Else it formats the number in a representable way and gets the accountnumber from the Remote or Locally (Rest APi).
 */
    @FXML
    private void onChooseAccount() {
        String valueText = (String) chooseAccount.getValue();
        if (valueText == null || valueText.equals("")) {
            account = null;
        } else {
            String number = valueText.split(": ")[1];
            int accountNumber = (number == null ? 1 : Integer.parseInt(number));
            account = cashFlowAccess.getAccount(accountNumber);

            DecimalFormat df = new DecimalFormat("##.0", new DecimalFormatSymbols(Locale.UK));
            String balance = account.getBalance() == 0.0 ? "0.0" : df.format(account.getBalance());
            
            setBalance.setText(balance + " kr");

        }
        updateTransferHistoryView();
    }
/**
 * Deletes an account when button is pushed.
 * Deletes the accountnumber only if there is no amount on the account from either Remote or Locally (rest API).
 */
    @FXML
    private void onDeleteAccount() {
        if (account != null) {
            if (account.getBalance() == 0.0) {
                if (cashFlowAccess.deleteAccount(account.getAccountNumber())) {
                    feedback.setText("Konto slettet.");
                    save();
                    chooseAccount.setValue("");
                    updateChooseAccountView();
                }
            } else {
                feedback.setText("Du må ha saldo 0 eller overføre pengene til en annen konto.");
            }
        } else {
            feedback.setText("Du må velge en konto først.");
        }
    }

/**
 * The method let the account transfer from one account to another.
 * Have to pick an account from and an account to transfer to.
 * There are restrictions for transfering. Can not transfer if the amount on the account is not a number or below 0.
 * And the from-account can not be the same as the to-account.
 * The type account BSU can not be transformed from and can not be transformed to if it has more than 25 000kr.
 */
    @FXML
    private void onTransfer() {
        feedback.setText("");
        if (account != null && accountToTransferTo != null) {
            String amount = transferAmount.getText();
            double transferAmount = 0;
            try {
                transferAmount = Double.parseDouble(amount);
            } catch (Exception e) {
                feedback.setText("Overføringsbeløpet må være et tall.");
                return;
            }
            if (transferAmount <= 0) {
                feedback.setText("Overføringsbeløpet må være større enn 0.");

            } else if (account instanceof BSUAccount) {
                feedback.setText("Kan ikke overføre fra en BSU-konto.");
            } else if (accountToTransferTo instanceof BSUAccount) {
                BSUAccount bsuAccount = (BSUAccount) accountToTransferTo;
                if (!bsuAccount.isValidDeposit(transferAmount)) {
                    feedback.setText("Saldoen til BSU-kontoen kan ikke overstige 25000 kr.");
                }

            } else if (account == accountToTransferTo) {
                feedback.setText("Sendekonto kan ikke være lik mottakerkonto.");
            } else if (account instanceof SavingsAccount
                    && !((SavingsAccount) account).isWithdrawalOrTransferPossible()) {
                feedback.setText("Maksimalt antall uttak fra sparekonto nådd.");
            } else {
                if (BankHelper.isBalanceValidWhenAdding(-transferAmount, account)
                        && BankHelper.isBalanceValidWhenAdding(transferAmount, accountToTransferTo)) {
                    cashFlowAccess.transfer(account, accountToTransferTo, transferAmount);
                    feedback.setText("Overføring godkjent");
                    save();
                    DecimalFormat df = new DecimalFormat("##.0", new DecimalFormatSymbols(Locale.UK));
                    String balance = account.getBalance() == 0.0 ? "0.0" : df.format(account.getBalance());
                    setBalance.setText(balance + " kr");
                    updateTransferHistoryView();
                    this.transferAmount.setText("");
                }else{
                    feedback.setText(account.getName() + " har ikke nok penger på konto.");

                }
            }

        } else {
            feedback.setText("Velg hvilken konto du vil overføre fra/til.");
        }

    }

/**
 * Selecting the account to transfer the selected amount to on Remote or Locally (Rest API).
 */
    @FXML
    private void onChooseAccountToTransferTo() {
        String valueText = (String) transferAccount.getValue();
        if (valueText == null || valueText.equals("")) {
            accountToTransferTo = null;
        } else {
            String number = valueText.split(": ")[1];
            int accountNumber = (number == null ? 1 : Integer.parseInt(number));
            accountToTransferTo = cashFlowAccess.getAccount(accountNumber);
        }
      }

/**
 * When "back" button is pressed, it returns the user to the previous page.
 * Loads the CashFlow FXML file for the CashFlowController and display it.
 * @throws IOException if it could not load the previous page.
 */

    @FXML
    private void onPreviousPage() throws IOException {
        Stage stage = (Stage) toMainPage.getScene().getWindow();
        stage.close();
        Stage primaryStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                .getResource(cashFlowAccess instanceof DirectAccess ? "LocalCashFlow.fxml" : "RemoteCashFlow.fxml"));
        Parent parent = fxmlLoader.load();
        primaryStage.setScene(new Scene(parent));
        primaryStage.show();
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
            feedback.setText("Noe gikk galt! Kunne ikke lagre brukerdata.");
        } catch (IOException e) {
            feedback.setText("Noe gikk galt! Kunne ikke lagre brukerdata.");
        }
    }
  }
