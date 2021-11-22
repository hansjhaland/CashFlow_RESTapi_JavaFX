package core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class BsuAccountTest {

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User(180900);
        user.setName("user");
    }
    
    @Test
    public void testConstructor() {
        //test depositing more than 25 000
        new BsuAccount("Kontonavn", 25000, 1000, null);
        assertThrows(IllegalStateException.class,
                    () -> new BsuAccount("Kontonavn", 25000.01, 1234, null),
                    "An IllegalStateException should have been thrown");
    }

    @Test
    public void testUserAlreadyOwningBsu() {
        //test adding two Bsu-accounts
        AbstractAccount account1 = new BsuAccount("accountOne", 100, user);
        assertTrue(account1 instanceof BsuAccount, "'account1' should have been an instance of BsuAccount");
        assertThrows(IllegalStateException.class,
                    () -> new BsuAccount("accountTwo", 100, user),
                    "An IllegalStateException should have been thrown");

    }

    @Test
    public void testDeposit() {
        //trowing when the deposit leads to the Bsu accounts balance exceeds 25 000
        AbstractAccount test = new BsuAccount("Kontonavn", 0, 1000, user);
        test.deposit(25000);
        assertEquals(25000, test.getBalance(), "Expected '25000', but was: " + test.getBalance());
        assertThrows(IllegalStateException.class,
                    () -> test.deposit(0.001),
                    "An IllegalStateException should have been thrown");
    }
}
