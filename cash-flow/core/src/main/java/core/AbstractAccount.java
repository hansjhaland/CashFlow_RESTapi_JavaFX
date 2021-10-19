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

    protected BankHelper helper = new BankHelper();

    //==============================================================================================
    // Constructors
    //==============================================================================================

    public AbstractAccount(String name, int accountNumber, User owner) {
        helper.checkIfValidName(name);
        this.name = name;

        helper.checkIfValidAccountNumber(accountNumber);
        if (owner != null){
            helper.checkIfAccountNumberIsTaken(accountNumber, owner);
        }
        
        this.accountNumber = accountNumber;
        
        setOwner(owner);
    }

    public AbstractAccount(String name, User owner) {
        helper.checkIfValidName(name);
        this.name = name;

        int availableAccountNumber = getNextAvailableAccountNumber(owner);
        if (availableAccountNumber == -1) {
            throw new IllegalStateException("This user has reached the maximum number of accounts");
        }
        accountNumber = availableAccountNumber;
        
        setOwner(owner);
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
        helper.checkIfValidAmount(amount);
        this.balance += amount;
    }

    /**
     * Deposits the given amount in to the account. Also adds this transaction to the transaction history.
     * @param amount the amount to be deposited
     * @throws IllegalArgumentException if the given amount is negative
     */
    public void deposit(double amount) {
        helper.checkIfValidAmount(amount);
        this.balance += amount;
        addToTransactionHistory(new Transaction(null, this, amount));
    }

    /**
     * Withdraws the given amount from the account. Also adds this transaction to the transaction history.
     * @param amount the amount to be withdrawn
     * @throws IllegalArgumentException if the given amount is negative
     * @throws IllegalStateException if the withdrawal of the amount leads to the balance being negative
     */
    public void withdraw(double amount) {
        helper.checkIfValidAmount(amount);
        helper.checkIfValidBalance(-amount, this);
        this.balance -= amount;
        addToTransactionHistory(new Transaction(this, null, amount));
    }

    /**
     * This method is only to be used when another account is transfering money to this account
     * and the money transferred neads to be added to the balance
     * @param amount amount to be recieved
     */
    private void recieveFromOtherAccount(double amount) {
        helper.checkIfValidAmount(amount);
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
        helper.checkIfValidAmount(amount);
        helper.checkIfValidBalance(-amount, this);
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
        return 1000; //if the user is null return 1000 
    }

    public boolean addToTransactionHistory(Transaction transaction) {
        if (transaction != null) {
            return (transactionHistory.add(transaction));
        }
        return false;
        
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
        helper.checkIfValidName(name);
        this.name = name;
    }

    public void setOwner(User owner) {
        if (owner != null) {
            owner.addAccount(this);
            User ownerCopy = new User(owner.getUserID());
            ownerCopy.setName(owner.getName());
            this.owner = ownerCopy;
        }
    }

    @Override
    public String toString() {
        return "Name: " + getName() +
             "\nAccount number: " + getAccountNumber() + 
             "\nOwner (ID): " + owner.getName() + " (" + owner.getUserID() + ")" + 
             "\nBalance: " + getBalance();
    }
}
