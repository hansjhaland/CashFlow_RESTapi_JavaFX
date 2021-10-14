package ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import json.CashFlowPersistence;


public class CashFlowControllerTest extends ApplicationTest{
    
    private CashFlowController controller;
    final String SETTBELOP = "#settBelop";
    final String NAVNKONTO = "#navnKonto";
    final String OPPRETTKONTO = "#opprettKonto";


    @BeforeClass
    public static void config() throws Exception {
        System.getProperties().put("testfx.robot", "glass");
    }

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
        controller = new CashFlowController();
        //System.getProperties().put("testfx.robot", "glass");
        //System.out.println(controller.kontoOpprettet.getText());
    }
    
    @Test
    public void testController() {
        assertNotNull(this.controller);
        
    }

    @Test
    public void testClickOn() {
        //Node feilmelding = find("#feilmelding");
        //Node opprettKonto = find("#opprettKonto");
        Node kontoOpprettet = find("#kontoOpprettet");

        //clickOn("#opprettKonto");
        clickOn(((Button)find("#opprettKonto")));
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals("Kontoen er opprettet", ((Text) kontoOpprettet).getText());

       // assertEquals("Husk å fylle inn alle felt", ((Text) feilmelding).getText());
    }
    
    /*
    @Test
    public void testNewCorrectAccount() {
        Node kontoOpprettet = find("#kontoOpprettet");
        //Node feilmelding = find("#feilmelding");
        TextField settBelop = find(SETTBELOP);
        TextField navnKonto = find(NAVNKONTO);
        Node opprettKonto = find(OPPRETTKONTO);
        String name = "first account";
        String amount = "12";

        clickOn(navnKonto);
        navnKonto.setText(name);
        clickOn(settBelop);
        settBelop.setText(amount);

        WaitForAsyncUtils.waitForFxEvents();

        clickOn(((Button) opprettKonto));

        
        WaitForAsyncUtils.waitForFxEvents();

        //assertNotNull(kontoOpprettet);

        //assertNotNull(((Text) kontoOpprettet).getText());
        assertEquals("Kontoen er opprettet", ((Text) kontoOpprettet).getText());
        //System.out.println(((Text) kontoOpprettet).getText());
        //System.out.println(((Text) feilmelding).getText());
        //List<String> accountOverview = controller.getAccountOverview();

        //List<String> thisAccountOverview = new ArrayList<String>();
        //thisAccountOverview.add(name + ":" + amount);

        //assertFalse(accountOverview.isEmpty());
        //assertEquals(thisAccountOverview, accountOverview);

        //System.out.println(controller.kontoOpprettet.getText());
        //vil sjekke at riktig tilbakemelding er sendt ut
        //assertEquals("Kontoen er opprettet", controller.kontoOpprettet.getText());
    }*/
    
    @SuppressWarnings (value="unchecked")
    private <T extends Node> T find(final String query) {
        return (T) lookup(query).queryAll().iterator().next();
    }

   /* @Test
    public void testMissingFields() {
        //hvis du ikke skriver inn noe
        clickOn("#opprettKonto");
        assertTrue(controller.getAccountOverview().isEmpty());
        assertEquals("Husk å fylle inn alle felt", controller.feilmelding.getText());

        //hvis du bare skriver inn beløp
        clickOn("#settBelop").write("12");
        clickOn("#opprettKonto");

        assertTrue(controller.getAccountOverview().isEmpty());
        assertEquals("Husk å fylle inn alle felt", controller.feilmelding.getText());

        //hvis du bare skriver inn navn
        clickOn("#navnKonto").write("test");
        clickOn("#opprettKonto");

        assertTrue(controller.getAccountOverview().isEmpty());
        assertEquals("Husk å fylle inn alle felt", controller.feilmelding.getText());
    }

    @Test
    public void testWrongAccountName() {
        String name = "account1";
        String amount = "12";
        clickOn("#navnKonto").write(name);
        clickOn("#settBelop").write(amount);
        clickOn("#opprettKonto");

        //sjekker at kontoen ikke er lagt til i kontooversikten
        assertTrue(controller.getAccountOverview().isEmpty());

        //sjekker at riktig feilmelding er sendt ut
        assertEquals("Du kan ikke bruke tall eller tegn i navnet", controller.feilmelding.getText());
    }

    @Test
    public void testWrongAmount() {
        String name = "account";
        String amount = "-12";
        clickOn("#navnKonto").write(name);
        clickOn("#settBelop").write(amount);
        clickOn("#opprettKonto");

        assertTrue(controller.getAccountOverview().isEmpty());
        //vil sjekke at riktig feilmelding er sendt ut
        assertEquals("Beløpet må bestå av tall og kan ikke være mindre enn null", controller.feilmelding.getText());
        assertEquals(" ", controller.kontoOpprettet.getText());
    }*/
}
