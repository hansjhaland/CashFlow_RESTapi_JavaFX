package ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import core.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import json.CashFlowPersistence;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class CashFlowControllerTest extends ApplicationTest{

    //Nødvendig å reste hva som skjer ved klikk på knapp "neste side"?
    
    private CashFlowController controller;
    final String SETTBELOP = "#settBelop";
    final String NAVNKONTO = "#navnKonto";
    final String OPPRETTKONTO = "#opprettKonto";
    final String TYPEKONTO = "#typeKonto";
    final String KONTOER = "#kontoer";

    private final static String testSaveFile = "SaveDataTest.json";
    private CashFlowPersistence cfp = new CashFlowPersistence();


    @Override
    public void start(final Stage stage) throws Exception {
    final FXMLLoader loader = new FXMLLoader(getClass().getResource("CashFlow_test.fxml"));
    final Parent root = loader.load();
    this.controller = loader.getController();
    stage.setScene(new Scene(root));
    stage.show();
    User user = new User(123456);
    cfp.saveUser(user, testSaveFile);
    controller.loadNewUser(testSaveFile);
    
  }

    @BeforeEach
    public void setUpItems() {
        TextField amount = find(SETTBELOP);
        TextField name = find(NAVNKONTO);
        amount.setText("");
        name.setText("");
        ChoiceBox<String> cb = find(TYPEKONTO);
        cb.getSelectionModel().clearSelection();
    }

    @AfterAll
    public static void deleteTestJsonFile() {
        Path testFilePath = Paths.get(System.getProperty("user.home"), testSaveFile);
        File testFile = testFilePath.toFile();
        testFile.delete();
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
        assertThrows(NullPointerException.class, () -> {
            clickOn(NAVNKONTO).write(name2);
        });

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
        assertThrows(NullPointerException.class, () -> {
            clickOn(SETTBELOP).write(amount3);
        });
    }


}
