package core;


public class Transaction {
    
    public enum TransactionType {DEPOSIT, WITHDRAWAL, TRANSFER}
    
    private final TransactionType type;
    private final String payer;
    private final int payersAccountNumber;
    private final String recipient;
    private final int recipientsAccountNumber;
    private final double amount;

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

    public static void main(String[] args) {
        User payer = new User(180900);
        payer.setName("Payer");
        AbstractAccount account1 = new CheckingAccount("payer", 100, payer);
        User recipient = new User(123456);
        recipient.setName("Recipient");
        AbstractAccount account2 = new CheckingAccount("recipient", 0, recipient);
        account1.transfer(account2, 50);
        Transaction transaction = account1.getTransactionHistory().get(0);
        System.out.println(transaction.toString());
        account1.withdraw(25);
        System.out.println(account1.getTransactionHistory().get(1));
    }

}
