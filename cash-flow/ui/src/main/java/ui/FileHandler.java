package ui;

import java.io.IOException;

import core.User;
import json.CashFlowPersistence;

public class FileHandler {

    private CashFlowPersistence cfp = new CashFlowPersistence();
    
    public void save(User user) throws IllegalStateException, IOException { 
        cfp.setSaveFilePath("SaveData.json");
        cfp.saveUser(user);
    }

    public User load() throws IllegalStateException, IOException {
        cfp.setSaveFilePath("SaveData.json");
        return cfp.loadUser();
    }

}
