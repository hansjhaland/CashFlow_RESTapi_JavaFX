package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BSUAccountTest {

    private User user;

    @BeforeEach
    public void setUp() {
        User user = new User(180900);
        user.setName("user");
    }
    
    @Test
    public void testConstructor() {
        //test depositing more than 25 000
        new BSUAccount("Kontonavn", 25000, 1000, user);
        assertThrows(IllegalArgumentException.class,
                    () -> new BSUAccount("Kontonavn", 25000.01, 1234, user),
                    "An IllegalArgumentException should have been thrown");
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
