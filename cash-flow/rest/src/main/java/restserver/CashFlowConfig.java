package restserver;

import core.CheckingAccount;
import core.SavingsAccount;
import core.User;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import restapi.UserRestService;

/**
 * Configures the REST-service, saying it should use JSON with Jackson library, inject a User-object
 * using a binder, etc.
 */
public class CashFlowConfig extends ResourceConfig {

  private User user;

  /**
   * Initializes the CashFlowConfig with the given user.
   * 
   * @param user the user that is going to get injected
   */
  public CashFlowConfig(User user) {
    setUser(user);
    register(UserRestService.class);
    register(UserObjectMapperProvider.class);
    register(JacksonFeature.class);
    register(new AbstractBinder() {
      @Override
      protected void configure() {
        bind(CashFlowConfig.this.user);
      }
    });
  }

  /**
   * Initializes the CashFlowConfig with a default User-object each time the server is started.
   */
  public CashFlowConfig() {
    this(createDefaultUser());
  }

  public User getUser() {
    return user;
  }

  private void setUser(User user) {
    this.user = user;
  }

  /**
   * Creates a default User-object.
   * 
   * @return the default user
   */
  private static User createDefaultUser() {
    User defaultUser = new User(654321);
    new CheckingAccount("Checking", 100, 2345, defaultUser);
    new SavingsAccount("Savings", 100, 5432, defaultUser);
    return defaultUser;
  }

}
