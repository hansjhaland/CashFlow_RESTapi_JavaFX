package core;

/**
 * A class for creating a standard account called checkingaccount that 
 * inherits from AbstractAccount.
 */
public class CheckingAccount extends AbstractAccount {

  /**
   * Initializes a new CheckingAccount-object.
   *
   * @param name the name of the account
   * @param amount the initial balance of the account
   * @param accountNumber the account number of the account
   * @param owner the owner of the account
   * @throws IllegalArgumentException if the name is more than 20 characters long or contains
   *         characters other than letters and spaces
   * @throws IllegalArgumentException if the account number is not between 1000 and 9999
   * @throws IllegalArgumentException if the account number already exists in the users list of
   *         account numbers
   * @throws IllegalStateException if the initial balance is set to be less than 0
   */
  public CheckingAccount(String name, double amount, int accountNumber, User owner) {
    super(name, accountNumber, owner);
    initialDeposit(amount);
  }

  /**
   * Initializes a new CheckingAccount-object, and also generates the next available account number
   * for this particular user.
   *
   * @param name the name of the account
   * @param amount the initial balance of the account
   * @param owner the owner of the account
   * @throws IllegalArgumentException if the name is more than 20 characters long or contains
   *         characters other than letters and spaces
   * @throws IllegalStateException if the maximum number of accounts have been reached for this user
   * @throws IllegalStateException if the initial balance is set to be less than 0
   */
  public CheckingAccount(String name, double amount, User owner) {
    super(name, owner);
    initialDeposit(amount);
  }
}
