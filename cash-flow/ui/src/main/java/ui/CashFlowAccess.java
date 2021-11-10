package ui;

import core.AbstractAccount;

public interface CashFlowAccess {


    /**
     * Gets account with given account number
     * 
     * @param accountNumber the account's account number
     */
    void getAccount(int accountNumber);

    /**
     * Adds an AbstractAccount to the underlying user.
     * 
     * @param account the account
     */
    void addAccount(AbstractAccount account);


    /**
     * Save account
     */
    void saveAccount();

}