package core;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A class for creating a user for the accounts to be created.
 */
public class User {

  private String name;
  private final int userID;
  private Collection<AbstractAccount> accounts = new ArrayList<>();

  // ==============================================================================================
  // Contstructors
  // ==============================================================================================

  /**
   * Initializes a new User-object. UserID must be between 100000 and 999999.
   *
   * @param userID the users identification number
   * @throws IllegalArgumentException if the UserID si not between 100000 and 999999
   */
  public User(int userID) {
    BankHelper.checkIfValidUserID(userID);
    this.userID = userID;
    name = "";
  }

  // ==============================================================================================
  // Functional methods
  // ==============================================================================================

  /**
   * Adds the account to users list of accounts. Also adds the accounts accountnumber to the users
   * list of accountnumbers.
   *
   * @param account the account to be added
   * @return {@code true} if the account was added
   */
  public boolean addAccount(AbstractAccount account) {
    if (getAccountNumbers().contains(account.getAccountNumber())) {
      return false;
    }
    accounts.add(account);
    account.setOwner(this);
    return true;
  }

  /**
   * Removes the given account from the users list of accounts. Also removes the accounts
   * accountnumber from the users list of accountnumbers.
   *
   * @param account the account to be removed
   * @return {@code true} if the account was removed
   */
  public boolean removeAccount(AbstractAccount account) {
    if (accounts.remove(account)) {
      return true;
    }
    return false;
  }

  // ==============================================================================================
  // Getters and setters
  // ==============================================================================================

  public String getName() {
    return name;
  }

  public int getUserID() {
    return userID;
  }

  public Collection<AbstractAccount> getAccounts() {
    return new ArrayList<>(accounts);
  }

  /**
   * A method that holds the accountnumbers of an account in a collection 
   * and puts every accountnumber to its account in the list.
   *
   * @return the lists of all accounts with its accountnumbers.
   */
  public Collection<Integer> getAccountNumbers() {
    Collection<Integer> accountNumbers = new ArrayList<>();
    for (AbstractAccount account : getAccounts()) {
      accountNumbers.add(account.getAccountNumber());
    }
    return accountNumbers;
  }

  /**
   * Returns the account with the coresponding account number, if the user has an account with this
   * account number. If not, it returns {@code null}.
   *
   * @param accountNumber the account number of the account you wish to find
   * @return the account with the account number, or {@code null} if the 
   *        account number doesn't exist
   */
  public AbstractAccount getAccount(int accountNumber) {
    AbstractAccount account = getAccounts().stream()
        .filter(existingAccount -> existingAccount.getAccountNumber() == accountNumber)
        .findAny()
        .orElse(null);
    return account;
  }

  /**
   * Changes the name of the user. Name must be 20 characters or less, and can only
   * consist of letters and spaces.
   *
   * @param name the name you wish to change to
   * @throws IllegalArgumentException if the name is more than 20 characters long, or does not only
   *         contain letters and spaces
   */
  public void setName(String name) {
    if (!BankHelper.isValidName(name)) {
      throw new IllegalArgumentException(
          "The name " + name.length()
          + " must be 20 characters or less and only contain letters and spaces");
    }
    this.name = name;
  }

  @Override
  public String toString() {
    String string = "Name: " + getName() + "\nUserID: " + getUserID() + "\nAccounts:";
    StringBuffer sb = new StringBuffer();
    sb.append(string);
    for (AbstractAccount account : getAccounts()) {
      sb.append("\n" + account.toString());
    }
    return sb.toString();
  }
}
