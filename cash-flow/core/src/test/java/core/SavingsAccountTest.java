package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SavingsAccountTest {

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User(123456);
    }
    
    @Test
    public void testDeposit() {
        //test deposisiting more than 25000
        AbstractAccount test = new SavingsAccount("Kontonavn", 0, 1234, user);
        test.deposit(25001);
        assertEquals(25001, test.getBalance(), "Expected '25001', but was: " + test.getBalance());
    }

    @Test
    public void testWithdraw() {
        //test normal withdrawal
        AbstractAccount test = new SavingsAccount("Kontonavn", 100, 1234, user);
        test.withdraw(80);
        assertEquals(20, test.getBalance(), "Expected the balance to be '20', but was: " + test.getBalance());
    }

    @Test
    public void testTransferAndWithdrawMoreThanTenTimes() {
        AbstractAccount test = new SavingsAccount("Kontonavn", 100, 1234, user);
        AbstractAccount test2 = new SavingsAccount("Kontonavn", 100, 4321, user);

        //test withdrawing 5 times
        for (int i = 0; i < 5; i++) { 
            test.withdraw(1); //withdrawing 5 times so that number of withdrawals is 5
        }
        assertEquals(95, test.getBalance(), "Expected '95', but was: " + test.getBalance());
        
        //test transfering 5 times
        for (int i = 0; i < 5; i++) { 
            test.transfer(test2, 1); //transfering 5 times so that number of transfers is 5
        }
        assertEquals(90, test.getBalance(), "Expected '90', but was: " + test.getBalance());

        //withdrawing for the 11th time should throw an exception
        assertThrows(IllegalStateException.class,
                    () -> test.withdraw(1),
                    "An IllegalStateException should have been thrown");

        //transfering for the 11th time should throw an exception
        assertThrows(IllegalStateException.class,
                    () -> test.transfer(test2, 1),
                    "An IllegalStateException should have been thrown");
    }

}
