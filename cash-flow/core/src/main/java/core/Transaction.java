package core;

public class Transaction {
    
    public enum TransactionType {DEPOSIT, WITHDRAWAL, TRANSFER}
    
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
     * @param payerAccount
     * @param recipientAccount
     * @param amount
     */
    public Transaction(AbstractAccount payerAccount, AbstractAccount recipientAccount, double amount) {
        if (payerAccount == null && recipientAccount == null) {
            throw new IllegalArgumentException("A transaction must have either 'payer' or a 'recipient', but both were null");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("The amount must be larger than 0, but was: " + amount);
        }
        if (payerAccount == null) {
            type = TransactionType.DEPOSIT;
            this.payer = "";
            this.payersAccountNumber = 0;
            this.recipient = recipientAccount.getName();
            this.recipientsAccountNumber = recipientAccount.getAccountNumber();
        }
        else if (recipientAccount == null) {
            type = TransactionType.WITHDRAWAL;
            this.payer = payerAccount.getName();
            this.payersAccountNumber = payerAccount.getAccountNumber();
            this.recipient = "";
            this.recipientsAccountNumber = 0;
        }
        else {
            type = TransactionType.TRANSFER;
            this.payer = payerAccount.getName();
            this.payersAccountNumber = payerAccount.getAccountNumber();
            this.recipient = recipientAccount.getName();
            this.recipientsAccountNumber = recipientAccount.getAccountNumber();
        }
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

    @Override
    public String toString() {
        return "type: " + type + 
               "\npayer: " + payer +
               "\npayersAccountNumber: " + payersAccountNumber +
               "\nrecipient: " + recipient +
               "\nrecipientsAccountNumber: " + recipientsAccountNumber +
               "\namount: " + amount;
    }
}