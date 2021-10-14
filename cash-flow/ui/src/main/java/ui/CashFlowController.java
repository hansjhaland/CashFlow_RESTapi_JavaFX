package ui;


import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import json.CashFlowPersistence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import core.User;
import core.AbstractAccount;
import core.CheckingAccount;

public class CashFlowController {

@FXML TextField navnKonto, settBelop;
@FXML TextArea kontoer;
@FXML Button opprettKonto;
@FXML Text kontoOpprettet, feilmelding;

private List<String> kontoOversikt = new ArrayList<String>();
private User user = new User(123456);
private CashFlowPersistence cfp = new CashFlowPersistence();
private Random ran = new Random();
//private AbstractAccount abstractAccount;

public void initialize() {
    kontoer.setEditable(false);
    updateAccountView();
    //kontoOpprettet.setText(" hei");
    //System.out.println(kontoOpprettet.getText());
    //feilmelding.setText(" ");
}

@FXML
public void onCreateAccount() {
    String name = this.navnKonto.getText();
    String amount = this.settBelop.getText();
    System.out.println("Kom hit");
    if (name.isBlank() || amount.isBlank()) {
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

    /*else if (!amount.matches("[0-9]+")){
        clear();
        feilmelding.setText("Beløpet må bestå av tall");
    }*/
    
    else {
        clear();
        kontoOpprettet.setText("Kontoen er opprettet");
        kontoOversikt.add(navnKonto.getText() + ":" + settBelop.getText());
        AbstractAccount account = new CheckingAccount(navnKonto.getText(), Double.valueOf(settBelop.getText()), 1000 + ran.nextInt(8999), user);
        user.addAccount(account);
        save();
        updateAccountView();
    } 
}

private boolean checkValidNameAmount(String name, String amount) {
    if (name == null && amount != null) {
        if (isNumeric(amount)) {
            return AbstractAccount.isPositiveAmount(Double.parseDouble(amount));
        }
        else {
            return false;
        }
        
    }
    else if (name != null && amount == null) {
        return User.isValidName(name);
    }
   else {
       return false;
   }
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
private void clear() {
    kontoOpprettet.setText(" ");
    feilmelding.setText(" ");
}



@FXML
private void updateAccountView(){
    kontoer.setText("");
    load();
    kontoOversikt.clear();
    for (AbstractAccount account : user.getAccounts()) {
        String konto = account.getName() + ":" + account.getBalance();
        kontoOversikt.add(konto);
    }
    for(int i = 0; i < kontoOversikt.size(); i++){
        String indeks = kontoOversikt.get(i);
        String navn = indeks.split(":") [0];
        String belop = indeks.split(":") [1];
        kontoer.setText(kontoer.getText() + "\n" + "Konto: " + navn + "\n" + "   Beløp: " + belop);
    }
}

private void save() {
    cfp.setSaveFilePath("SaveData.json");
    try {
        cfp.saveUser(user);
    } catch (IllegalStateException e) {
        feilmelding.setText("Bankkontoene ble ikke lagret.");
    } catch (IOException e) {
        feilmelding.setText("Bankkontoene ble ikke lagret.");
    }
}

private void load() {
    cfp.setSaveFilePath("SaveData.json");
    try {
        user = cfp.loadUser();
    } catch (IllegalStateException e) {
        feilmelding.setText("Bankkontoene ble ikke funnet.");
    } catch (IOException e) {
        feilmelding.setText("Bankkontoene ble ikke funnet.");
    }
}

public List<String> getAccountOverview() {
    return kontoOversikt;
}

}
