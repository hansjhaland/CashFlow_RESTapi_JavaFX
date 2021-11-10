package ui;

import core.AbstractAccount;

public interface DetailsAccess {
    
    /**
     * Gets account with given account number
     * 
     * @param accountNumber the account's account number
     */
    void getAccount(int accountNumber);

    /**
     * Adds an transaction to both accouts' transaction history
     * 
     * @param payer paying account
     * @param reciever recieving account
     */
    void transfer(AbstractAccount payer, AbstractAccount reciever);

    /**
     * Deletes an account from the underlying user's account list
     * 
     * @param account the account to be deleted
     */
    void deleteAccount(AbstractAccount account);

}
