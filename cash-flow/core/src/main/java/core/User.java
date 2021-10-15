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
     * Initializes a new User-object. UserID must be between 100000 and 999999.
     * @param UserID the users identification number
     * @throws IllegalArgumentException if the UserID si not between 100000 and 999999
     */
    public User(int userID) {
        checkIfValidUserID(userID);
        this.userID = userID;
    }
    

    //Er mulig man ikke trenger denne konstruktøren!
    /**
     * Initializes a new User-object. UserID must be between 100000 and 999999.
     * Also adds the given accounts to the users list of accounts
     * @param UserID the users identification number
     * @param accounts the accounts to be added, given as a vararg
     * @throws IllegalArgumentException if the UserID is not between 100000 and 999999
     */
    public User(int userID, AbstractAccount... accounts) {
        checkIfValidUserID(userID);
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
        if (account.getOwnerID() != -1) { //this means that some other user already owns this account
            account.removeOwnersOwnershipOfAccount();
        }
        accounts.add(account);
        account.setOwner(this);
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
     * Checks if the UserID is between 100000 and 999999.
     * @param userID the UserID to be checked
     * @throws IllegalArguementException if the UserID isn't between 100000 and 999999
     */
    private void checkIfValidUserID(int userID) {
        int numberOfDigits = (int)Math.log10(userID)+1;
        if (numberOfDigits != 6) {
            throw new IllegalArgumentException("UserID must be between 100000 and 999999, but had: " + numberOfDigits + " digits.");
        }
    }

    protected void checkIfAccountNumberIsTaken(int accountNumber) {
        for (int exisitingAccountNumber : getAccountNumbers()) {
            if (exisitingAccountNumber == accountNumber) {
                throw new IllegalStateException("The user already has an account with account number: " + accountNumber);
            }
        }
    }

    /**
     * Checks if the name is less than 20 characters long and only consists of letters and spaces.
     * @param name the name to be checked
     * @return {@code true} if the name satisfies this rule
     */
    public static boolean isValidName(String name) {
        if (name.length() > 20 || !isOnlyLettersAndSpaces(name)) {
            return false;
        }
        return true;
    }


    private static boolean isOnlyLettersAndSpaces(String s) {
        for(int i = 0; i < s.length(); i++){
          char ch = s.charAt(i);
          if (Character.isLetter(ch) || ch == ' ') {
            continue;
          }
          return false;
        }
        return true;
    }

    public boolean hasBSU() {
        return getAccounts().stream().anyMatch(account -> account instanceof BSUAccount);
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
     * Returns the account with the coresponding account number, if the user has an account
     * with this account number. If not, it returns {@code null}.
     * @param accountNumber the account number of the account you wish to find
     * @return the account with the account number, or {@code null} if the 
     * account number doesn't excist
     */
    public AbstractAccount getAccount(int accountNumber) {
        AbstractAccount account = getAccounts()
                                  .stream()
                                  .filter(existingAccount -> existingAccount.getAccountNumber() == accountNumber)
                                  .findAny().orElse(null);            
        return account;
    }
    
    /**
     * Changes the name of the user. Name must be 20 characters or less, and can only consist
     * of letters and spaces.
     * @param name the name you wish to change to
     * @throws IllegalArgumentException if the name is more than 20 characters long, or does not
     * only contain letters and spaces
     */
    public void setName(String name) {
        if (!isValidName(name)) {
            throw new IllegalArgumentException("The name " + name.length() + " must be 20 characters or less and only contain letters and spaces");
        }
        this.name = name;
    }



    public static void main(String[] args) {
        
        
    }
}
