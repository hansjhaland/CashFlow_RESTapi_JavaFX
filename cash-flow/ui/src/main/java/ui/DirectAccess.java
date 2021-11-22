package ui;

import core.AbstractAccount;
import core.User;
import java.io.IOException;
import json.CashFlowPersistence;

/**
 * Allows controller classes to load data from and save data to a local file.
 */
public class DirectAccess implements CashFlowAccess {

  private User user;
  private CashFlowPersistence cfp;
  public static final String LOCALSAVEFILE = "SaveData.json";
  public static final String TESTSAVEFILE = "SaveDataTest.json";
  private String saveFile;

  /**
   * Constructs a DirectAccess object which gives the indicates 
   * that the local app version is running.
   *
   * @param user the user
   * @param saveFile the savefile 
   */
  public DirectAccess(User user, String saveFile) {
    this.saveFile = saveFile;
    this.user = user;
    this.cfp = new CashFlowPersistence();
    cfp.setSaveFilePath(saveFile);
  }

  /**
   * Getter for user field.
   *
   * @return a user
   */
  public User getUser() {
    return user;
  }

  /**
   * Gets an acount from the user's account list based on its account number.
   *
   * @return the corresponding account if it exists in user's account list. Otherwise null.
   */
  @Override
  public AbstractAccount getAccount(int accountNumber) {
    return user.getAccount(accountNumber);
  }

  /**
   * Adds an account to user's account list.
   */
  @Override
  public void addAccount(AbstractAccount account) {
    if (user != null) {
      user.addAccount(account);
    }

  }

  /**
   * Saves user to local save file.
   */
  @Override
  public void saveUser() throws IllegalStateException, IOException {
    cfp.saveUser(user, saveFile);
  }

  /**
   * Creates a transfer between two accounts.
   *
   * @param payer the paying account
   * @param reciever the recieving account
   * @param amount the amount to be transfered
   */
  @Override
  public void transfer(AbstractAccount payer, AbstractAccount reciever, double amount) {
    payer.transfer(reciever, amount);
  }

  /**
   * Deletes the account with the corresponding account number.
   *
   * @param accountNumber the account number
   * @return true if account is deleted. false if account is not deleted or the user is null.
   */
  @Override
  public boolean deleteAccount(int accountNumber) {
    if (user != null) {
      return user.removeAccount(getAccount(accountNumber));
    }
    return false;
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
