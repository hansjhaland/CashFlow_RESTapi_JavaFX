package core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class BSUAccountTest {

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User(180900);
        user.setName("user");
    }
    
    @Test
    public void testConstructor() {
        //test depositing more than 25 000
        new BSUAccount("Kontonavn", 25000, 1000, null);
        assertThrows(IllegalStateException.class,
                    () -> new BSUAccount("Kontonavn", 25000.01, 1234, null),
                    "An IllegalStateException should have been thrown");
    }

    @Test
    public void testUserAlreadyOwningBSU() {
        //test adding two BSU-accounts
        AbstractAccount account1 = new BSUAccount("accountOne", 100, user);
        assertTrue(account1 instanceof BSUAccount, "'account1' should have been an instance of BSUAccount");
        assertThrows(IllegalStateException.class,
                    () -> new BSUAccount("accountTwo", 100, user),
                    "An IllegalStateException should have been thrown");

    }

    @Test
    public void testDeposit() {
        //trowing when the deposit leads to the BSU accounts balance exceeds 25 000
        AbstractAccount test = new BSUAccount("Kontonavn", 0, 1000, user);
        test.deposit(25000);
        assertEquals(25000, test.getBalance(), "Expected '25000', but was: " + test.getBalance());
        assertThrows(IllegalStateException.class,
                    () -> test.deposit(0.001),
                    "An IllegalStateException should have been thrown");
    }
}
