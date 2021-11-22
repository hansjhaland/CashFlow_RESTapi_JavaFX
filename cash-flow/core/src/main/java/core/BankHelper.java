package core;


/**
 * Library for checking and validating certain parameters and conditions for all our
 * cashflow-objects.
 */
public final class BankHelper {

  /**
   * Checks if the UserID is between 100000 and 999999.
   * 
   * @param userID the UserID to be checked
   * @throws IllegalArguementException if the UserID isn't between 100000 and 999999
   */
  static void checkIfValidUserID(int userID) {
    int numberOfDigits = (int) Math.log10(userID) + 1;
    if (numberOfDigits != 6) {
      throw new IllegalArgumentException(
          "UserID must be between 100000 and 999999, but had: " + numberOfDigits + " digits.");
    }
  }

  /**
   * Checks if the account number is taken.
   * 
   * @param accountNumber the account number to check
   * @param user the user to check
   * @throws IllegalStateException if the account number is taken
   */
  static void checkIfAccountNumberIsTaken(int accountNumber, User user) {
    for (int exisitingAccountNumber : user.getAccountNumbers()) {
      if (exisitingAccountNumber == accountNumber) {
        throw new IllegalStateException("The user already has an account with account number: "
            + accountNumber);
      }
    }
  }

  /**
   * Checks if the name is 20 characters or less. Checks if the name is only letters and spaces.
   * 
   * @param name the name to be checked
   * @throws IllegalArgumentException if the name is more than 20 characters long or does not only
   *         contain letters and spaces
   */
  static void checkIfValidName(String name) {
    if (!isValidName(name)) {
      throw new IllegalArgumentException(
          "The name " + name.length() 
              + " must be 20 characters or less and only contain letters and spaces");
    }
  }

  /**
   * Checks if the amount added sets the balance in an invalid state (less than 0).
   * Amount can be negative if you are checking a withdrawal.
   * 
   * @param amount the amount to be added to the balance
   * @param account the account to be checked
   * @throws IllegalStateException if the balance is less than 0 when the amount is added
   */
  static void checkIfValidBalance(double amount, AbstractAccount account) {
    if (!isBalanceValidWhenAdding(amount, account)) {
      throw new IllegalStateException(
          "The balance of the account cannot be negative, and adding the amount made it negative");
    }
  }


  /**
   * Checks if the account number is between 1000 and 9999.
   * 
   * @param accountNumber the acocunt number to be checked
   * @throws IllegalArgumentException if the account number is not between 1000 and 9999
   */
  static void checkIfValidAccountNumber(int accountNumber) {
    if (accountNumber < 1000 || accountNumber > 9999) {
      throw new IllegalArgumentException("Accountnumber must be between 1000 and 9999, but was: " 
          + accountNumber);
    }
  }

  /**
   * Checks if the amount is positive.
   * 
   * @param amount the amount to be checked
   * @throws IllegalArgumentException if the amount is negative
   */
  static void checkIfValidAmount(double amount) {
    if (!isPositiveAmount(amount)) {
      throw new IllegalArgumentException("The amount must be positive, but was: " + amount);
    }
  }

  /**
   * Checks if the amount is positive.
   * 
   * @param amount the amount to check
   * @return {@code true} if the amount is positive
   */
  public static boolean isPositiveAmount(double amount) {
    return amount >= 0;
  }

  /**
   * Checks if the amount added sets the balance in an invalid state (less than 0). Amount can be
   * negative if you are checking a withdrawal.
   * 
   * @param amount the amount to be added
   * @return {@code false} if the balance is set to be negative
   */
  public static boolean isBalanceValidWhenAdding(double amount, AbstractAccount account) {
    double newBalance = account.getBalance() + amount;
    return newBalance >= 0;
  }

  /**
   * Checks if the user already owns a BSU-account.
   * 
   * @param user the user to be checked
   * @return {@code true} if the user owns a BSU-account
   */
  public static boolean hasBSU(User user) {
    return user.getAccounts().stream().anyMatch(account -> account instanceof BSUAccount);
  }

  /**
   * Checks if the name is less than 20 characters long and only consists of letters and spaces.
   * 
   * @param name the name to be checked
   * @return {@code true} if the name satisfies this rule
   */
  public static boolean isValidName(String name) {
    if (name.length() > 20 || !isOnlyLettersAndSpaces(name)) {
      return false;
    }
    return true;
  }

  /**
   * Checks if the given string consists of only letters and spaces.
   * 
   * @param s the string to check
   * @return {@code true} if name consists of only letters and spaces
   */
  public static boolean isOnlyLettersAndSpaces(String s) {
    for (int i = 0; i < s.length(); i++) {
      char ch = s.charAt(i);
      if (Character.isLetter(ch) || ch == ' ') {
        continue;
      }
      return false;
    }
    return true;
  }
}
