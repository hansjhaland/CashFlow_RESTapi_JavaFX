package json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Iterator;

import core.AbstractAccount;
import core.CheckingAccount;
import core.User;

public class CashFlowModuleTest {

    private static ObjectMapper mapper;

    @BeforeAll
    public static void setUp() {
        mapper = new ObjectMapper();
        mapper.registerModule(new CashFlowModule());
    }

    private final static String userWithOneAccount  = "{\"name\":\"name1\",\"userID\":123456,\"accounts\":[{\"name\":\"ac1\",\"balance\":200.0,\"accountNumber\":5555}]}";
    private final static String userWithTwoAccounts = "{\"name\":\"name2\",\"userID\":654321,\"accounts\":[{\"name\":\"ac1\",\"balance\":200.0,\"accountNumber\":5555},{\"name\":\"ac2\",\"balance\":100.0,\"accountNumber\":1234}]}";

    @Test
    public void testCheckingAccountAndUserSerializers(){
        User user = new User(123456);
        user.setName("name1");
        CheckingAccount account = new CheckingAccount("ac1", 200, 5555, user);
        try{
            assertEquals(userWithOneAccount, mapper.writeValueAsString(user), 
                "Incorrect serialization of User and/or CheckingAccount!");
        } catch (JsonProcessingException e){
            fail();
        }
    }

    private void checkCheckingAccount(AbstractAccount account, String name, double balance, int accountNumber) {
        assertEquals(name, account.getName(), "Incorrect account name for account: " + account);
        assertTrue(balance == account.getBalance(), "Incorrect balance for account: " + account);
        assertTrue(accountNumber == account.getAccountNumber(), "Incorrect account number for account: " + account);
    }

    private void checkCheckingAccount(AbstractAccount account1, AbstractAccount account2) {
        checkCheckingAccount(account1, account2.getName(), account2.getBalance(), account2.getAccountNumber());
    }

    @Test
    public void testCheckingAccountAndUserDeserializers(){
        try {
            User user = mapper.readValue(userWithTwoAccounts, User.class);
            assertEquals("name2", user.getName());
            assertTrue(654321 == user.getUserID());
            Iterator<AbstractAccount> it = user.getAccounts().iterator();
            AbstractAccount account = null;
            assertTrue(it.hasNext());
            account = it.next();
            checkCheckingAccount(account, "ac1", 200.0, 5555);
            assertEquals(user, account.getOwner());
            assertTrue(it.hasNext());
            account = it.next();
            checkCheckingAccount(account, "ac2", 100.0, 1234);
            assertEquals(user, account.getOwner());
            assertFalse(it.hasNext());
        } catch (JsonProcessingException e){
            fail("Throws JsonProcessingException");
        }
    }

    @Test
    public void testCheckingAccountAndUserSerializersDeserializers() {
        User user1 = new User(123456);
        user1.setName("name1");
        AbstractAccount account1 = new CheckingAccount("ac1", 200, 5555, user1);
        AbstractAccount account2 = new CheckingAccount("ac2", 100, 1234, user1);
        try{
            String json = mapper.writeValueAsString(user1);
            User user2 = mapper.readValue(json, User.class);
            assertEquals(user1.getName(), user2.getName(), "Usernames are not equal!");
            assertEquals(user1.getUserID(), user2.getUserID(), "UserIDs are not equal!");
            Iterator<AbstractAccount> it1 = user1.getAccounts().iterator();
            Iterator<AbstractAccount> it2 = user2.getAccounts().iterator();
            AbstractAccount user1Account;
            AbstractAccount user2Account;
            assertTrue(it1.hasNext());
            assertTrue(it2.hasNext());
            user1Account = it1.next();
            user2Account = it2.next();
            checkCheckingAccount(user1Account, user2Account);
            assertTrue(it1.hasNext());
            assertTrue(it2.hasNext());
            user1Account = it1.next();
            user2Account = it2.next();
            checkCheckingAccount(user1Account, user2Account);
            assertFalse(it1.hasNext());
            assertFalse(it2.hasNext());
        } catch (JsonProcessingException e){
            fail();
        }   
    }

}
