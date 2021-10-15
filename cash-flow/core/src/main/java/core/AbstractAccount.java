package core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class AbstractAccount {

    private String name;
    private double balance;
    private final int accountNumber;
    private User owner;
    private List<Transaction> transactionHistory = new ArrayList<>();

    //==============================================================================================
    // Constructors
    //==============================================================================================

    public AbstractAccount(String name, int accountNumber, User owner) {
        checkIfValidName(name);
        this.name = name;

        this.owner = owner;

        checkIfValidAccountNumber(accountNumber);
        if (owner != null){
            owner.checkIfAccountNumberIsTaken(accountNumber);
            owner.addAccount(this);
        }
        this.accountNumber = accountNumber;

    }

    public AbstractAccount(String name, User owner) {
        checkIfValidName(name);
        this.name = name;

        this.owner = owner;
        if (owner != null){
            owner.addAccount(this);
        }

        int availableAccountNumber = getNextAvailableAccountNumber(owner);
        if (availableAccountNumber == -1) {
            throw new IllegalStateException("This user has reached the maximum number of accounts");
        }
        accountNumber = availableAccountNumber;
        
    }

    

    //==============================================================================================
    // Functional methods
    //==============================================================================================

    /**
     * This deposit is the initial deposit when the account is being created
     * @param amount the amount to be deposited
     * @throws IllegalArgumentException if the given amount is negative
     */
    protected void initialDeposit(double amount) {
        checkIfValidAmount(amount);
        this.balance += amount;
    }

    /**
     * Deposits the given amount in to the account. Also adds this transaction to the transaction history.
     * @param amount the amount to be deposited
     * @throws IllegalArgumentException if the given amount is negative
     */
    public boolean deposit(double amount) {
        checkIfValidAmount(amount);
        this.balance += amount;
        addToTransactionHistory(new Transaction(null, this, amount));
        return true;
    }

    /**
     * Withdraws the given amount from the account. Also adds this transaction to the transaction history.
     * @param amount the amount to be withdrawn
     * @throws IllegalArgumentException if the given amount is negative
     * @throws IllegalStateException if the withdrawal of the amount leads to the balance being negative
     */
    public boolean withdraw(double amount) {
        checkIfValidAmount(amount);
        checkIfValidBalance(-amount);
        this.balance -= amount;
        addToTransactionHistory(new Transaction(this, null, amount));
        return true;
    }

    /**
     * This method is only to be used when another account is transfering money to this account
     * and the money transferred neads to be added to the balance
     * @param amount amount to be recieved
     */
    private void recieveFromOtherAccount(double amount) {
        checkIfValidAmount(amount);
        balance += amount;
    }

    /**
     * Transfers the spesified amount of money from this account to the recipient account. Also adds
     * this transaction to both the payers and the recipients transaction history
     * @param recievingAccount the account that will recieve the money
     * @param amount the amount of money to be transferred to the recipient account
     * @throws IllegalArgumentException if the amount is negative
     * @throws IllegalArgumentException if 'recievingAccount' is {@code null}
     * @throws IllegalArgumentException if 'recievingAccount' is the account that is also transfering the money
     * @throws IllegalStateException if the balance is less than 0 when the amount is added
     */
    public void transfer(AbstractAccount recievingAccount, double amount) {
        checkIfValidAmount(amount);
        checkIfValidBalance(-amount);
        if (recievingAccount == null) {
            throw new IllegalArgumentException("The parameter 'recievingAccount' cannot be 'null'");
        }
        if (recievingAccount == this) {
            throw new IllegalArgumentException("The parameter 'recievingAccount' cannot be itself");
        }
        if (recievingAccount instanceof BSUAccount && recievingAccount.getBalance() + amount > 25000) {
            throw new IllegalStateException("The BSU account's balance cannot exceed '25000', but was: " + recievingAccount.getBalance() + amount);
        }
        balance -= amount; //deducts the amount from this accounts balance
        recievingAccount.recieveFromOtherAccount(amount); //adds the amount to the recipient accounts balance
        Transaction transfer = new Transaction(this, recievingAccount, amount);
        addToTransactionHistory(transfer);
        recievingAccount.addToTransactionHistory(transfer);
    }

    private int getNextAvailableAccountNumber(User user) {
        if (user != null) { 
            for (int accountNumber = 1000; accountNumber < 10000; accountNumber++) { //looping through the possible account numbers
                if (!user.getAccountNumbers().contains(accountNumber)) { //checking if its taken
                    return accountNumber; 
                }
            }
            return -1; //return -1 if all the account numbers are taken
        }
        return new Random().nextInt(8999) + 1000; //if the user is null, return a random number between 1000 and 9999
    }

    public boolean addToTransactionHistory(Transaction transaction) {
        if (transaction != null) {
            return (transactionHistory.add(transaction));
        }
        return false;
        
    }

    /**Only used when a user is adding this account that already has an owner.
     * This method will then remove this account from the owners list of accounts. 
     */
    protected void removeOwnersOwnershipOfAccount() {
        owner.removeAccount(this);
    }
    
    //==============================================================================================
    // Methods to check arguments
    //==============================================================================================

    /**
     * Checks if the name is 20 characters or less. Checks if the name is only letters and spaces.
     * @param name the name to be checked
     * @throws IllegalArgumentException if the name is more than 20 characters long or does not only contain letters and spaces
     */
    private static void checkIfValidName(String name) {
        if (!User.isValidName(name)) {
            throw new IllegalArgumentException("The name " + name.length() + " must be 20 characters or less and only contain letters and spaces");
        }
    }
    
    /**
     * Checks if the amount added sets the balance in an invalid state (less than 0). Amount can be negative if you are checking a withdrawal.
     * @param amount the amount to be added to the balance
     * @throws IllegalStateException if the balance is less than 0 when the amount is added
     */
    private void checkIfValidBalance(double amount) {
        if (!isBalanceValidWhenAdding(amount)) {
            throw new IllegalStateException("The balance of the account cannot be negative, and adding the amount made it negative");
        }
    }


    private void checkIfValidAccountNumber(int accountNumber) {
        if (accountNumber < 1000 || accountNumber > 9999) {
            throw new IllegalArgumentException("Accountnumber must be between 1000 and 9999, but was: " + accountNumber);
        }
    }
    
    /**
     * Checks if the amount is positive.
     * @param amount the amount to be checked
     * @throws IllegalArgumentException if the amount is negative
     */
    private void checkIfValidAmount(double amount) {
        if (!isPositiveAmount(amount)) {
            throw new IllegalArgumentException("The amount must be positive, but was: " + amount);
        }
    }

    /**
     * Checks if the amount is positive.
     * @param amount the amount to check
     * @return {@code true} if the amount is positive
     */
    public static boolean isPositiveAmount(double amount) {
        return amount >= 0;
    }

    /**
     * Checks if the amount added sets the balance in an invalid state (less than 0). 
     * Amount can be negative if you are checking a withdrawal.
     * @param amount the amount to be added
     * @return {@code false} if the balance is set to be negative
     */
    public boolean isBalanceValidWhenAdding(double amount) {
        double newBalance = balance + amount;
        return newBalance >= 0;
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

    /**
     * Returns the ID number of the owner. Returns {@code -1} if the owner is {@code null}.
     * @return the ID number of the owner
     */
    public int getOwnerID(){
        if (owner == null) {
            return -1;
        }
        return owner.getUserID();
    }

    public List<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }

    /**
     * Changes the name of the account. Name must be 20 characters or less, and can only consist
     * of letters and spaces.
     * @param name the name you wish to change to
     * @throws IllegalArgumentException if the name is more than 20 characters long, or does not
     * only contain letters and spaces
     */
    public void setName(String name) {
        checkIfValidName(name);
        this.name = name;
    }

    //dårlig innkapsling?
    public void setOwner(User owner) {
        this.owner = owner;
        if (owner != null) {
            owner.addAccount(this);
        }
    }

    @Override
    public String toString() {
        return "Name: " + getName() +
             "\nAccount number: " + getAccountNumber() + 
             "\nOwner (ID): " + owner.getName() + " (" + owner.getUserID() + ")" + 
             "\nBalance: " + getBalance();
    }

    public static void main(String[] args) {
        
    }
}
