package ui;

import java.net.URI;
import java.net.URISyntaxException;

import javafx.fxml.FXML;

public class AppController {

    @FXML
    String baseUri;

    @FXML
    CashFlowController mainPageController;

    CashFlowAccess cashFlowAccess;

    @FXML
    public void initialize() {
        if (baseUri != null) {
            try {
                cashFlowAccess = new RemoteAccess(new URI(baseUri));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        if (cashFlowAccess == null) {
            cashFlowAccess = new DirectAccess();
        }
        mainPageController.cashFlowAccess = cashFlowAccess;
    }
}
