package core;

/**
 * A class that holds all the transaction methods based on which type of account it is.
 */
public class Transaction {

  /**
   * An enum for spesifying which type of transaction you want.
   */

  public enum TransactionType {
    DEPOSIT, WITHDRAWAL, TRANSFER
  }

  private final TransactionType type;
  private final String payer;
  private final int payersAccountNumber;
  private final String recipient;
  private final int recipientsAccountNumber;
  private final double amount;

  /**
   * Initializes a new Transaction-object. The Transaction-object is an object which is initialized
   * each time there's a transfer, deposit or a withdrawal. It keeps track of: the paying account,
   * the recipient account, the type of transfer and the amount money transferred.
   *
   * @param payerAccount the paying account
   * @param recipientAccount the recieving account
   * @param amount the amount to be transfered
   */
  public Transaction(AbstractAccount payerAccount, 
      AbstractAccount recipientAccount, double amount) {
    if (payerAccount == null && recipientAccount == null) {
      throw new IllegalArgumentException("A transaction must have either 'payer' or a 'recipient'," 
          + " but both were null");
    }
    if (amount <= 0) {
      throw new IllegalArgumentException("The amount must be larger than 0, but was: " 
          + amount);
    }
    if (payerAccount == null) {
      type = TransactionType.DEPOSIT;
      this.payer = "";
      this.payersAccountNumber = 0;
      this.recipient = recipientAccount.getName();
      this.recipientsAccountNumber = recipientAccount.getAccountNumber();
    } else if (recipientAccount == null) {
      type = TransactionType.WITHDRAWAL;
      this.payer = payerAccount.getName();
      this.payersAccountNumber = payerAccount.getAccountNumber();
      this.recipient = "";
      this.recipientsAccountNumber = 0;
    } else {
      type = TransactionType.TRANSFER;
      this.payer = payerAccount.getName();
      this.payersAccountNumber = payerAccount.getAccountNumber();
      this.recipient = recipientAccount.getName();
      this.recipientsAccountNumber = recipientAccount.getAccountNumber();
    }
    this.amount = amount;
  }

  /**
   * A method for creating an transaction object.
   *
   * @param payer user that pays.
   * @param payersAccountNumber accountnumber of the payer.
   * @param recipient the reciver of the transaction.
   * @param recipientsAccountNumber accountnumber of the reciver.
   * @param amount that is being transferd.
   * @throws IllegalArgumentException if there is no payer or recipent.
   */
  public Transaction(String payer, int payersAccountNumber, String recipient, 
      int recipientsAccountNumber, double amount) {
    if (payer.equals("") && recipient.equals("")) {
      throw new IllegalArgumentException("A transaction must have either 'payer' or a" 
          + " 'recipient', but both were null");
    }
    if (payer.equals("")) {
      type = TransactionType.DEPOSIT;
    } else if (recipient.equals("")) {
      type = TransactionType.WITHDRAWAL;
    } else {
      type = TransactionType.TRANSFER;
    }
    this.payer = payer;
    this.payersAccountNumber = payersAccountNumber;
    this.recipient = recipient;
    this.recipientsAccountNumber = recipientsAccountNumber;
    this.amount = amount;
  }

  public TransactionType getType() {
    return type;
  }

  public String getPayer() {
    return payer;
  }

  public String getRecipient() {
    return recipient;
  }

  public int getPayersAccountNumber() {
    return payersAccountNumber;
  }

  public int getRecipientsAccountNumber() {
    return recipientsAccountNumber;
  }

  public double getAmount() {
    return amount;
  }

  /**
   * Tostring for the transaction.
   */
  @Override
  public String toString() {
    return "type: " + type + "\npayer: " + payer + "\npayersAccountNumber: "
        + payersAccountNumber + "\nrecipient: " + recipient + "\nrecipientsAccountNumber: "
        + recipientsAccountNumber + "\namount: " + amount;
  }
}
