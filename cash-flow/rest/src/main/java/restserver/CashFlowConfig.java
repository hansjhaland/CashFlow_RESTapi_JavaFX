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
        this(createDefaultUser());
    }

    public User getUser(){
        return user;
    }

    private void setUser(User user) {
        this.user = user;
    }

    private static User createDefaultUser() {
        User defaultUser = new User(654321);
        new CheckingAccount("Checking", 100, 2345, defaultUser);
        new SavingsAccount("Savings", 100, 5432, defaultUser);
        return defaultUser;
    }

}
