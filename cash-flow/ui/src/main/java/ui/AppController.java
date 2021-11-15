package ui;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import core.AbstractAccount;
import core.CheckingAccount;
import core.SavingsAccount;
import core.User;
import javafx.fxml.FXML;
import json.CashFlowPersistence;

public class AppController {

    @FXML
    String baseUri;

    @FXML
    CashFlowController mainPageController;

    private CashFlowAccess cashFlowAccess;

    private CashFlowPersistence cfp;

    private User getInitialUser() {
        User initialUser = null;
        if (cfp != null) {
            try {
                initialUser = cfp.loadUser(DirectAccess.SAVEFILE);
            } catch (IOException e) {
                System.err.println("Fikk ikke lest inn lagret bruker");
            }
        }
        if (initialUser == null) {
            initialUser = new User(123456);
            AbstractAccount account1 = new CheckingAccount("Brukskonto", 250.0, initialUser);
            AbstractAccount account2 = new SavingsAccount("Sparekonto", 1000.0, initialUser);
            initialUser.addAccount(account1);
            initialUser.addAccount(account2);
        }
        return initialUser;
    }

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
            cfp = new CashFlowPersistence();
            if (cfp.doesFileExist(DirectAccess.SAVEFILE)) {
                cashFlowAccess = new DirectAccess(getInitialUser());
            }
        }
        mainPageController.setCashFlowAccess(cashFlowAccess);
        mainPageController.setUser(cashFlowAccess.getUser());
    }
}
