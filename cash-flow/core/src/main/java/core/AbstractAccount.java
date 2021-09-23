package core;

public abstract class AbstractAccount {

    private String name;
    private double balance;
    private int accountNumber;
    private User owner;

    //==============================================================================================
    // Constructors
    //==============================================================================================

    public AbstractAccount(String name, int accountNumber, User owner) {
        checkIfValidName(name);
        this.name = name;

        this.owner = owner;

        checkIfValidAccountNumber(accountNumber);
        this.accountNumber = accountNumber;

        owner.addAccount(this);
    }

    public AbstractAccount(){};

    //==============================================================================================
    // Functional methods
    //==============================================================================================

    public void deposit(double amount) {
        checkIfValidAmount(amount);
        this.balance += amount;
    }

    public void withdraw(double amount) {
        checkIfValidAmount(amount);
        checkIfValidBalance(-amount);
        this.balance -= amount;
    }
    
    //==============================================================================================
    // Methods to check arguments
    //==============================================================================================

    /**
     * Checks if the name is 20 characters or less.
     * @param name the name to be checked
     * @throws IllegalArgumentException if the name is more than 20 characters long
     */
    private void checkIfValidName(String name) {
        if (name.length() > 20) {
            throw new IllegalArgumentException("The name of the account must be 20 characters or less, but was: " + name.length());
        }
    }
    
    /**
     * Checks if the amount added sets the balance in an invalid state (less than 0). Amount can be negative if you are checking a withdrawal.
     * @param amount the amount to be added to the balance
     * @throws IllegalStateException if the balance is less than 0 when the amount is added
     */
    private void checkIfValidBalance(double amount) {
        double newBalance = balance + amount;
        if (newBalance < 0) {
            throw new IllegalStateException("The balance of the account must be positive, but was: " + newBalance);
        }
    }

    private void checkIfValidAccountNumber(int accountNumber) {
        if (accountNumber < 1000 || accountNumber > 9999) {
            throw new IllegalArgumentException("Accountnumber must be between 1000 and 9999, but was: " + accountNumber);
        }
        for (int exisitingAccountNumber : owner.getAccountNumbers()) {
            if (exisitingAccountNumber == accountNumber) {
                throw new IllegalArgumentException("The user already has an account with account number: " + accountNumber);
            }
        }
    }
    
    /**
     * Checks if the amount is positive.
     * @param amount the amount to be checked
     * @throws IllegalArgumentException if the amount is negative
     */
    private void checkIfValidAmount(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("The amount must be positive, but was: " + amount);
        }
    }

    private void checkOwnerNotNull(User owner){
        if (owner == null) {
            throw new IllegalArgumentException("Cannot set owner to null");
        }
    }

    //==============================================================================================
    // Getters and setters
    //==============================================================================================
    
    public double getBalance() {
        return balance;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getName() {
        return name;
    }

    public User getOwner(){
        return owner;
    }

    public void setName(String name) {
        checkIfValidName(name);
        this.name = name;
    }

    public void setBalance(double amount) {
        checkIfValidAmount(amount);
        this.balance = amount;
    }

    public void setAccountNumber(int accountNumber) {
        //checkIfValidAccountNumber(accountNumber);
        this.accountNumber = accountNumber;
    }

    public void setOwner(User owner) {
        checkOwnerNotNull(owner);
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Name: " + getName() +
             "\nAccount number: " + getAccountNumber() + 
             "\nOwner (ID): " + owner.getName() + " (" + owner.getUserID() + ")" + 
             "\nBalance: " + getBalance();
    }
}
