package ui;

import java.io.IOException;

import core.AbstractAccount;
import core.User;

public interface CashFlowAccess {

    /**
     * Gets a user from relevant source.
     * 
     * @return a user 
     */
    public User getUser();

    /**
     * Gets account with given account number
     * 
     * @param accountNumber the account's account number
     */
    public AbstractAccount getAccount(int accountNumber);

    /**
     * Adds an AbstractAccount to the underlying user.
     * 
     * @param account the account
     */
    public void addAccount(AbstractAccount account);

    /**
     * Deletes an account from the underlying user's account list
     * 
     * @param account the account to be deleted
     */
    public void deleteAccount(int accountNumber);

    /**
     * Adds an transaction to both accouts' transaction history
     * 
     * @param payer paying account
     * @param reciever recieving account
     */
    public void transfer(AbstractAccount payer, AbstractAccount reciever, double amount);

    /**
     * Gets a user object from file if it exists.
     * 
     * @throws IllegalStateException
     * @throws IOException
     * @return user from file if it exists. Default user otherwise.
     */
    public User loadInitialUser() throws IllegalStateException, IOException;

    /**
     * Save user to file as JSON object.
     * 
     * @throws IllegalStateException
     * @throws IOException
     */
    public void saveUser() throws IllegalStateException, IOException;

}