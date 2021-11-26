package ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import core.User;

public class DetailsIT extends ApplicationTest {
    
  private DetailsController controller;
  private final static String testSaveFile = "SaveDataIT.json";
  private RemoteAccess remoteAccess;
  private User user;

  final String DETAILEDACCOUNT = "#chooseAccount"; // ChoiceBox
  final String RECIPIENTACCOUNT = "#transferAccount"; // ChoiceBox
  final String AMOUNT = "#transferAmount"; // TextField
  final String TRANSFER = "#transfer"; // Button
  final String DELETEBUTTON = "#deleteButton"; // Button
  final String SETBALANCE = "#setBalance";

  @SuppressWarnings(value = "unchecked")
  private <T extends Node> T find(final String query) {
    return (T) lookup(query).queryAll().iterator().next();
  }

  @Override
  public void start(final Stage stage) throws Exception {
    final FXMLLoader loader = new FXMLLoader(getClass().getResource("Details_it.fxml"));
    final Parent root = loader.load();
    this.controller = loader.getController();
    stage.setScene(new Scene(root));
    stage.show();
  }

  @BeforeEach
  public void setupItems() throws URISyntaxException {
    try (Reader reader = new InputStreamReader(getClass().getResourceAsStream("it-cashflow.json"))) {
      String port = System.getProperty("user.port"); //hvor skal dette?
      assertNotNull(port, "No user.port system property set");
      String baseUri = "http://localhost:" + port + "/user/";
      URI uri = new URI(baseUri); //??
      System.out.println("Base CashFlowAccess URI: " + baseUri); //??
      this.remoteAccess = new RemoteAccess(uri, testSaveFile);

      assertNotNull(this.controller);
      this.controller.setCashFlowAccess(remoteAccess);
      user = remoteAccess.getUser();

    } catch (IOException ioe) {
      fail(ioe.getMessage());
    }
  }

  @AfterAll
  public static void deleteTestJsonFile() {
    Path testFilePath = Paths.get(System.getProperty("user.home"), testSaveFile);
    File testFile = testFilePath.toFile();
    testFile.delete();
  }

  @Test
  public void testTransfer() {
    clickOn(DETAILEDACCOUNT);
    type(KeyCode.ENTER);
    TextField setBalance = find(SETBALANCE);

    assertEquals("100.0 kr", setBalance.getText());
    assertEquals(100, user.getAccount(2345).getBalance());
    assertEquals(100, user.getAccount(5432).getBalance());

    String amount = "100";
    clickOn(AMOUNT).write(amount);
    clickOn(RECIPIENTACCOUNT);
    type(KeyCode.DOWN);
    type(KeyCode.ENTER);
    clickOn(TRANSFER);

    assertEquals("0.0 kr", setBalance.getText());
    assertEquals(0, user.getAccount(2345).getBalance());
    assertEquals(200, user.getAccount(5432).getBalance());

    clickOn(DELETEBUTTON);
    assertNull(user.getAccount(2345));
  }
}
