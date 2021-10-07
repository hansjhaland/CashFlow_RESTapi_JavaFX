package core;

public class BSUAccount extends AbstractAccount {

    /**
     * Initializes a new BSUAccount-object.
     * @param name the name of the account
     * @param amount the initial balance of the account
     * @param accountNumber the account number of the account
     * @param owner the owner of the account
     * @throws IllegalArgumentException if the name is more than 20 characters long
     * @throws IllegalArgumentException if the account number is not between 1000 and 9999 
     * @throws IllegalArgumentException if the account number already exists in the users list of account numbers
     * @throws IllegalStateException if the initial balance is set to be less than 0
     * @throws IllegalStateException if the initial balance is set to be more than 25000
     */
    public BSUAccount(String name, double balance, int accountNumber, User owner) {
        super(name, accountNumber, owner);
        checkIfValidDeposit(balance);
        initialDeposit(balance);
    }
    
    /**
     * Deposits the given amount in to the account.
     * @param amount the amount to be deposited
     * @throws IllegalStateException if the deposit leads to the balance exceeding the cap of 25000
     */
    @Override
    public boolean deposit(double amount) {
        checkIfValidDeposit(amount);
        return super.deposit(amount);
    }

    /**
     * This method does nothing, because one cannot withdraw from a BSU account,
     * unless its used as deductable for a mortgage
     */
    @Override
    public boolean withdraw(double amount) {
        return false;
    }

    private void checkIfValidDeposit(double amount) {
        double newBalance = amount + getBalance();
        if (newBalance > 25000) {
            throw new IllegalStateException("New balance must be 25000 or below, but was: " + newBalance);
        }
    }
    
}
