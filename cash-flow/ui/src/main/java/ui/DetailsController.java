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
import json.CashFlowPersistence;

import java.io.IOException;

import core.User;
import core.AbstractAccount;
import core.BSUAccount;
import core.SavingsAccount;
import core.Transaction;
import core.BankHelper;

public class DetailsController {

    @FXML private TextField navnKonto, settBelop, overførBeløp;
    @FXML private TextArea kontoer, kontoHistorikk;
    @FXML private Button opprettKonto, detaljerOgOverforinger, tilHovedside, overfør;
    @FXML private Text kontoOpprettet, feedback;
    @FXML private ChoiceBox<String> velgKonto, overførKonto;
    
    private User user ;
    private AbstractAccount account;
    private AbstractAccount accountToTransferTo;
    private CashFlowPersistence cfp = new CashFlowPersistence();
    private BankHelper bankHelper = new BankHelper();

    public void initialize() {
        try {
            user = cfp.loadUser("SaveData.json");
        } catch (IllegalStateException e) {
            feedback.setText("Noe gikk galt! Fant ikke lagret brukerdata.");
        } catch (IOException e) {
            feedback.setText("Noe gikk galt! Fant ikke lagret brukerdata.");
        }
        if (user == null) {
            user = new User(123456);
        }
        kontoHistorikk.setEditable(false);
        updateTransferHistoryView();
        updateChooseAccountView();
    }

    private void updateTransferHistoryView() {
        String string = "";
        StringBuffer sb = new StringBuffer();
        if (account != null) {
            for (Transaction transaction : account.getTransactionHistory()) {
                string = "Til: " + transaction.getRecipient() + "\n" + "Fra: " + transaction.getPayer() + "\n" + "Beløp: " + transaction.getAmount() + "\n" + "\n";
                sb.append(string);
            }
            /* for (Transaction transaction : account.getTransactionHistory()) {
                string += "Til: " + transaction.getRecipient() + "\n" + "Fra: " + transaction.getPayer() + "\n" + "Beløp: " + transaction.getAmount() + "\n" + "\n";
            } */
        }
        kontoHistorikk.setText(sb.toString());
    }

    private void updateChooseAccountView() {
        velgKonto.getItems().clear();
        overførKonto.getItems().clear();
        for (AbstractAccount account : user.getAccounts()) {
            velgKonto.getItems().add(account.getName() + ": " + account.getAccountNumber());
            overførKonto.getItems().add(account.getName() + ": " + account.getAccountNumber());
        }
    }

    @FXML
    private void onChooseAccount() {
        String valueText = (String) velgKonto.getValue();
        String number = valueText.split(": ")[1];
        int accountNumber = (number == null ? 1 : Integer.parseInt(number));
        account = user.getAccount(accountNumber);
        updateTransferHistoryView();
    }



    @FXML
    private void onTransfer() {
        if (account != null && accountToTransferTo != null) {
            //double transferAmount = Double.valueOf(overførBeløp.getText());
            String amount = overførBeløp.getText();
            double transferAmount = (amount == null ? 0 : Double.parseDouble(amount));
            if (transferAmount <= 0){
                feedback.setText("Overføringsbeløpet må være større enn 0.");

            }
            else if (account instanceof BSUAccount){
                feedback.setText("Kan ikke overføre fra en BSU-konto");
            }
            else if (account == accountToTransferTo){
                feedback.setText("Sendekonto kan ikke være lik mottakerkonto");
            }
            else if (account instanceof SavingsAccount && !((SavingsAccount) account).isWithdrawalOrTransferPossible()){
                feedback.setText("Maksimalt antall uttak fra sparekonto nådd");
            }
            else{
                if (bankHelper.isBalanceValidWhenAdding(-transferAmount, account) && bankHelper.isBalanceValidWhenAdding(transferAmount, accountToTransferTo)){
                    account.transfer(accountToTransferTo, transferAmount);
                    save();
                    updateTransferHistoryView();
                }else{
                    feedback.setText(account.getName() + " har ikke nok penger på konto.");
                
                }
            }
            
        }else{
            feedback.setText("Velg hvilken konto du vil overføre fra/til.");
        }
        

        
    }

    @FXML
    private void onChooseAccountToTransferTo() {
        String valueText = (String) overførKonto.getValue();
        String number = valueText.split(": ")[1];
        int accountNumber = (number == null ? 1 : Integer.parseInt(number));
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

    public void loadNewUser(String saveFile) {
        cfp.setSaveFilePath(saveFile);
        load();
        updateChooseAccountView();
        updateTransferHistoryView();
    }
    
    private void load() {
        try {
            user = cfp.loadUser();
        } catch (IllegalStateException e) {
            feedback.setText("Noe gikk galt! Fant ikke lagret brukerdata.");
        } catch (IOException e) {
            feedback.setText("Noe gikk galt! Fant ikke lagret brukerdata.");
        }
    }

    private void save() {
        try {
            cfp.saveUser(user);
        } catch (IllegalStateException e) {
            feedback.setText("Noe gikk galt! Kunne ikke lagre brukerdata.");
        } catch (IOException e) {
            feedback.setText("Noe gikk galt! Kunne ikke lagre brukerdata.");
        }
    }
}
