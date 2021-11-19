package ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

public class CashFlowControllerTest extends ApplicationTest {

    private CashFlowController controller;
    final String SETAMOUNT = "#setAmount";
    final String NAMEACCOUNT = "#nameAccount";
    final String CREATEACCOUNT = "#createAccount";
    final String ACCOUNTTYPE = "#accountType";
    final String ACCOUNTS = "#accounts";

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
        DirectAccess directAccess = new DirectAccess(user, DirectAccess.TESTSAVEFILE);
        controller.setCashFlowAccess(directAccess);
    }

    @BeforeEach
    public void setUpItems() {
        //Before each test
        TextField amount = find(SETAMOUNT);
        TextField name = find(NAMEACCOUNT);
        amount.setText("");
        name.setText("");
        ChoiceBox<String> cb = find(ACCOUNTTYPE);
        cb.getSelectionModel().clearSelection();
    }

    /**
     * 
     */
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

    /**
     * Tests that you get a reaction when button is clicked on
     */
    @Test
    public void testClickOn() {
        clickOn(CREATEACCOUNT);
        assertEquals("Velg en kontotype!", lookup("#errorMessage").queryText().getText());
    }
    @Test
    public void testNewCorrectAccount() {
        TextArea accountOverview = find(ACCOUNTS);
        // hvis null?
        String accounts = accountOverview.getText();

        String name = "first account";
        String amount = "12";

        clickOn(ACCOUNTTYPE);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);

        clickOn(NAMEACCOUNT).write(name);
        clickOn(SETAMOUNT).write(amount);

        clickOn(CREATEACCOUNT);

        assertNotNull(lookup("#accountCreated").queryText().getText());
        assertEquals("Kontoen er opprettet", (lookup("#accountCreated").queryText().getText()));
        assertEquals(accounts + "\n" + "Sparekonto" + ": " + name + "\n" + "   Beløp: " + Double.parseDouble(amount),
                accountOverview.getText());

    }

    @SuppressWarnings(value = "unchecked")
    private <T extends Node> T find(final String query) {
        return (T) lookup(query).queryAll().iterator().next();
    }

    @Test
    public void testMissingFields() {
        TextArea kontoOversikt = find(ACCOUNTS);
        String kontoer1 = kontoOversikt.getText();

        TextField amount = find(SETAMOUNT);
        TextField name = find(NAMEACCOUNT);

        // Test bare beløp
        clickOn(SETAMOUNT).write("34");
        clickOn(CREATEACCOUNT);
        assertEquals("Velg en kontotype!", lookup("#errorMessage").queryText().getText());

        // hvis du bare skriver inn navn
        amount.setText("");
        clickOn(NAMEACCOUNT).write("test");
        clickOn(CREATEACCOUNT);
        assertEquals("Velg en kontotype!", lookup("#errorMessage").queryText().getText());

        // hvis du skriver inn navn og beløp
        name.setText("");
        clickOn(NAMEACCOUNT).write("test");
        clickOn(SETAMOUNT).write("35");
        clickOn(CREATEACCOUNT);
        assertEquals("Velg en kontotype!", lookup("#errorMessage").queryText().getText());

        // hvis du skriver inn type
        amount.setText("");
        name.setText("");
        clickOn(ACCOUNTTYPE);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        clickOn(CREATEACCOUNT);
        assertEquals("Husk å fylle inn alle felt", lookup("#errorMessage").queryText().getText());

        // hvis du skriver inn type og beløp
        clickOn(SETAMOUNT).write("35");
        clickOn(CREATEACCOUNT);
        assertEquals("Husk å fylle inn alle felt", lookup("#errorMessage").queryText().getText());

        // hvis du skriver inn type og navn
        amount.setText("");
        clickOn(NAMEACCOUNT).write("test");
        clickOn(CREATEACCOUNT);
        assertEquals("Husk å fylle inn alle felt", lookup("#errorMessage").queryText().getText());

        String kontoer2 = kontoOversikt.getText();
        assertEquals(kontoer1, kontoer2);

    }

    /**
     * Testing that you can not create an account and get the correct error message
     * when you write the wrong account name
     */
    @Test
    public void testWrongAccountName() {
        TextField name = find(NAMEACCOUNT);

        String name1 = "account1";
        String name2 = null;
        String name3 = "!.";
        String name4 = "thelengthogthisstringistoolong";

        clickOn(SETAMOUNT).write("100");
        clickOn(ACCOUNTTYPE);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);

        // 1
        clickOn(NAMEACCOUNT).write(name1);
        clickOn(CREATEACCOUNT);
        assertEquals("Du kan ikke bruke tall eller tegn i navnet, og det må være mindre enn 20 bokstaver",
                controller.errorMessage.getText());

        // 2 Håndtere hvis null
        assertThrows(NullPointerException.class, () -> {
            clickOn(NAMEACCOUNT).write(name2);
        });

        // 3
        name.setText("");
        clickOn(NAMEACCOUNT).write(name3);
        clickOn(CREATEACCOUNT);
        assertEquals("Du kan ikke bruke tall eller tegn i navnet, og det må være mindre enn 20 bokstaver",
                controller.errorMessage.getText());

        // 4
        name.setText("");
        clickOn(NAMEACCOUNT).write(name4);
        clickOn(CREATEACCOUNT);
        assertEquals("Du kan ikke bruke tall eller tegn i navnet, og det må være mindre enn 20 bokstaver",
                controller.errorMessage.getText());
    }

    /**
     * Testing that you can not create an account and get the correct error message
     * when you write an amount not following the rules
     */
    @Test
    public void testWrongAmount() {
        TextField amount = find(SETAMOUNT);

        String amount1 = "-12";
        String amount2 = "String";
        String amount3 = null;

        clickOn(NAMEACCOUNT).write("Main Account");
        clickOn(ACCOUNTTYPE);
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);

        // 1
        clickOn(SETAMOUNT).write(amount1);
        clickOn(CREATEACCOUNT);
        assertEquals("Beløpet må bestå av tall og kan ikke være mindre enn null", controller.errorMessage.getText());

        // 2
        amount.setText("");
        clickOn(SETAMOUNT).write(amount2);
        clickOn(CREATEACCOUNT);
        assertEquals("Beløpet må bestå av tall og kan ikke være mindre enn null", controller.errorMessage.getText());

        // 3
        assertThrows(NullPointerException.class, () -> {
            clickOn(SETAMOUNT).write(amount3);
        });
    }

    /**
     * Tests that the cashFlowAccess field is an instance of DirectAccess.
     */
    @Test
    public void testCorrectCashFlowAccessInstance() {
        assertTrue(controller.getCashFlowAccess() instanceof DirectAccess);
    }

}
