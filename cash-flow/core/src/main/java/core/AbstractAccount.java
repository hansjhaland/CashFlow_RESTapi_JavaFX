package core;

import java.util.ArrayList;
import java.util.List;

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

        int availableAccountNumber = getNextAvailableAccountNumber(owner);
        if (availableAccountNumber == -1) {
            throw new IllegalStateException("This user has reached the maximum number of account");
        }
        accountNumber = availableAccountNumber;
        
    }

    

    //==============================================================================================
    // Functional methods
    //==============================================================================================

    protected void initialDeposit(double amount) {
        checkIfValidAmount(amount);
        this.balance += amount;
    }

    public boolean deposit(double amount) {
        checkIfValidAmount(amount);
        this.balance += amount;
        addToTransactionHistory(new Transaction(null, this, amount));
        return true;
    }

    public boolean withdraw(double amount) {
        checkIfValidAmount(amount);
        checkIfValidBalance(-amount);
        this.balance -= amount;
        addToTransactionHistory(new Transaction(this, null, amount));
        return true;
    }

    private void recieveFromOtherAccount(double amount) {
        checkIfValidAmount(amount);
        balance += amount;
    }

    public void transfer(AbstractAccount recievingAccount, double amount) {
        checkIfValidAmount(amount);
        checkIfValidBalance(-amount);
        if (recievingAccount == null) {
            throw new IllegalArgumentException("The parameter 'recievingAccount' cannot be 'null'");
        }
        if (recievingAccount == this) {
            throw new IllegalArgumentException("The parameter 'recievingAccount' cannot be itself");
        }
        balance -= amount;
        recievingAccount.recieveFromOtherAccount(amount);
        Transaction transfer = new Transaction(this, recievingAccount, amount);
        addToTransactionHistory(transfer);
        recievingAccount.addToTransactionHistory(transfer);
    }

    private int getNextAvailableAccountNumber(User user) {
        if (user != null) {
            for (int number = 1000; number < 10000; number++) {
                if (!user.getAccountNumbers().contains(number)) {
                    return number;
                }
            }
        }
        return -1;
    }

    private boolean addToTransactionHistory(Transaction transaction) {
        if (transactionHistory.add(transaction)) {
            return true;
        }
        return false;
    }
    
    //==============================================================================================
    // Methods to check arguments
    //==============================================================================================

    /**
     * Checks if the name is 20 characters or less. Checks if the name is only letters and spaces.
     * @param name the name to be checked
     * @throws IllegalArgumentException if the name is more than 20 characters long
     * @throws IllegalArgumentException if the name contains characters other than letters and spaces
     */
    public static void checkIfValidName(String name) {
        if (name.length() > 20) {
            throw new IllegalArgumentException("The name of the account must be 20 characters or less, but was: " + name.length());
        }
        if (!User.onlyLettersAndSpaces(name)){
            throw new IllegalArgumentException("The name '" + name + "' can only consist of letters and spaces");
        }
    }
    
    /**
     * Checks if the amount added sets the balance in an invalid state (less than 0). Amount can be negative if you are checking a withdrawal.
     * @param amount the amount to be added to the balance
     * @throws IllegalStateException if the balance is less than 0 when the amount is added
     */
    public void checkIfValidBalance(double amount) {
        double newBalance = balance + amount;
        if (newBalance < 0) {
            throw new IllegalStateException("The balance of the account cannot be negative, but was: " + newBalance);
        }
    }

    public void checkIfValidAccountNumber(int accountNumber) {
        if (owner != null){
            if (accountNumber < 1000 || accountNumber > 9999) {
                throw new IllegalArgumentException("Accountnumber must be between 1000 and 9999, but was: " + accountNumber);
            }
        }
    }
    
    /**
     * Checks if the amount is positive.
     * @param amount the amount to be checked
     * @throws IllegalArgumentException if the amount is negative
     */
    public void checkIfValidAmount(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("The amount must be positive, but was: " + amount);
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

    public void setName(String name) {
        checkIfValidName(name);
        this.name = name;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void removeOwnersOwnershipOfAccount() {
        owner.removeAccount(this);
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
