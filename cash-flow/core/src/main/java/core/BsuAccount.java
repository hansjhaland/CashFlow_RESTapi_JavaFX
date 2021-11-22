package core;


/**
 * A class that creates a Bsu account with its restrictions.
 */
public class BsuAccount extends AbstractAccount {

  /**
   * Initializes a new BsuAccount-object.
   *
   * @param name the name of the account
   * @param accountNumber the account number of the account
   * @param owner the owner of the account
   * @throws IllegalArgumentException if the name is more than 20 characters long or contains
   *         characters other than letters and spaces
   * @throws IllegalArgumentException if the account number is not between 1000 and 9999 or already
   *         exists in the users list of account numbers
   * @throws IllegalStateException if the initial balance is set to be less than 0 or 
   *         more than 25000 or the user already owns a Bsu-account
   */
  public BsuAccount(String name, double balance, int accountNumber, User owner) {
    super(name, accountNumber, owner);
    checkIfUserDoesNotHaveBsu(owner);
    setOwner(owner);
    checkIfValidDeposit(balance);
    initialDeposit(balance);
  }

  /**
   * Initializes a new BsuAccount-object, and also generates the next available account number for
   * this particular user.
   *
   * @param name the name of the account
   * @param owner the owner of the account
   * @throws IllegalArgumentException if the name is more than 20 characters long or contains
   *         characters other than letters and spaces
   * @throws IllegalStateException if the maximum number of accounts have been reached for this user
   * @throws IllegalStateException if the initial balance is set to be less than 0 
   *         or more than 25000 or the user already owns a Bsu-account
   */
  public BsuAccount(String name, double balance, User owner) {
    super(name, owner);
    checkIfUserDoesNotHaveBsu(owner);
    setOwner(owner);
    checkIfValidDeposit(balance);
    initialDeposit(balance);
  }

  /**
   * Deposits the given amount in to the account, which must not exceed 25000. Also adds this
   * transaction to the transaction history.
   *
   * @param amount the amount to be deposited
   * @throws IllegalArgumentException if the given amount is negative
   * @throws IllegalStateException if the deposit leads to the balance exceeding the cap of 25000
   */
  @Override
  public void deposit(double amount) {
    checkIfValidDeposit(amount);
    super.deposit(amount);
  }

  /**
   * This method does nothing, because you cannot withdraw from a Bsu account, unless its used as
   * deductable for a mortgage.
   *
   * @param amount the amount to be withdrawn
   */
  @Override
  public void withdraw(double amount) {}

  /**
   * This method does nothing, because you cannot transfer from a Bsu account, unless its used as
   * deductable for a mortgage.
   *
   * @param amount the amount to be transfered
   */
  @Override
  public void transfer(AbstractAccount recievingAccount, double amount) {}

  private void checkIfValidDeposit(double amount) {
    if (!isValidDeposit(amount)) {
      throw new IllegalStateException("The deposit of '" + amount 
          + "' led to the balance exceeding the cap of 25000");
    }
  }

  /**
   * Checks whether the given user has a Bsu account or not.
   *
   * @param user to be checked.
   */
  private void checkIfUserDoesNotHaveBsu(User user) {
    if (user != null) {
      long numberOfBsu = user.getAccounts().stream()
          .filter(account -> account instanceof BsuAccount).count();
      if (numberOfBsu > 1) {
        user.removeAccount(this);
        throw new IllegalStateException("A user can only own excactly one Bsu-account");
      }
    }
  }

  /**
   * Checks if the deposit leads to the balance of the BsuAccount exceeding the cap of 25000.
   *
   * @param amount the amount to be added
   * @return {@code false} if the balance exceeds 25000
   */
  public boolean isValidDeposit(double amount) {
    double newBalance = amount + getBalance();
    return newBalance <= 25000;
  }
}
