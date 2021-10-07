package ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import core.AbstractAccount;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class CashFlowControllerTest extends ApplicationTest{
    
    private CashFlowController controller;

    @Override
    public void start(final Stage stage) throws Exception {
    final FXMLLoader loader = new FXMLLoader(getClass().getResource("TodoList_test.fxml"));
    final Parent root = loader.load();
    this.controller = loader.getController();
    stage.setScene(new Scene(root));
    stage.show();
  }

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
    public void testOnlyLetters() {
        String name = "Account";
        click
    }
}
