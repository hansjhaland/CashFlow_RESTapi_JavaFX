package ui;

import java.io.IOException;

import core.AbstractAccount;
import core.User;
import json.CashFlowPersistence;

public class DirectAccess implements CashFlowAccess {

    private User user;
    private CashFlowPersistence cfp;
    public final static String SAVEFILE = "SaveData.json";

    public DirectAccess(User user) {
        this.user = user;
        this.cfp = new CashFlowPersistence();
        cfp.setSaveFilePath(SAVEFILE);
    }

    public User getUser(){
        return user;
    }

    @Override
    public AbstractAccount getAccount(int accountNumber) {
        return user.getAccount(accountNumber);
    }

    @Override
    public void addAccount(AbstractAccount account) {
        if (user != null) {
            user.addAccount(account);
        }

    }

    @Override
    public void saveUser() throws IllegalStateException, IOException {
        cfp.saveUser(user, SAVEFILE);
    }

    @Override
    public void transfer(AbstractAccount payer, AbstractAccount reciever, double amount) {
        payer.transfer(reciever, amount);
    }

    @Override
    public boolean deleteAccount(int accountNumber) {
        if (user != null) {
            return user.removeAccount(getAccount(accountNumber));
        }
        return false;
    }

    @Override
    public User loadInitialUser() throws IllegalStateException, IOException {
        if (cfp.doesFileExist(SAVEFILE)){
            return cfp.loadUser(SAVEFILE);
        }
        return new User(123456);
    }

}