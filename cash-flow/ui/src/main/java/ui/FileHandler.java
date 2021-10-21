package ui;

import java.io.IOException;

import core.User;
import json.CashFlowPersistence;

public class FileHandler {

    private CashFlowPersistence cfp = new CashFlowPersistence();
    
    public void save(User user) throws IllegalStateException, IOException { 
        cfp.saveUser(user);
    }

    public void save(User user, String saveFile) throws IllegalStateException, IOException {
        cfp.setSaveFilePath(saveFile);
        cfp.saveUser(user);
    }

    public User load() throws IllegalStateException, IOException {
        return cfp.loadUser();
    }

    public User load(String saveFilePath) throws IllegalStateException, IOException {
        cfp.setSaveFilePath(saveFilePath);
        return cfp.loadUser();
    }

    public void setSaveFilePath(String saveFile) {
        cfp.setSaveFilePath(saveFile);
    }

}
