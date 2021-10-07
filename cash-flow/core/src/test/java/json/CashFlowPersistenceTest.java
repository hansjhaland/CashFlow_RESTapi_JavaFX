package json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Iterator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import core.AbstractAccount;
import core.CheckingAccount;
import core.User;

public class CashFlowPersistenceTest {
    
    private CashFlowPersistence cashFlowPersistence = new CashFlowPersistence();

    @Test
    public void testSerializersDeserializers() {
        User user1 = new User(123456);
        user1.setName("name");
        AbstractAccount account1 = new CheckingAccount("ac1", 100.0, 5555, user1);
        AbstractAccount account2 = new CheckingAccount("ac2", 200.0, 1234, user1);
        try {
            StringWriter writer = new StringWriter();
            cashFlowPersistence.writeUser(user1, writer);
            String json = writer.toString();
            User user2 = cashFlowPersistence.readUser(new StringReader(json));
            assertEquals("name", user2.getName());
            assertEquals(123456, user2.getUserID());
            List<AbstractAccount> accountList = new ArrayList<>();
            for (AbstractAccount user2Account : user2.getAccounts()) {
                accountList.add(user2Account);
            }
            CashFlowModuleTest.checkCheckingAccount(account1, accountList.get(0));
            CashFlowModuleTest.checkCheckingAccount(account2, accountList.get(1));
        } catch (IOException e) {
            fail();
        }
    }

}