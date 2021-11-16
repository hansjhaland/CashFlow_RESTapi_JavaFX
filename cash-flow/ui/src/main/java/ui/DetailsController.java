package ui;

import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import core.User;
import core.AbstractAccount;
import core.BSUAccount;
import core.SavingsAccount;
import core.Transaction;
import core.BankHelper;

public class DetailsController {

    @FXML
    private TextField nameAccount, setAmount, transferAmount;
    @FXML
    private TextArea accounts, accountHistory;
    @FXML
    private Button createAccount, detailsAndTransfers, toMainPage, transfer, deleteButton;
    @FXML
    private Text accountCreated, feedback;
    @FXML
    private ChoiceBox<String> chooseAccount, transferAccount;

    private User user;
    private AbstractAccount account;
    private AbstractAccount accountToTransferTo;
    private CashFlowAccess cashFlowAccess;

    public void setCashFlowAccess(CashFlowAccess cashFlowAccess) {
        if (cashFlowAccess != null) {
            this.cashFlowAccess = cashFlowAccess;
            this.user = cashFlowAccess.getUser();
            updateTransferHistoryView();
            updateChooseAccountView();
        }
    }

    @FXML
    public void initialize() {
        accountHistory.setEditable(false);
    }

    private void updateTransferHistoryView() {
        String string = "";
        StringBuffer sb = new StringBuffer();
        if (account != null) {
            for (Transaction transaction : account.getTransactionHistory()) {
                string = "Til: " + transaction.getRecipient() + "\n" + "Fra: " + transaction.getPayer() + "\n"
                        + "Beløp: " + transaction.getAmount() + "\n" + "\n";
                sb.append(string);
            }
        }
        accountHistory.setText(sb.toString());
    }

    private void updateChooseAccountView() {
        chooseAccount.getItems().clear();
        transferAccount.getItems().clear();
        for (AbstractAccount account : user.getAccounts()) {
            chooseAccount.getItems().add(account.getName() + ": " + account.getAccountNumber());
            transferAccount.getItems().add(account.getName() + ": " + account.getAccountNumber());
        }
    }

    @FXML
    private void onChooseAccount() {
        String valueText = (String) chooseAccount.getValue();
        if (valueText == null || valueText.equals("")) {
            account = null;
        } else {
            String number = valueText.split(": ")[1];
            int accountNumber = (number == null ? 1 : Integer.parseInt(number));
            account = cashFlowAccess.getAccount(accountNumber);
        }
        updateTransferHistoryView();
    }

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

    @FXML
    private void onTransfer() {
        feedback.setText("");
        if (account != null && accountToTransferTo != null) {
            String amount = transferAmount.getText();
            double transferAmount = (amount == null ? 0 : Double.parseDouble(amount));
            if (transferAmount <= 0) {
                feedback.setText("Overføringsbeløpet må være større enn 0.");

            } else if (account instanceof BSUAccount) {
                feedback.setText("Kan ikke overføre fra en BSU-konto.");
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
                    updateTransferHistoryView();
                } else {
                    feedback.setText(account.getName() + " har ikke nok penger på konto.");

                }
            }

        } else {
            feedback.setText("Velg hvilken konto du vil overføre fra/til.");
        }

    }

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
