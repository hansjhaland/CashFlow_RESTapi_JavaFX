package restapi;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import core.User;
import core.AbstractAccount;



@Path(UserRestService.USER_REST_SERVICE_PATH)
@Produces(MediaType.APPLICATION_JSON)
public class UserRestService {
    
    public static final String USER_REST_SERVICE_PATH = "user";

    private static final Logger LOG = LoggerFactory.getLogger(UserRestService.class);

    @Context
    private User user;

    @GET
    public User getUser(){
        LOG.debug("getUser: " + user);
        return user;
    }

    @Path("/{accountNumber}")
    public AccountResource getAccount(@PathParam("accountNumber") String accountNumber){
        //Account trenger ikke å eksistere.
        AbstractAccount account = getUser().getAccount(Integer.valueOf(accountNumber));
        LOG.debug("getAccount: " + account + "with account number: " + accountNumber);
        AccountResource accountResource = new AccountResource(user, accountNumber, account);
        return accountResource;
    }
}

