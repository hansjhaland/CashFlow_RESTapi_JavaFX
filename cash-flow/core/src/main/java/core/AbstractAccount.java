package core;

public abstract class AbstractAccount {

    String name;
    double balance;
    int accountNumber;

    public AbstractAccount(String name, int accountNumber) {
        checkName(name);
        this.name = name;

        if (accountNumber < 1000 || accountNumber > 9999) {
            throw new IllegalArgumentException("Accountnumber must be between 1000 and 9999, but was: " + accountNumber);
        }
        //Sjekke om en konto med likt kontonummer eksisterer
        this.accountNumber = accountNumber;

    }

    //Functional methods:
    public abstract void deposit(int amount);

    public abstract void withdraw(int amount);

    public abstract void transfer(int amount);

    // Getters and setters:
    public double getBalance() {
        return balance;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        checkName(name);
        this.name = name;
    }
    
    // Methods to check arguments:
    private void checkName(String name) {
        if (name.length() > 19 || name.isBlank()) {
            throw new IllegalArgumentException("The name of the account must be less than 20 characters, but was: " + name.length());
        }
    }


    
}
