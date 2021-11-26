package restserver;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import restapi.UserRestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import core.AbstractAccount;
import core.CheckingAccount;
import core.SavingsAccount;
import core.User;

/**
 * A class that tests the users data with Rest Api.
 */
public class UserRestServiceTest extends JerseyTest{
  
    /**
     * Tests if the user 
     */
    @Override
    protected ResourceConfig configure() {
        final CashFlowConfig config = new CashFlowConfig();
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        config.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_SERVER, "WARNING");
        return config;
    }
   

    private ObjectMapper objectMapper;

    /**
     * The method creats a test-container.
     */
    @BeforeEach
    @Override
    public void setUp() throws Exception {
      super.setUp();
      objectMapper = new UserObjectMapperProvider().getContext(getClass());
    }

    /**
     * The method removes the test-container.
     */
    @AfterEach
    @Override
    public void tearDown() throws Exception {
      super.tearDown();
    }

    /**
     * Uses Rest Api to get account and checks if the user of the account has the correct data to that account.
     */
    @Test
    public void testGet_user() {
      Response getResponse = target(UserRestService.USER_REST_SERVICE_PATH)
          .request(MediaType.APPLICATION_JSON)
          .get();
      assertEquals(200, getResponse.getStatus());
      try {
        User user = objectMapper.readValue(getResponse.readEntity(String.class), User.class);
        assertEquals(654321, user.getUserId());
        assertEquals(2, user.getAccounts().size());
        assertTrue(user.getAccount(2345) instanceof CheckingAccount);
        assertTrue(user.getAccount(5432) instanceof SavingsAccount);
      } catch (JsonProcessingException e) {
        fail(e.getMessage());
      }
    }
   
    /**
     * The method gets an account based on the accountnumber and checks if the answere is as 
     * expected.
     */
    @Test
    public void testGetAccount_2345() {
      Response getResponse = target(UserRestService.USER_REST_SERVICE_PATH)
          .path("/2345")
          .request(MediaType.APPLICATION_JSON)
          .get();
      assertEquals(200, getResponse.getStatus());
      try {
        AbstractAccount account = objectMapper.readValue(getResponse.readEntity(String.class), AbstractAccount.class);

        assertEquals(2345, account.getAccountNumber());
      } catch (JsonProcessingException e) {
        fail(e.getMessage());
      }
    }

}
