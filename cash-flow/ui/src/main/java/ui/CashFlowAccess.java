package ui;

import core.AbstractAccount;

public interface CashFlowAccess {


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
    public void transfer(AbstractAccount payer, AbstractAccount reciever);
    
    /**
     * Save account
     */
    public void saveAccount();

    

}