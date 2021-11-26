package ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.AbstractAccount;
import core.User;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import json.CashFlowPersistence;

/**
 * Allows controller classes to get data from and send data to a server with
 * http requests.
 */
public class RemoteAccess implements CashFlowAccess {

  private final URI endpointBaseUri;
  private ObjectMapper objectMapper;
  private User user;
  private CashFlowPersistence cfp;
  public static final String SERVERSAVEFILE = "Server-SaveData.json";
  private String saveFile;

  /**
   * Constructs a RemoteAccess object which gives the indicates that the remote
   * app version is running.
   *
   * @param endpointBaseUri the server URI
   */
  public RemoteAccess(URI endpointBaseUri) {
    this.saveFile = SERVERSAVEFILE;
    this.endpointBaseUri = endpointBaseUri;
    this.objectMapper = CashFlowPersistence.createObjectMapper();
    this.cfp = new CashFlowPersistence();
    cfp.setSaveFilePath(SERVERSAVEFILE);
  }

  /**
   * Constructs a RemoteAccess object which gives the indicates that the remote
   * app version is running.
   *
   * @param endpointBaseUri the server URI
   */
  public RemoteAccess(URI endpointBaseUri, String saveFile) {
    this.saveFile = saveFile;
    this.endpointBaseUri = endpointBaseUri;
    this.objectMapper = CashFlowPersistence.createObjectMapper();
    this.cfp = new CashFlowPersistence();
    cfp.setSaveFilePath(saveFile);
  }

  /**
   * Gets a user by sending a http GET request if user field is not set.
   */
  public User getUser() {
    if (user == null) {
      HttpRequest request = HttpRequest.newBuilder(endpointBaseUri).header("Accept", 
          "application/json").GET().build();
      try {
        final HttpResponse<String> response = HttpClient.newBuilder().build().send(request,
            HttpResponse.BodyHandlers.ofString());
        this.user = objectMapper.readValue(response.body(), User.class);
      } catch (IOException | InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
    return user;
  }

  /**
   * Creates a URI with a given account number.
   *
   * @param accountNumber the account number.
   * @return a URI with the account number.
   */
  private URI accountUri(int accountNumber) {
    return endpointBaseUri.resolve(String.valueOf(accountNumber));
  }

  /**
   * Gets the account with the given account number.
   *
   * @param accountNumber the account number
   * @return the account with the given account number
   */
  @Override
  public AbstractAccount getAccount(int accountNumber) {
    AbstractAccount oldAccount = null;
    if (user != null) {
      oldAccount = this.user.getAccount(accountNumber);
    }
    if (oldAccount == null) {
      HttpRequest request = HttpRequest.newBuilder(accountUri(accountNumber))
          .header("Accept", "application/json").GET()
          .build();
      try {
        final HttpResponse<String> response = HttpClient.newBuilder().build().send(request,
            HttpResponse.BodyHandlers.ofString());
        String responseString = response.body();
        System.out.println("getAccount(" + accountNumber + ") response: " + responseString);
        AbstractAccount account = objectMapper.readValue(responseString, AbstractAccount.class);
        if (user != null) {
          this.user.addAccount(account);
        }
        return account;
      } catch (IOException | InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
    return oldAccount;
  }

  /**
   * Sends a http PUT request with an account and adds the account to the user's
   * account list.
   *
   * @param account the account to be added.
   */
  private void putAccount(AbstractAccount account) {
    try {
      String json = objectMapper.writeValueAsString(account);
      HttpRequest request = HttpRequest.newBuilder(accountUri(account.getAccountNumber()))
          .header("Accept", "application/json").header("Content-Type", "application/json")
          .PUT(BodyPublishers.ofString(json)).build();
      final HttpResponse<String> response = HttpClient.newBuilder().build().send(request,
          HttpResponse.BodyHandlers.ofString());
      String responseString = response.body();
      Boolean added = objectMapper.readValue(responseString, Boolean.class);
      if (added != null) {
        user.addAccount(account);
      }
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Adds an account with the helper method putAccount().
   *
   * @param account the account to be added.
   */
  @Override
  public void addAccount(AbstractAccount account) {
    putAccount(account);
  }

  /**
   * Sends a http DELETE with an account and deletes the given account from the
   * server and the user's account list.
   *
   * @param accountNumber the account number.
   */
  @Override
  public boolean deleteAccount(int accountNumber) {
    try {
      HttpRequest request = HttpRequest.newBuilder(accountUri(accountNumber))
          .header("Accept", "application/json").DELETE().build();
      final HttpResponse<String> response = HttpClient.newBuilder().build().send(request,
          HttpResponse.BodyHandlers.ofString());
      String responseString = response.body();
      Boolean removed = objectMapper.readValue(responseString, Boolean.class);
      if (removed != null) {
        return user.removeAccount(user.getAccount(accountNumber));
      }
      return false;
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Creates a transfer between two accounts and uppdates both accounts by sending
   * one http PUT request for each account.
   *
   * @param payer    the paying account
   * @param reciever the recieving account
   * @param amount   the amount to be transfered
   */
  @Override
  public void transfer(AbstractAccount payer, AbstractAccount reciever, double amount) {
    payer.transfer(reciever, amount);
    putAccount(payer);
    putAccount(reciever);
  }

  /**
   * Saves the user to a local file.
   */
  @Override
  public void saveUser() throws IllegalStateException, IOException {
    cfp.saveUser(user, saveFile);
  }

  /**
   * Loads a user from file if the file exists. Returns a default user otherwise.
   *
   * @return a user from file if the file exists. A default user otherwise.
   */
  @Override
  public User loadInitialUser() throws IllegalStateException, IOException {
    if (cfp.doesFileExist(saveFile)) {
      return cfp.loadUser(saveFile);
    }
    return new User(123456);
  }
}
