package restapi;

import core.AbstractAccount;
import core.User;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import json.CashFlowPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Sub-resource of UserRestService. Is used to update information regarding the users accounts.
 */

@Produces(MediaType.APPLICATION_JSON)
public class AccountResource {

  private static final Logger LOG = LoggerFactory.getLogger(UserRestService.class);

  private final User user;
  private final String accountNumber;
  private final AbstractAccount account;

  @Context
  private CashFlowPersistence cfp;

  /**
   * Initializes an AccountResource-object which represents an account.
   * 
   * @param user the owner of the account
   * @param accountNumber the accountnumber
   * @param account the account-object
   */
  public AccountResource(User user, String accountNumber, AbstractAccount account) {
    this.user = user;
    this.accountNumber = accountNumber;
    this.account = account;
  }

  /**
   * Checks if the current user-object has the spesified account.
   */
  private void checkIfAccountExists() {
    if (account == null) {
      throw new IllegalStateException("The account " + account + " does not exist");
    }
  }

  /**
   * Method for getting the account, indicating that the method responds to HTTP-GET requests.
   * 
   * @return the account
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public AbstractAccount getAccount() {
    checkIfAccountExists();
    LOG.debug("getAccount(): " + account.getName() + " (" + accountNumber + ")");
    return this.account;
  }

  /**
   * Method for adding an account or updating the account if it already exists, indicating that the
   * method responds to HTTP-PUT requests.
   * 
   * @param account the account to be put
   * @return {@code true} if the account was put
   */
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  public boolean putAccount(AbstractAccount account) {
    LOG.debug("putAccount(): " + account.getName() + " (" + accountNumber + ")");
    return user.addAccount(account);
  }

  /**
   * Method for deleting/removing an account, indicating that the method responds to HTTP-DELETE
   * requests.
   * 
   * @return {@code true} if the account was deleted
   */
  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  public boolean removeAccount() {
    checkIfAccountExists();
    LOG.debug("deleteAccount(): " + account.getName() + " (" + accountNumber + ")");
    return user.removeAccount(this.account);
  }
}
