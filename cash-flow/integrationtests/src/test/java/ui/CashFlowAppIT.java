package ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import core.User;

/**
 * End-to-end testing. Tests that both the core, the CashFlow page,
 * and the server works together.
 */
public class CashFlowAppIT extends ApplicationTest {

  private CashFlowController controller;
  private final static String testSaveFile = "SaveDataIT.json";
  private RemoteAccess remoteAccess;
  private User user;

  final String SETAMOUNT = "#setAmount";
  final String NAMEACCOUNT = "#nameAccount";
  final String CREATEACCOUNT = "#createAccount";
  final String ACCOUNTTYPE = "#accountType";
  final String ACCOUNTS = "#accounts";
  final String NEXTPAGE = "#detailsAndTransfers";

  @SuppressWarnings(value = "unchecked")
  private <T extends Node> T find(final String query) {
      return (T) lookup(query).queryAll().iterator().next();
  }

  /**
   * Loads the MainPage fxml (CashFlowController). 
   * Sets the controller with the fxml and displays it for the user.
   */
  @Override
  public void start(final Stage stage) throws Exception {
    final FXMLLoader loader = new FXMLLoader(getClass().getResource("MainPage_it.fxml"));
    final Parent root = loader.load();
    this.controller = loader.getController();
    stage.setScene(new Scene(root));
    stage.show();
  }

  /**
   * Starts the server. Sets the controllers cashflowaccess-object with right URI.
   * @throws URISyntaxException if the server can not be started.
   */
  @BeforeEach
  public void setupItems() throws URISyntaxException {
    try (Reader reader = new InputStreamReader(getClass().getResourceAsStream("it-cashflow.json"))) {
      String port = System.getProperty("user.port"); 
      assertNotNull(port, "No user.port system property set");
      String baseUri = "http://localhost:" + port + "/user/";
      URI uri = new URI(baseUri); //??
      System.out.println("Base CashFlowAccess URI: " + baseUri); 
      this.remoteAccess = new RemoteAccess(uri, testSaveFile);

      assertNotNull(this.controller);
      this.controller.setCashFlowAccess(remoteAccess);
      user = remoteAccess.getUser();

    } catch (IOException ioe) {
      fail(ioe.getMessage());
    }
  }

  /**
   * Deletes the Json test-file after the tests are finished.
   */
  @AfterAll
  public static void deleteTestJsonFile() {
    Path testFilePath = Paths.get(System.getProperty("user.home"), testSaveFile);
    File testFile = testFilePath.toFile();
    testFile.delete();
    
  }

  /**
   * Test for creating an account and checking if the user-object
   * contains the account, and that the UI is updated.
   */
  @Test
  public void testCreateAccount() {
    TextArea accountOverview = find(ACCOUNTS);
    String accounts = accountOverview.getText();

    String name = "first account";
    String amount = "12";

    clickOn(NAMEACCOUNT).write(name);
    clickOn(SETAMOUNT).write(amount);
    
    clickOn(ACCOUNTTYPE);
    type(KeyCode.DOWN);
    type(KeyCode.ENTER);

    clickOn(CREATEACCOUNT);

    assertEquals(accounts + "\n" + "Sparekonto" + ": " + name + "\n" + "   Bel??p: " 
                          + Double.parseDouble(amount) + " kr",
                accountOverview.getText());

    assertTrue(user.getAccounts()
                   .stream()
                   .anyMatch(account -> account.getName().equals("first account")));
  }
}