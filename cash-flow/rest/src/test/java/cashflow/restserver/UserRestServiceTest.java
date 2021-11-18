package cashflow.restserver;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.Iterator;
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

public class UserRestServiceTest extends JerseyTest{

    /**
     * ??
     * @return
     */
    protected boolean shouldLog() {
        return true;
      }
  
      /**
       * ???
       */
      /*
    @Override
    protected ResourceConfig configure() {
        final TodoConfig config = new TodoConfig();
        if (shouldLog()) {
          enable(TestProperties.LOG_TRAFFIC);
          enable(TestProperties.DUMP_ENTITY);
          config.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_SERVER, "WARNING");
        }
        return config;
    }
    */

    private ObjectMapper objectMapper;

    /**
     * Denne er jeg også usikker på helt ærlig
     */
    /*
    @BeforeEach
    @Override
    public void setUp() throws Exception {
      super.setUp();
      objectMapper = new TodoModuleObjectMapperProvider().getContext(getClass());
    }
*/
    /**
     * denne er jeg usikker på helt ærlig
     */
    /*
    @AfterEach
    @Override
    public void tearDown() throws Exception {
      super.tearDown();
    }
*/
    /**
     * denne har jeg såvidt endret og er litt uskker på hva jeg skal gjøre noe med
     */
    /*
    @Test
    public void testGet_todo() {
      Response getResponse = target(UserRestService.USER_REST_SERVICE_PATH)
          .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8")
          .get();
      assertEquals(200, getResponse.getStatus());
      try {
        TodoModel todoModel = objectMapper.readValue(getResponse.readEntity(String.class), TodoModel.class);
        Iterator<AbstractTodoList> it = todoModel.iterator();
        assertTrue(it.hasNext());
        AbstractTodoList todoList1 = it.next();
        assertTrue(it.hasNext());
        AbstractTodoList todoList2 = it.next();
        assertFalse(it.hasNext());
        assertEquals("todo1", todoList1.getName());
        assertEquals("todo2", todoList2.getName());
      } catch (JsonProcessingException e) {
        fail(e.getMessage());
      }
    }
    */
    /**
     * Henter en bestemt konto basert på kontonr og sjekker at vi får svar som forventet
     */
    @Test
    public void testGetAccount_5555() {
      Response getResponse = target(UserRestService.USER_REST_SERVICE_PATH)
          .path("5555")//??
          .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8")
          .get();
      assertEquals(200, getResponse.getStatus());
      try {
          //riktig med AbstractAccount??
        AbstractAccount account = objectMapper.readValue(getResponse.readEntity(String.class), AbstractAccount.class);

        assertEquals(5555, account.getAccountNumber());
        //har vi noe JsonProcessingException
      } catch (JsonProcessingException e) {
        fail(e.getMessage());
      }
    }

}
