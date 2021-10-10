package json;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import core.SavingsAccount;
import core.BSUAccount;
import core.User;

public class CashFlowModuleTest {

    private static ObjectMapper mapper;

    @BeforeAll
    public static void setUp() {
        mapper = new ObjectMapper();
        mapper.registerModule(new CashFlowModule());
    }

    
    private final static String userWithOneAccount  = "{\"name\":\"nameA\",\"userID\":123456,\"accounts\":[{\"type\":\"checking\",\"name\":\"acA\",\"balance\":200.0,\"accountNumber\":5555}]}";
    private final static String userWithTwoAccounts = "{\"name\":\"nameB\",\"userID\":654321,\"accounts\":[{\"type\":\"savings\",\"name\":\"acA\",\"balance\":200.0,\"accountNumber\":5555},{\"type\":\"bsu\",\"name\":\"acB\",\"balance\":100.0,\"accountNumber\":1234}]}";

    @Test
    public void testAccountAndUserSerializers(){
        User user = new User(123456);
        user.setName("nameA");
        CheckingAccount account = new CheckingAccount("acA", 200, 5555, user);
        try{
            assertEquals(userWithOneAccount, mapper.writeValueAsString(user), 
                "Incorrect serialization of User and/or CheckingAccount!");
        } catch (JsonProcessingException e){
            fail();
        }
    }

    public static void checkAccount(AbstractAccount account, String name, double balance, int accountNumber) {
        assertEquals(name, account.getName(), "Incorrect account name for account: " + account);
        assertTrue(balance == account.getBalance(), "Incorrect balance for account: " + account);
        assertTrue(accountNumber == account.getAccountNumber(), "Incorrect account number for account: " + account);
    }

    public static void checkAccount(AbstractAccount account1, AbstractAccount account2) {
        checkAccount(account1, account2.getName(), account2.getBalance(), account2.getAccountNumber());
    }

    @Test
    public void testAccountAndUserDeserializers(){
        try {
            User user = mapper.readValue(userWithTwoAccounts, User.class);
            assertEquals("nameB", user.getName());
            assertTrue(654321 == user.getUserID());
            Iterator<AbstractAccount> it = user.getAccounts().iterator();
            AbstractAccount account = null;
            assertTrue(it.hasNext());
            account = it.next();
            assertTrue(account instanceof SavingsAccount);
            checkAccount(account, "acA", 200.0, 5555);
            assertEquals(user.getUserID(), account.getOwnerID());
            assertTrue(it.hasNext());
            account = it.next();
            assertTrue(account instanceof BSUAccount);
            checkAccount(account, "acB", 100.0, 1234);
            assertEquals(user.getUserID(), account.getOwnerID());
            assertFalse(it.hasNext());
        } catch (JsonProcessingException e){
            fail("Throws JsonProcessingException");
        }
    }

    @Test
    public void testAccountAndUserSerializersDeserializers() {
        User user1 = new User(123456);
        user1.setName("nameA");
        AbstractAccount account1 = new CheckingAccount("acA", 200, 5555, user1);
        AbstractAccount account2 = new CheckingAccount("acB", 100, 1234, user1);
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
            checkAccount(user1Account, user2Account);
            assertTrue(it1.hasNext());
            assertTrue(it2.hasNext());
            user1Account = it1.next();
            user2Account = it2.next();
            checkAccount(user1Account, user2Account);
            assertFalse(it1.hasNext());
            assertFalse(it2.hasNext());
        } catch (JsonProcessingException e){
            fail();
        }   
    }

}
