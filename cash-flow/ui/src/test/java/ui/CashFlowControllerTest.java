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
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import json.CashFlowPersistence;


public class CashFlowControllerTest extends ApplicationTest{
    
    private CashFlowController controller;
    final String SETTBELOP = "#settBelop";
    final String NAVNKONTO = "#navnKonto";
    final String OPPRETTKONTO = "#opprettKonto";
    final String TYPEKONTO = "#typeKonto";


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
        clickOn(OPPRETTKONTO);
        assertEquals("Velg en kontotype!", lookup("#feilmelding").queryText().getText());
    }
    
    
    @Test
    public void testNewCorrectAccount() {
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
        
    }
    
    @SuppressWarnings (value="unchecked")
    private <T extends Node> T find(final String query) {
        return (T) lookup(query).queryAll().iterator().next();
    }

    @Test
    public void testMissingFields() {
        //hvis du ikke skriver inn noe
        clickOn(OPPRETTKONTO);
        assertEquals("Velg en kontotype!", lookup("#feilmelding").queryText().getText());
        //onClear();

        //hvis du bare skriver inn beløp
        clickOn("#settBelop").write("12");
        clickOn("#opprettKonto");
        assertEquals("Velg en kontotype!", lookup("#feilmelding").queryText().getText());
        //onClear();

        //hvis du bare skriver inn navn
        clickOn("#navnKonto").write("test");
        clickOn("#opprettKonto");
        assertEquals("Velg en kontotype!", lookup("#feilmelding").queryText().getText());
        //onClear();

        //hvis du skriver inn navn og beløp 
        clickOn("#navnKonto").write("test");
        clickOn("#settBelop").write("12");
        clickOn("#opprettKonto");
        assertEquals("Velg en kontotype!", lookup("#feilmelding").queryText().getText());
       //onClear();

        //hvis du skriver inn type
        if(controller.navnKonto != null) {
            controller.navnKonto.clear();
        }
        if(controller.settBelop != null) {
            controller.settBelop.clear();;
        }
        
        clickOn("#typeKonto");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        assertEquals("Husk å fylle inn alle felt", lookup("#feilmelding").queryText().getText());
        //onClear();

        //hvis du skriver inn type og navn
        clickOn("#navnKonto").write("test");
        clickOn("#typeKonto");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        assertEquals("Husk å fylle inn alle felt", lookup("#feilmelding").queryText().getText());
        //onClear();

        //hvis du skriver inn type og beløp
        controller.navnKonto.clear();
        clickOn("#settBelop").write("12");
        clickOn("#typeKonto");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        assertEquals("Husk å fylle inn alle felt", lookup("#feilmelding").queryText().getText());
        //onClear();
    }
/*
    @Test
    public void testWrongAccountName() {
        String name = "account1";
        String amount = "12";
        clickOn("#navnKonto").write(name);
        clickOn("#settBelop").write(amount);
        clickOn("#opprettKonto");

        //sjekker at kontoen ikke er lagt til i kontooversikten
        

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

        //vil sjekke at riktig feilmelding er sendt ut
        assertEquals("Beløpet må bestå av tall og kan ikke være mindre enn null", controller.feilmelding.getText());
        assertEquals(" ", controller.kontoOpprettet.getText());
    }*/
/*
	private void onClear() {
		controller.navnKonto.clear();
		controller.settBelop.clear();
		controller.typeKonto.setValue(null);
		controller.kontoOpprettet.setText("");
        controller.feilmelding.setText("");
    }*/
}
