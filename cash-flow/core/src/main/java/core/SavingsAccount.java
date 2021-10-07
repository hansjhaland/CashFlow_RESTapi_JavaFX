package core;

import core.Transaction.TransactionType;

public class SavingsAccount extends AbstractAccount {
    
    public SavingsAccount(String name, double balance, int accountNumber, User owner) {
        super(name, accountNumber, owner);
        initialDeposit(balance);
    }

    @Override
    public boolean deposit(double amount) {
        return super.deposit(amount);
        
    }

    @Override
    public boolean withdraw(double amount) {
        checkIfWithdrawalPossible();
        return super.withdraw(amount);
    }
    
    public void checkIfWithdrawalPossible() {
        if (getNumberOfWithdrawals() + getNumberOfTransfersFromThisAccount() > 9) {
            throw new IllegalStateException("Cannot withdraw, because number of withdrawals have been reached (10)");
        }
    }

    private long getNumberOfWithdrawals() {
        long numberOfWithdrawals = getTransactionHistory().stream()
                                                          .filter(transaction -> transaction.getType() == TransactionType.WITHDRAWAL)
                                                          .count();
        return numberOfWithdrawals;
    }

    private long getNumberOfTransfersFromThisAccount() {
        long numberOfTransfers = getTransactionHistory().stream()
                                                        .filter(transaction -> transaction.getType() == TransactionType.TRANSFER)
                                                        .filter(transfer -> transfer.getPayersAccountNumber() == getAccountNumber())
                                                        .count();
        return numberOfTransfers;
    }
}