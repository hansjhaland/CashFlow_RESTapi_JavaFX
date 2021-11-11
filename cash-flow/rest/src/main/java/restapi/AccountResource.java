package restapi;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import json.CashFlowPersistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import core.User;
import core.AbstractAccount;

@Produces(MediaType.APPLICATION_JSON)
public class AccountResource {
    
    private static final Logger LOG = LoggerFactory.getLogger(UserRestService.class);

    private final User user;
    private final String accountNumber;
    private final AbstractAccount account;

    @Context
    private CashFlowPersistence cfp;

    public AccountResource(User user, String accountNumber, AbstractAccount account){
        this.user = user;
        this.accountNumber = accountNumber;
        this.account = account;
    }

    private void checkIfAccountExists(){
        if (account == null){
            throw new IllegalStateException("The account " + account + " does not exist");
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public AbstractAccount getAccount() {
        checkIfAccountExists();
        LOG.debug("getAccount()", accountNumber);
        return this.account;
    }

    //?????
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean putAccount(AbstractAccount account){
        LOG.debug("putAccount(): ", account.toString());
        return user.addAccount(account);
    } 

    @DELETE
    public boolean removeAccount(){
        checkIfAccountExists();
        this.user.removeAccount(this.account);
        return true; 
    }
}
