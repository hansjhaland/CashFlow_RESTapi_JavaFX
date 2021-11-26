package ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import core.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import json.CashFlowPersistence;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class CashFlowControllerTest extends ApplicationTest {

    private CashFlowController controller;
    final String SETAMOUNT = "#setAmount"; // TextField
    final String NAMEACCOUNT = "#nameAccount"; // TextField
    final String CREATEACCOUNT = "#createAccount"; // Button
    final String ACCOUNTTYPE = "#accountType"; // ChoiceBox
    final String ACCOUNTS = "#accounts"; // TextArea
    final String DETAILSANDTRANSFERS = "#detailsAndTransfers"; // Button

    private final static String testSaveFile = "SaveDataTest.json";
    private CashFlowPersistence cfp = new CashFlowPersistence();

    /**
     * Loads the CashFlowTest fxml. Sets the controller with the fxml and displays it for the user.
     */
    @Override
    public void start(final Stage stage) throws Exception {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("CashFlowTest.fxml"));
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

    @AfterAll
    public static void deleteTestJsonFile() {
        Path testFilePath = Paths.get(System.getProperty("user.home"), testSaveFile);
        File testFile = testFilePath.toFile();
        testFile.delete();
    }

    /**
     * Tests that the controller is loaded.
     */
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

    /**
     * Tests that correct input adds a new account to the
     * account overview.
     */
    @Test
    public void testNewCorrectAccount() {
        TextArea accountOverview = find(ACCOUNTS);
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
        assertEquals(accounts + "\n" + "Sparekonto" + ": " + name + "\n" + "   Beløp: 12.0 kr",
                accountOverview.getText());

    }

    @SuppressWarnings(value = "unchecked")
    private <T extends Node> T find(final String query) {
        return (T) lookup(query).queryAll().iterator().next();
    }

    /**
     * Tests that missing input gives corresponding error message.
     */
    @Test
    public void testMissingFields() {
        TextArea kontoOversikt = find(ACCOUNTS);
        String kontoer1 = kontoOversikt.getText();

        TextField amount = find(SETAMOUNT);
        TextField name = find(NAMEACCOUNT);

        // Test only amount
        clickOn(SETAMOUNT).write("34");
        clickOn(CREATEACCOUNT);
        assertEquals("Velg en kontotype!", lookup("#errorMessage").queryText().getText());

        // Test only name
        amount.setText("");
        clickOn(NAMEACCOUNT).write("test");
        clickOn(CREATEACCOUNT);
        assertEquals("Velg en kontotype!", lookup("#errorMessage").queryText().getText());

        // Test name and amount without type
        name.setText("");
        clickOn(NAMEACCOUNT).write("test");
        clickOn(SETAMOUNT).write("35");
        clickOn(CREATEACCOUNT);
        assertEquals("Velg en kontotype!", lookup("#errorMessage").queryText().getText());

        // Test only type
        amount.setText("");
        name.setText("");
        clickOn(ACCOUNTTYPE);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        clickOn(CREATEACCOUNT);
        assertEquals("Husk å fylle inn alle felt", lookup("#errorMessage").queryText().getText());

        // Test only type and amount
        clickOn(SETAMOUNT).write("35");
        clickOn(CREATEACCOUNT);
        assertEquals("Husk å fylle inn alle felt", lookup("#errorMessage").queryText().getText());

        // Test only type and name
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

        // name1
        clickOn(NAMEACCOUNT).write(name1);
        clickOn(CREATEACCOUNT);
        assertEquals("Du kan ikke bruke tall eller tegn i navnet, og det må være mindre enn 20 bokstaver",
                controller.errorMessage.getText());

        // name2
        assertThrows(NullPointerException.class, () -> {
            clickOn(NAMEACCOUNT).write(name2);
        });

        // name3
        name.setText("");
        clickOn(NAMEACCOUNT).write(name3);
        clickOn(CREATEACCOUNT);
        assertEquals("Du kan ikke bruke tall eller tegn i navnet, og det må være mindre enn 20 bokstaver",
                controller.errorMessage.getText());

        // name4
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

        // amount1
        clickOn(SETAMOUNT).write(amount1);
        clickOn(CREATEACCOUNT);
        assertEquals("Beløpet må bestå av tall og kan ikke være mindre enn null", controller.errorMessage.getText());

        // amount2
        amount.setText("");
        clickOn(SETAMOUNT).write(amount2);
        clickOn(CREATEACCOUNT);
        assertEquals("Beløpet må bestå av tall og kan ikke være mindre enn null", controller.errorMessage.getText());

        // amount3
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

    /**
     * Tests that current window is closing, and new window details page is showing when button is
     * pressed.
     */
    @Test
    public void testOnNextPage() {
        //Need to create an account before going to the next page
        clickOn(ACCOUNTTYPE);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);

        clickOn(NAMEACCOUNT).write("Account");
        clickOn(SETAMOUNT).write("5");
        clickOn(CREATEACCOUNT);
        WaitForAsyncUtils.waitForFxEvents();

        clickOn(DETAILSANDTRANSFERS);
        WaitForAsyncUtils.waitForFxEvents();


        assertNull(findSceneRootWithId("cashFlowRoot"));
        assertNotNull(findSceneRootWithId("localDetails"));
    }

    private Parent findSceneRootWithId(String id) {
        for (Window window : Window.getWindows()) {
          if (window instanceof Stage && window.isShowing()) {
            var root = window.getScene().getRoot();
            if (id.equals(root.getId())) {
              return root;
            }
          }
        }
        return null;
      }

}
