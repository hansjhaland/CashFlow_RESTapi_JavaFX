package core;

public class CheckingAccount extends AbstractAccount {
    
    /**
     * Initializes a new CheckingAccount-object.
     * @param name the name of the account
     * @param amount the initial balance of the account
     * @param accountNumber the account number of the account
     * @param owner the owner of the account
     * @throws IllegalArgumentException if the name is more than 20 characters long
     * @throws IllegalArgumentException if the account number is not between 1000 and 9999 
     * @throws IllegalArgumentException if the account number already exists in the users list of account numbers
     * @throws IllegalStateException if the initial balance is set to be less than 0
     */
    public CheckingAccount(String name, double amount, int accountNumber, User owner) {
        super(name, accountNumber, owner);
        initialDeposit(amount);
    }

    public CheckingAccount(String name, double amount, User owner) {
        super(name, owner);
        initialDeposit(amount);
    }


    
    /**
     * Deposits the given amount in to the account.
     * @param amount the amount to be deposited
     * @throws IllegalArgumentException if the given amount is negative
     */
    @Override
    public boolean deposit(double amount) {
        return super.deposit(amount);
    }
    
    /**
     * Withdraws the given amount from the account.
     * @param amount the amount to be withdrawn
     * @throws IllegalArgumentException if the given amount is negative
     * @throws IllegalStateException if the withdrawal of the amount leads to the balance being negative
     */
    @Override
    public boolean withdraw(double amount) {
        return super.withdraw(amount);
        
    }
}
