package ui;


import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.List;
import core.User;
import core.CheckingAccount;

public class CashFlowController {

@FXML private TextField navnKonto, settBelop;
@FXML private TextArea kontoer;
@FXML private Button opprettKonto;
@FXML private Text kontoOpprettet, feilmelding;

private static List<String> kontoOversikt = new ArrayList<String>();
private final User user = new User(123456);

public void initialize() {
    kontoer.setEditable(false);
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
    for(int i = 0; i < kontoOversikt.size(); i++){
        String indeks = kontoOversikt.get(i);
        String navn = indeks.split(":") [0];
        String belop = indeks.split(":") [1];
        kontoer.setText(kontoer.getText() + "\n" + "Konto: " + navn + "\n" + "   Beløp: " + belop);
    }
}


}
