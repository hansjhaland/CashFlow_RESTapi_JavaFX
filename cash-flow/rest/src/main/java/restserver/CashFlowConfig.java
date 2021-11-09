package restserver;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import core.User;
import json.CashFlowPersistence;
import core.AbstractAccount;
import core.CheckingAccount;
import core.SavingsAccount;
import restapi.UserRestService;

public class CashFlowConfig extends ResourceConfig{
    
    private User user;
    private CashFlowPersistence cfp;

    public CashFlowConfig(User user) {
        setUser(user);
        cfp = new CashFlowPersistence();
        register(UserRestService.class);
        register(UserObjectMapperProvider.class);
        register(JacksonFeature.class);
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(CashFlowConfig.this.user);
                bind(CashFlowConfig.this.cfp);
            }
        });
    }

    public CashFlowConfig() {
        createDefaultUser();
    }

    public User getUser(){
        return user;
    }

    private void setUser(User user) {
        this.user = user;
    }

    public void createDefaultUser() {
        User defaultUser = new User(888888888);
        AbstractAccount account1 = new CheckingAccount("Checking", 100, 2345, defaultUser);
        AbstractAccount account2 = new SavingsAccount("Savings", 100, 5432, defaultUser);
        setUser(defaultUser);
        cfp = new CashFlowPersistence();
    }

}
