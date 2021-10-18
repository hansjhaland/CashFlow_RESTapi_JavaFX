package ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.internal.bytebuddy.implementation.bind.annotation.Default;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;
import org.loadui.testfx.GuiTest;


import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import json.CashFlowPersistence;


public class CashFlowControllerTest extends ApplicationTest{

    //Nødvendig å reste hva som skjer ved klikk på knapp "neste side"?
    //Har jeg testet verdi lik null riktig?
    //mangler kanskje noen edge cases
    
    private CashFlowController controller;
    final String SETTBELOP = "#settBelop";
    final String NAVNKONTO = "#navnKonto";
    final String OPPRETTKONTO = "#opprettKonto";
    final String TYPEKONTO = "#typeKonto";
    final String KONTOER = "#kontoer";


    @Override
    public void start(final Stage stage) throws Exception {
    final FXMLLoader loader = new FXMLLoader(getClass().getResource("CashFlow_test.fxml"));
    final Parent root = loader.load();
    this.controller = loader.getController();
    stage.setScene(new Scene(root));
    stage.show();
    
  }

    //trenger kanskje ikke denne
    //private CashFlowPersistence cfp = new CashFlowPersistence();

    @BeforeEach
    public void setUpItems() {
        TextField amount = find(SETTBELOP);
        TextField name = find(NAVNKONTO);
        amount.setText("");
        name.setText("");
        ChoiceBox cb = find(TYPEKONTO);
        cb.getSelectionModel().clearSelection();
        //controller = new CashFlowController();
        //System.getProperties().put("testfx.robot", "glass");
    }
    
    @Test
    public void testController() {
        assertNotNull(this.controller);
        
    }

    @Test
    public void testClickOn() {
        clickOn(OPPRETTKONTO);
        assertEquals("Velg en kontotype!", lookup("#feilmelding").queryText().getText());
    }
    
    
    @Test
    public void testNewCorrectAccount() {
        TextArea kontoOversikt = find("#kontoer");
        //hvis null?
        String kontoer = kontoOversikt.getText();
        
        String name = "first account";
        String amount = "12";

        clickOn(TYPEKONTO);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);

        clickOn(NAVNKONTO).write(name);
        clickOn(SETTBELOP).write(amount);

        clickOn(OPPRETTKONTO);

        assertNotNull(lookup("#kontoOpprettet").queryText().getText());
        assertEquals("Kontoen er opprettet", (lookup("#kontoOpprettet").queryText().getText()));
        assertEquals(kontoer + "\n" + "Sparekonto" + ": " + name + "\n" + "   Beløp: " + Double.parseDouble(amount), kontoOversikt.getText());
        
    }
    
    @SuppressWarnings (value="unchecked")
    private <T extends Node> T find(final String query) {
        return (T) lookup(query).queryAll().iterator().next();
    }


    @Test
    public void testMissingFields() {
        TextArea kontoOversikt = find("#kontoer");
        //hvis null?
        String kontoer1 = kontoOversikt.getText();

        TextField amount = find(SETTBELOP);
        TextField name = find(NAVNKONTO);

        //Test bare beløp
        clickOn("#settBelop").write("34");
        clickOn("#opprettKonto");
        assertEquals("Velg en kontotype!", lookup("#feilmelding").queryText().getText());

        //hvis du bare skriver inn navn
        amount.setText("");
        clickOn("#navnKonto").write("test");
        clickOn("#opprettKonto");
        assertEquals("Velg en kontotype!", lookup("#feilmelding").queryText().getText());

        //hvis du skriver inn navn og beløp 
        name.setText("");
        clickOn("#navnKonto").write("test");
        clickOn("#settBelop").write("35");
        clickOn("#opprettKonto");
        assertEquals("Velg en kontotype!", lookup("#feilmelding").queryText().getText());

        //hvis du skriver inn type
        amount.setText("");
        name.setText("");
        clickOn(TYPEKONTO);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        clickOn(OPPRETTKONTO);
        assertEquals("Husk å fylle inn alle felt", lookup("#feilmelding").queryText().getText());

        //hvis du skriver inn type og beløp
        clickOn("#settBelop").write("35");
        clickOn("#opprettKonto");
        assertEquals("Husk å fylle inn alle felt", lookup("#feilmelding").queryText().getText());

        //hvis du skriver inn type og navn
        amount.setText("");
        clickOn("#navnKonto").write("test");
        clickOn("#opprettKonto");
        assertEquals("Husk å fylle inn alle felt", lookup("#feilmelding").queryText().getText());

        String kontoer2 = kontoOversikt.getText();
        assertEquals(kontoer1, kontoer2);

    }

    @Test
    public void testWrongAccountName() {
        //mangler null-håndtering og edge case
        TextField name = find(NAVNKONTO);

        String name1 = "account1";
        String name2 = null;
        String name3 = "!.";
        String name4 = "thelengthogthisstringistoolong";

        clickOn("#settBelop").write("100");
        clickOn(TYPEKONTO);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);

        //1
        clickOn(NAVNKONTO).write(name1);
        clickOn(OPPRETTKONTO);
        assertEquals("Du kan ikke bruke tall eller tegn i navnet, og det må være mindre enn 20 bokstaver", controller.feilmelding.getText());

        //2 Håndtere hvis null
        try {
            clickOn(NAVNKONTO).write(name2);
            fail("Skal ikke kunne sette navn lik null");
        } catch (NullPointerException e) {

        }

        //3
        name.setText("");
        clickOn(NAVNKONTO).write(name3);
        clickOn(OPPRETTKONTO);
        assertEquals("Du kan ikke bruke tall eller tegn i navnet, og det må være mindre enn 20 bokstaver", controller.feilmelding.getText());

        //4
        name.setText("");
        clickOn(NAVNKONTO).write(name4);
        clickOn(OPPRETTKONTO);
        assertEquals("Du kan ikke bruke tall eller tegn i navnet, og det må være mindre enn 20 bokstaver", controller.feilmelding.getText());
    }

    @Test
    public void testWrongAmount() {
        TextField amount = find(SETTBELOP);

        String amount1 = "-12";
        String amount2 = "String";
        String amount3 = null;

        clickOn(NAVNKONTO).write("Main Account");
        clickOn(TYPEKONTO);
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);

        //1
        clickOn(SETTBELOP).write(amount1);
        clickOn(OPPRETTKONTO);
        assertEquals("Beløpet må bestå av tall og kan ikke være mindre enn null", controller.feilmelding.getText());

        //2
        amount.setText("");
        clickOn(SETTBELOP).write(amount2);
        clickOn(OPPRETTKONTO);
        assertEquals("Beløpet må bestå av tall og kan ikke være mindre enn null", controller.feilmelding.getText());

        //3
        try {
            clickOn(SETTBELOP).write(amount3);
            fail("Skal ikke kunne sette beløp lik null");
        } catch (NullPointerException e) {

        }
    }


}
