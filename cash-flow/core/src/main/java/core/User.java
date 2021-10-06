package core;

import java.util.ArrayList;
import java.util.Collection;

public class User {

    private String name;
    private final int userID;
    private Collection<AbstractAccount> accounts = new ArrayList<>();

    //==============================================================================================
    // Contstructors
    //==============================================================================================

    /**
     * Initializes a new User-object. UserID must be excactly 6 digits long (for example 180900).
     * @param UserID the users identification number
     * @throws IllegalArgumentException if the UserID is not excactly 6 digits long
     */
    public User(int userID) {
        CheckIfValidUserID(userID);
        this.userID = userID;
    }
    

    //Er mulig man ikke trenger denne konstruktøren!
    /**
     * Initializes a new User-object. UserID must be excactly 6 digits long (for example 180900).
     * Also adds the given accounts to the users list of accounts
     * @param UserID the users identification number
     * @param accounts the accounts to be added, given as a vararg
     * @throws IllegalArgumentException if the UserID is not excactly 6 digits long
     */
    public User(int userID, AbstractAccount... accounts) {
        CheckIfValidUserID(userID);
        this.userID = userID;
        for (AbstractAccount account : accounts) {
            addAccount(account);
        }
    }

    //==============================================================================================
    // Functional methods
    //==============================================================================================

    /**
     * Adds the account to users list of accounts. Also adds the accounts 
     * accountnumber to the users list of accountnumbers.
     * @param account the account to be added
     * @return {@code true} if the account was added
     */
    public boolean addAccount(AbstractAccount account) {
        if (getAccountNumbers().contains(account.getAccountNumber())) {
            return false;
        }
        accounts.add(account);
        return true;
    }

    /**
     * Removes the given account from the users list of accounts. Also removes
     * the accounts accountnumber from the users list of accountnumbers.
     * @param account the account to be removed
     * @return {@code true} if the account was removed
     */
    public boolean removeAccount(AbstractAccount account) {
        if (accounts.remove(account)) {
            return true;
        }
        return false;
    }

    //==============================================================================================
    // Methods to check arguments
    //==============================================================================================
    
    /**
     * Checks if the UserID is excactly 6 digits long.
     * @param userID the UserID to be checked
     * @throws IllegalArguementException if the UserID isn't excactly 6 digits long
     */
    private void CheckIfValidUserID(int userID) {
        int numberOfDigits = (int)Math.log10(userID)+1;
        if (numberOfDigits != 6) {
            throw new IllegalArgumentException("UserID must be an int with excactly 6 digits, but had: " + numberOfDigits + " digits.");
        }
    }

    //==============================================================================================
    // Getters and setters
    //==============================================================================================

    public String getName() {
        return name;
    }

    public int getUserID() {
        return userID;
    }

    public Collection<AbstractAccount> getAccounts() {
        return new ArrayList<>(accounts);
    }

    public Collection<Integer> getAccountNumbers() {
        Collection<Integer> accountNumbers = new ArrayList<>();
        for (AbstractAccount account : getAccounts()) {
            accountNumbers.add(account.getAccountNumber());
        }
        return accountNumbers;
    }
    
    /**
     * Changes the name of the user. Name must be 20 characters or less.
     * @param name the name you wish to change to
     * @throws IllegalArgumentException if the name is more than 20 characters long
     */
    public void setName(String name) {
        if (name.length() > 20) {
            throw new IllegalArgumentException("The name of the user must be 20 characters or less, but was: " + name.length());
        }
        this.name = name;
    }

    public static void main(String[] args) {
        User test = new User(180900);
        CheckingAccount a1 = new CheckingAccount("Arild", 100, 4353, test);
        test.addAccount(a1);
        CheckingAccount a2 = new CheckingAccount("Arild", 200, 4352, test);
        test.addAccount(a2);
        
    }
}
