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

@FXML private TextField navnKonto, settBelop;
@FXML private TextArea kontoer;
@FXML private Button opprettKonto, detaljerOgOverføringer;
@FXML private Text kontoOpprettet, feilmelding;



private static List<String> kontoOversikt = new ArrayList<String>();
private User user = new User(123456);
private CashFlowPersistence cfp = new CashFlowPersistence();
private Random ran = new Random();

public void initialize() {
    kontoer.setEditable(false);
    updateAccountView();
}

@FXML
private void onCreateAccount() {
    if (this.navnKonto.getText().isBlank() || this.settBelop.getText().isBlank()) {
        clear();
        feilmelding.setText("Husk å fylle inn alle felt");
    }

    else if (!onlyLetters(this.navnKonto.getText())){
        clear();
        feilmelding.setText("Du kan ikke bruke tall eller tegn i navnet");
    }

    else if (!this.settBelop.getText().matches("[0-9]+")){
        clear();
        feilmelding.setText("Beløpet må bestå av tall");
    }
    
    else {
        clear();
        kontoOpprettet.setText("Kontoen er opprettet");
        kontoOversikt.add(navnKonto.getText() + ":" + settBelop.getText());
        AbstractAccount account = new CheckingAccount(navnKonto.getText(), Double.valueOf(settBelop.getText()), 1000 + ran.nextInt(8999));
        user.addAccount(account);
        save();
        updateAccountView();
    } 
}

@FXML
private void clear() {
    kontoOpprettet.setText("");
    feilmelding.setText("");
}

@FXML
private boolean onlyLetters(String s){
    for(int i = 0; i < s.length(); i++){
        char ch = s.charAt(i);
        if (Character.isLetter(ch) || ch == ' ') {
            continue;        
        }
        return false;
    }
    return true;
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


}
