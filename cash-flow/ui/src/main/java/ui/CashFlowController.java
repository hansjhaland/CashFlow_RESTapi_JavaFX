package ui;


import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class CashFlowController {

@FXML private TextField navnKonto, settBelop;
@FXML private TextArea kontoer;
@FXML private Button opprettKonto;
@FXML private Text kontoOpprettet, feilmelding;

public void initialize() {
    kontoer.setEditable(false);
}

@FXML
private void onOpprettKonto() {
   // String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVXYZ";
    if (this.navnKonto.getText().isBlank() && this.settBelop.getText().isBlank()) {
        feilmelding.setText("Husk å fylle inn alle felt");
    }
    else {
        kontoOpprettet.setText("Kontoen er opprettet");
    } 
}

}
