package core;

import core.Transaction.TransactionType;

/**
 * A class that creats a savingsaccount that inherits from AbstractAccount class.
 */
public class SavingsAccount extends AbstractAccount {

  /**
   * Initializes a new SavingsAccount-object.
   *
   * @param name the name of the account
   * @param accountNumber the account number of the account
   * @param owner the owner of the account
   * @throws IllegalArgumentException if the name is more than 20 characters long
   * @throws IllegalArgumentException if the name contains characters other than letters and spaces
   * @throws IllegalArgumentException if the account number is not between 1000 and 9999
   * @throws IllegalArgumentException if the account number already exists in the users list of
   *         account numbers
   * @throws IllegalStateException if the initial balance is set to be less than 0
   */
  public SavingsAccount(String name, double balance, int accountNumber, User owner) {
    super(name, accountNumber, owner);
    initialDeposit(balance);
  }

  /**
   * Initializes a new SavingsAccount-object, and also generates the next available 
   * account number for this particular user.
   *
   * @param name the name of the account
   * @param owner the owner of the account
   * @throws IllegalArgumentException if the name is more than 20 characters long or contains
   *         characters other than letters and spaces
   * @throws IllegalStateException if the maximum number of accounts have been reached for this user
   * @throws IllegalStateException if the initial balance is set to be less than 0
   */
  public SavingsAccount(String name, double balance, User owner) {
    super(name, owner);
    initialDeposit(balance);
  }

  /**
   * Withdraws the given amount from the account. Also adds this transaction to the transaction
   * history.
   *
   * @param amount the amount to be withdrawn
   * @throws IllegalArgumentException if the given amount is negative
   * @throws IllegalStateException if the withdrawal of the amount leads to the 
   *        balance being negative
   * @throws IllegalStateException if the maximum number of withdrawals and transfers from this
   *         account has been reached, which is 10
   */
  @Override
  public void withdraw(double amount) {
    checkIfWithdrawalOrTransferPossible();
    super.withdraw(amount);
  }

  /**
   * Transfers the spesified amount of money from this account to the recipient account.
   *
   * @param recievingAccount the account that will recieve the money
   * @param amount the amount of money to be transferred to the recipient account
   * @throws IllegalArgumentException if the amount is negative
   * @throws IllegalArgumentException if 'recievingAccount' is {@code null}
   * @throws IllegalArgumentException if 'recievingAccount' is the account that is also transfering
   *         the money
   * @throws IllegalStateException if the balance is less than 0 when the amount is added
   * @throws IllegalStateException if the maximum number of withdrawals and transfers has been
   *         reached, which is 10
   */
  @Override
  public void transfer(AbstractAccount recievingAccount, double amount) {
    checkIfWithdrawalOrTransferPossible();
    super.transfer(recievingAccount, amount);
  }

  private void checkIfWithdrawalOrTransferPossible() {
    if (!isWithdrawalOrTransferPossible()) {
      throw new IllegalStateException(
          "Cannot withdraw, because number of withdrawals and transfers have been reached (10)");
    }
  }

  /**
   * Checks if the number of withdrawals and transfer hase been reached. This limit is set to be 10
   * withdrawals or transfers.
   *
   * @return {@code false} if the account has reached the withdrawal/transfer limit of 10
   */
  public boolean isWithdrawalOrTransferPossible() {
    return getNumberOfWithdrawals() + getNumberOfTransfersFromThisAccount() < 10;
  }

  private long getNumberOfWithdrawals() {
    long numberOfWithdrawals = getTransactionHistory().stream()
        .filter(transaction -> transaction.getType() == TransactionType.WITHDRAWAL).count();
    return numberOfWithdrawals;
  }

  private long getNumberOfTransfersFromThisAccount() {
    long numberOfTransfers =
        getTransactionHistory().stream()
            .filter(transaction -> transaction.getType() == TransactionType.TRANSFER)
            .filter(transfer -> transfer.getPayersAccountNumber() == getAccountNumber()).count();
    return numberOfTransfers;
  }
}
