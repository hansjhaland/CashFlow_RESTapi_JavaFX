package ui;

import core.AbstractAccount;
import core.User;
import java.io.IOException;

/**
 * Interface that allows controller to work with different sources, e.g. local or server (RESTAPI).
 */
public interface CashFlowAccess {

  /**
   * Gets a user from relevant source.
   *
   * @return a user
   */
  public User getUser();

  /**
   * Gets account with given account number.
   *
   * @param accountNumber the account's account number
   */
  public AbstractAccount getAccount(int accountNumber);

  /**
   * Adds an AbstractAccount to the underlying user.
   *
   * @param account the account
   */
  public void addAccount(AbstractAccount account);

  /**
   * Deletes an account from the underlying user's account list.
   *
   * @param accountNumber the account number of the account to be deleted
   */
  public boolean deleteAccount(int accountNumber);

  /**
   * Adds an transaction to both accouts' transaction history.
   *
   * @param payer paying account
   * @param reciever recieving account
   * @param amount the amount to be transfered
   */
  public void transfer(AbstractAccount payer, AbstractAccount reciever, double amount);

  /**
   * Gets a user object from file if it exists.
   *
   * @return user from file if it exists. Default user otherwise.
   * @throws IllegalStateException IllegalStateException occurred
   * @throws IOException IOExeption occurred
   */
  public User loadInitialUser() throws IllegalStateException, IOException;

  /**
   * Save user to file as JSON object.
   *
   * @throws IllegalStateException IllegalStateException occurred
   * @throws IOException IOExeption occurred
   */
  public void saveUser() throws IllegalStateException, IOException;

}
