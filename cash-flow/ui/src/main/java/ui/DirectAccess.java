package ui;

import java.io.IOException;

import core.AbstractAccount;
import core.User;
import json.CashFlowPersistence;

public class DirectAccess implements CashFlowAccess {

    private User user;
    private CashFlowPersistence cfp;
    public final static String LOCALSAVEFILE = "SaveData.json";
    public final static String TESTSAVEFILE = "SaveDataTest.json";
    private String saveFile;

    public DirectAccess(User user, String saveFile) {
        this.saveFile = saveFile;
        this.user = user;
        this.cfp = new CashFlowPersistence();
        cfp.setSaveFilePath(saveFile);
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
        cfp.saveUser(user, saveFile);
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
        if (cfp.doesFileExist(saveFile)){
            return cfp.loadUser(saveFile);
        }
        return new User(123456);
    }

}