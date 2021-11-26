package restserver;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.Iterator;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import restapi.UserRestService;
import restserver.CashFlowConfig;
import restserver.UserObjectMapperProvider;
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
import core.BsuAccount;
import core.CheckingAccount;
import core.SavingsAccount;
import core.User;

public class UserRestServiceTest extends JerseyTest{
  
      /**
       * ???
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
     * Setter opp test-container
     */
    
    @BeforeEach
    @Override
    public void setUp() throws Exception {
      super.setUp();
      objectMapper = new UserObjectMapperProvider().getContext(getClass());
    }

    /**
     * River ned test-container
     */
    
    @AfterEach
    @Override
    public void tearDown() throws Exception {
      super.tearDown();
    }

    /**
     * denne har jeg såvidt endret og er litt uskker på hva jeg skal gjøre noe med
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
     * Henter en bestemt konto basert på kontonr og sjekker at vi får svar som forventet
     */
    @Test
    public void testGetAccount_2345() {
      Response getResponse = target(UserRestService.USER_REST_SERVICE_PATH)
          .path("/2345")
          .request(MediaType.APPLICATION_JSON)
          .get();
      assertEquals(200, getResponse.getStatus());
      try {
          //riktig med AbstractAccount??
        AbstractAccount account = objectMapper.readValue(getResponse.readEntity(String.class), AbstractAccount.class);

        assertEquals(2345, account.getAccountNumber());
        //har vi noe JsonProcessingException
      } catch (JsonProcessingException e) {
        fail(e.getMessage());
      }
    }

}
