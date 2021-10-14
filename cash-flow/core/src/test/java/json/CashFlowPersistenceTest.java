package json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.io.TempDir;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;


import java.util.ArrayList;
import java.util.List;

import core.AbstractAccount;
import core.CheckingAccount;
import core.User;

public class CashFlowPersistenceTest {
    
    private CashFlowPersistence cashFlowPersistence = new CashFlowPersistence();

    User user1;
    AbstractAccount account1, account2;

    @TempDir
    Path tempDir;

    @BeforeEach
    public void setUp(){
        user1 = new User(123456);
        user1.setName("name");
        account1 = new CheckingAccount("acA", 100.0, 5555, user1);
        account2 = new CheckingAccount("acB", 200.0, 1234, user1);
        account1.transfer(account2, 10);
    }


    @Test
    public void testSerializersDeserializers() {
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
            CashFlowModuleTest.checkAccount(account1, accountList.get(0));
            CashFlowModuleTest.checkAccount(account2, accountList.get(1));
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void testWriteToReadFromFile(){
        try{
            Path testPath = tempDir.resolve("testFile.json");
            cashFlowPersistence.setSaveFilePath(testPath);
            cashFlowPersistence.saveUser(user1);
            User user2 = cashFlowPersistence.loadUser();
            assertEquals("name", user2.getName());
            assertEquals(123456, user2.getUserID());
            List<AbstractAccount> accountList = new ArrayList<>();
            for (AbstractAccount user2Account : user2.getAccounts()) {
                accountList.add(user2Account);
            }
            CashFlowModuleTest.checkAccount(account1, accountList.get(0));
            CashFlowModuleTest.checkAccount(account2, accountList.get(1));
        }
        catch(InvalidPathException | IllegalStateException | IOException e) {
            fail();
        }
    }

    @Test
    public void testSetSaveFilePathWith(){
        assertThrows(IllegalStateException.class, () -> {
            cashFlowPersistence.setSaveFilePath((Path) null);
        });
    }

}
