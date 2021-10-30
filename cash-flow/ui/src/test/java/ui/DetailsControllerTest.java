package ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

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
import javafx.scene.text.*;

import core.User;
import core.CheckingAccount;
import core.SavingsAccount;
import core.AbstractAccount;
import core.BSUAccount;



public class DetailsControllerTest extends ApplicationTest{
    
   
    final String TRANSACTIONHISTORY = "#accountHistory";  //TextArea
    final String DETAILEDACCOUNT = "#chooseAccount";          //ChoiceBox
    final String RECIPIENTACCOUNT = "#transferAccount";      //ChoiceBox
    final String AMOUNT = "#transferAmount";                //TextField
    final String TRANSFER = "#transfer";                   //Button
    final String FEEDBACK = "#feedback";                   //Text
    final String DELETEBUTTON = "#deleteButton";             //Button
    final String DELETEMESSAGE = "#deleteMessage";           //Text

    private DetailsController controller;
    private CashFlowPersistence cfp = new CashFlowPersistence();
    private User user = new User(123456);
    
    private final static String testSaveFile = "SaveDataTest.json";
    
    @Override
    public void start(final Stage stage) throws Exception {
        new CheckingAccount("ChA", 1000, 1000, user);
        new SavingsAccount("SA", 1000, 1001, user);
        new BSUAccount("BSUA", 1000, 1002, user);
        try{
            cfp.saveUser(user, testSaveFile);
        } catch (IllegalStateException e) {
            fail(e);
        } catch (IOException e) {
            fail(e);
        }
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("Details_test.fxml"));
        final Parent root = loader.load();
        this.controller = loader.getController();
        stage.setScene(new Scene(root));
        stage.show();
        controller.loadNewUser(testSaveFile);
    }

    @SuppressWarnings (value="unchecked")
    private <T extends Node> T find(final String query) {
        return (T) lookup(query).queryAll().iterator().next();
    }

    @BeforeEach
    public void setUpItems() {
        ChoiceBox<String> detailedAccount = find(DETAILEDACCOUNT);
        ChoiceBox<String> recipientAccount = find(RECIPIENTACCOUNT);
        TextField amount = find(AMOUNT);
        Text feedback = find(FEEDBACK);
        detailedAccount.getSelectionModel().clearSelection();
        recipientAccount.getSelectionModel().clearSelection();
        amount.setText("");
        feedback.setText("");
    }

    @AfterAll
    public static void deleteTestJsonFile() {
        Path testFilePath = Paths.get(System.getProperty("user.home"), testSaveFile);
        File testFile = testFilePath.toFile();
        testFile.delete();
    }

    /**
     * Tests that controller is loaded.
     */
    @Test
    public void testController() {
        assertNotNull(this.controller);
    }

    /**
     * Tests that a checking account is chosen when expected.
     */
    @Test
    public void testChooseCheckingAccount(){
        clickOn(DETAILEDACCOUNT);
        type(KeyCode.ENTER);
        ChoiceBox<String> detailedAccount = find(DETAILEDACCOUNT);
        String account1 = (String) detailedAccount.getValue();
        assertEquals("ChA: 1000", account1);
        clickOn(RECIPIENTACCOUNT);
        type(KeyCode.ENTER);
        ChoiceBox<String> recipientAccount = find(RECIPIENTACCOUNT);
        String account2 = (String) recipientAccount.getValue();
        assertEquals("ChA: 1000", account2);
    }

    /**
     * Test that a savings account is chosen when expected.
     */
    @Test
    public void testChooseSavingsAccount(){
        clickOn(DETAILEDACCOUNT);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        ChoiceBox<String> detailedAccount = find(DETAILEDACCOUNT);
        String account1 = (String) detailedAccount.getValue();
        assertEquals("SA: 1001", account1);
        clickOn(RECIPIENTACCOUNT);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        ChoiceBox<String> recipientAccount = find(RECIPIENTACCOUNT);
        String account2 = (String) recipientAccount.getValue();
        assertEquals("SA: 1001", account2);
    }

    /**
     * Tests that a BSU account is chosen when expected.
     */
    @Test
    public void testChooseBSUAccount(){
        clickOn(DETAILEDACCOUNT);
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        ChoiceBox<String> detailedAccount = find(DETAILEDACCOUNT);
        String account1 = (String) detailedAccount.getValue();
        assertEquals("BSUA: 1002", account1);
        clickOn(RECIPIENTACCOUNT);
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        ChoiceBox<String> recipientAccount = find(RECIPIENTACCOUNT);
        String account2 = (String) recipientAccount.getValue();
        assertEquals("BSUA: 1002", account2);
    }

    /**
     * Tests that clicking the transfer button without selecting any accounts
     * gives the proper error message.
     */
    @Test
    public void testTransferWithoutAnyAccounts(){
        clickOn(TRANSFER);
        assertEquals("Velg hvilken konto du vil overføre fra/til.", lookup(FEEDBACK)
                    .queryText().getText());
    }

    /**
     * Tests that clicking the transfer button without selecting
     * a detailed account gives the proper error message.
     */
    @Test
    public void testTransferWithoutDetailedAccount(){
        clickOn(RECIPIENTACCOUNT);
        type(KeyCode.ENTER);
        clickOn(TRANSFER);
        assertEquals("Velg hvilken konto du vil overføre fra/til.", lookup(FEEDBACK)
                    .queryText().getText());
    }

    /**
     * Tests that clicking the transfer button without selecting
     * a recipient account gives the proper error message.
     */
    @Test
    public void testTransferWithoutRecipientAccount(){
        clickOn(DETAILEDACCOUNT);
        type(KeyCode.ENTER);
        clickOn(TRANSFER);
        assertEquals("Velg hvilken konto du vil overføre fra/til.", lookup(FEEDBACK)
                    .queryText().getText());
    }

    /**
     * Tests that clicking the transfer button after selecting
     * the same account as payer and recipient gives the
     * proper error message.
     */
    @Test 
    public void testTransferWithSameAccounts(){
        clickOn(RECIPIENTACCOUNT);
        type(KeyCode.ENTER);
        clickOn(DETAILEDACCOUNT);
        type(KeyCode.ENTER);
        TextField amount = find(AMOUNT);
        amount.setText("10");
        clickOn(TRANSFER);
        assertEquals("Sendekonto kan ikke være lik mottakerkonto", lookup(FEEDBACK)
                    .queryText().getText());
    }

    /**
     * Tests that no error message is given when a valid transfer is executed.
     */
    @Test 
    public void testValidTransfer(){
        clickOn(RECIPIENTACCOUNT);
        type(KeyCode.ENTER);
        clickOn(DETAILEDACCOUNT);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        TextField amount = find(AMOUNT);
        amount.setText("10");
        clickOn(TRANSFER);
        TextArea transactionHistory = find(TRANSACTIONHISTORY);
        String historyString = transactionHistory.getText();
        assertTrue(!historyString.equals(""));
    }

    /**
     * Tests that a transfer with negative amount gives
     * proper error message.
     */
    @Test
    public void testTransferWithNegativeAmount() {
        clickOn(RECIPIENTACCOUNT);
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        clickOn(DETAILEDACCOUNT);
        type(KeyCode.ENTER);
        TextField amount = find(AMOUNT);
        amount.setText("-10");
        clickOn(TRANSFER);
        assertEquals("Overføringsbeløpet må være større enn 0.", lookup(FEEDBACK)
                    .queryText().getText());
    }

    /**
     * Tests that withdrawing from a savings account more than
     * ten times is impossible.
     */
    @Test
    public void testExceedingMaxNumberOfWithdrawalsSavingsAccount(){
        clickOn(DETAILEDACCOUNT);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        clickOn(RECIPIENTACCOUNT);
        type(KeyCode.ENTER);
        TextField amount = find(AMOUNT);
        for (int i = 0; i < 12; i++){
            amount.setText("10");
            clickOn(TRANSFER);
        }
        assertEquals("Maksimalt antall uttak fra sparekonto nådd", lookup(FEEDBACK)
                    .queryText().getText());
    }

    /**
     * Tests that a transfer with amount exceeding account balance
     * is impossible.
     */
    @Test
    public void testTransferMoreThanAccountBalance(){
        clickOn(DETAILEDACCOUNT);
        type(KeyCode.ENTER);
        clickOn(RECIPIENTACCOUNT);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        TextField amount = find(AMOUNT);
        amount.setText("10000");
        clickOn(TRANSFER);
        assertEquals("ChA har ikke nok penger på konto.", lookup(FEEDBACK)
                    .queryText().getText());
    }

    @Test
    public void testDeleteAccountWithoutChoosingAccount() {
        clickOn(DELETEBUTTON);
        assertEquals("Du må velge en konto først.", lookup(DELETEMESSAGE)
            .queryText().getText());
    }

    @Test
    public void testDeleteAccountWithMoney() {
        clickOn(DETAILEDACCOUNT);
        type(KeyCode.ENTER);
        clickOn(DELETEBUTTON);
        assertEquals("Du må ha saldo 0 eller overføre pengene til en annen konto", lookup(DELETEMESSAGE)
            .queryText().getText());
    }

    @Test
    public void testValidDeleteAccount() {
        clickOn(DETAILEDACCOUNT);
        type(KeyCode.ENTER);
        clickOn(RECIPIENTACCOUNT);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        TextField amount = find(AMOUNT);
        amount.setText("1000");
        clickOn(TRANSFER);

        clickOn(DELETEBUTTON);
        assertEquals("Konto slettet.", lookup(DELETEMESSAGE)
            .queryText().getText());
    }

}
