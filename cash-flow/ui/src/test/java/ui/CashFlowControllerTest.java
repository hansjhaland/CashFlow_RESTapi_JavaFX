package ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import json.CashFlowPersistence;

public class CashFlowControllerTest extends ApplicationTest{
    
    private CashFlowController controller;

    @Override
    public void start(final Stage stage) throws Exception {
    final FXMLLoader loader = new FXMLLoader(getClass().getResource("CashFlow_test.fxml"));
    final Parent root = loader.load();
    this.controller = loader.getController();
    stage.setScene(new Scene(root));
    stage.show();
  }

    private CashFlowPersistence cfp = new CashFlowPersistence();

    @BeforeEach
    public void setUp() {
        controller = new CashFlowController();
        /*Teste filepath*/
    }
    
    @Test
    public void testController() {
        assertNotNull(this.controller);
        
    }

    @Test
    public void testNewAccount() {
        String name = "first account";
        String amount = "12";
        clickOn("navnKonto").write(name);
        clickOn("settBelop").write(amount);
        clickOn("opprettKonto");

        List<String> accountOverview = controller.getAccountOverview();

        List<String> thisAccountOverview = new ArrayList<String>();
        thisAccountOverview.add(name + ":" + amount);

        assertEquals(thisAccountOverview, accountOverview);

    }
    
    /*@Test
    public void testOnlyLetters() {
        String name = "Account";
    }*/
}
