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


/**
 * Top-level REST-service which represents the user (the one that is currently running
 * the app).
 */
@Path(UserRestService.USER_REST_SERVICE_PATH)
@Produces(MediaType.APPLICATION_JSON)
public class UserRestService {
    
    public static final String USER_REST_SERVICE_PATH = "user";

    private static final Logger LOG = LoggerFactory.getLogger(UserRestService.class);

    @Context
    public User user;

    /**
     * Method for getting the user, indicating
     * that the method responds to HTTP-GET request.
     * @return the user
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(){
        LOG.debug("getUser: " + user.getUserID());
        return user;
    }

    /**
     * Method that delegates the methods regarding the the users accounts to an
     * AccountResource-object.
     * @param accountNumber the account number of the account
     * @return the AccountResource-object
     */
    @Path("{accountNumber}")
    public AccountResource getAccount(@PathParam("accountNumber") String accountNumber){
        //Account trenger ikke å eksistere.
        AbstractAccount account = getUser().getAccount(Integer.valueOf(accountNumber));
        AccountResource accountResource = new AccountResource(user, accountNumber, account);
        return accountResource;
    }
}

