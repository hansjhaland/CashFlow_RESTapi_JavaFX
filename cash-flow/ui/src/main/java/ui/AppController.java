package ui;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import core.CheckingAccount;
import core.SavingsAccount;
import core.User;
import javafx.fxml.FXML;
import json.CashFlowPersistence;

public class AppController {

    String baseUri = "http://localhost:8999/user/";

    @FXML
    String remote;

    @FXML
    String page;

    @FXML
    DetailsController detailsController;
    @FXML
    CashFlowController mainPageController;

    private CashFlowAccess cashFlowAccess;

    private CashFlowPersistence cfp;

    private User getInitialUser() {
        User initialUser = null;
        if (cfp != null) {
            try {
                initialUser = cfp.loadUser(DirectAccess.LOCALSAVEFILE);
            } catch (IOException e) {
                System.err.println("Fikk ikke lest inn lagret bruker");
            }
        }
        if (initialUser == null) {
            initialUser = new User(123456);
            new CheckingAccount("Brukskonto", 250.0, initialUser);
            new SavingsAccount("Sparekonto", 1000.0, initialUser);
        }
        return initialUser;
    }

    @FXML
    public void initialize() {
        cfp = new CashFlowPersistence();
        cashFlowAccess = new DirectAccess(getInitialUser(), DirectAccess.LOCALSAVEFILE);
        if (remote.equals("false")) {
            if (page.equals("main")) {
                mainPageController.setCashFlowAccess(cashFlowAccess);
            } else if (page.equals("details")) {
                detailsController.setCashFlowAccess(cashFlowAccess);
            }
        } else if (remote.equals("true")) {
            try {
                cashFlowAccess = new RemoteAccess(new URI(baseUri));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            if (page.equals("main")) {
                mainPageController.setCashFlowAccess(cashFlowAccess);
            } else if (page.equals("details")) {
                detailsController.setCashFlowAccess(cashFlowAccess);
            }
        }
    }
}
