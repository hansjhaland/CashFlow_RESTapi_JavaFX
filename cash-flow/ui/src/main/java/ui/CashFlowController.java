package ui;

/* import javafx.event.ActionEvent;
import javafx.fxml.FXML; */

public class CashFlowController {

@FXML private TextField navnKonto, settBelop;
@FXML private TextArea kontoer;
@FXML private Button opprettKonto;
@FXML private Text kontoOpprettet, feilmelding;

@FXML
private void onOpprettKonto() {
    String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVXYZ";
    if (this.navnKonto.getText().isBlank() || this.settBelop.getText().isBlank) {

    }
}

}
