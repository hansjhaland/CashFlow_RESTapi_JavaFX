package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GeneralAccountTest {

    private User user;
    private AbstractAccount test;

    @BeforeEach
    public void setUp() {
        user = new User(180900);
        user.setName("user");
        test = new CheckingAccount("Kontonavn", 0, 1000, null);
        test.setName("test");
    }

    @Test
    public void testConstructorWithAccountNumber() {
        //throwing if name is longer than 20 characters
        AbstractAccount test = new CheckingAccount("qwertyuiopasdfghjklz", 0, 1000, null);
        assertThrows(IllegalArgumentException.class,
                    () -> new CheckingAccount("qwertyuiopasdfghjklzx", 0, 1000, null),
                    "An IllegalArgumentException should have been thrown");

        //throwing if name contains characters other than letters or spaces
        test = new CheckingAccount("Tilfeldig kontonavn", 0, 1000, null);
        assertThrows(IllegalArgumentException.class,
                    () -> new CheckingAccount("K0nt0navn med ta1l", 0, 1000, null),
                    "An IllegalArgumentException should have been thrown");
        assertThrows(IllegalArgumentException.class,
                    () -> new CheckingAccount("Kontonavn_med-tegn", 0, 1000, null),
                    "An IllegalArgumentException should have been thrown");
        assertThrows(IllegalArgumentException.class,
                    () -> new CheckingAccount("Kontonavn! med tegn?", 0, 1000, null),
                    "An IllegalArgumentException should have been thrown");

        //throwing if the account number is not between 1000 and 9999
        test = new CheckingAccount("Kontonavn", 0, 9999, null);
        assertThrows(IllegalArgumentException.class,
                    () -> new CheckingAccount("Kontonavn", 0, 999, null),
                    "An IllegalArgumentException should have been thrown");
        assertThrows(IllegalArgumentException.class,
                    () -> new CheckingAccount("Kontonavn", 0, 10000, null),
                    "An IllegalArgumentException should have been thrown");

        //return false if the account number already exists in the users list of account numbers
        test = new CheckingAccount("Kontonavn", 0, 1234, user);
        AbstractAccount test2 = new CheckingAccount("Kontonavn", 0, 1234, null);
        AbstractAccount test3 = new CheckingAccount("Kontonavn", 0, 4321, null);
        user.addAccount(test3);
        assertFalse(user.addAccount(test2), "Expected that the account was not added, but it was");

        //throwing if the initial balance is set to be less than 0
        assertThrows(IllegalArgumentException.class,
                    () -> new CheckingAccount("Kontnavn", -1, 1000, null),
                    "An IllegalArgumentException should have been thrown");

        //test that the fields have been set correctly
        test = new CheckingAccount("Kontonavn", 123, 2345, user);
        assertEquals("Kontonavn", test.getName(), "Expected 'Kontonavn', but was: " + test.getName());
        assertEquals(123, test.getBalance(), "Expected '123', but was: " + test.getBalance());
        assertEquals(2345, test.getAccountNumber(), "Expected '2345', but was: " + test.getAccountNumber());
        assertEquals(user.getUserID(), test.getOwnerID());

        //reinitializing object-pointer 'test'
        //and see if the users list of accounts is correct
        test = new CheckingAccount("Kontonavn", 123, 2346, user);
        List<AbstractAccount> listOfAccounts = new ArrayList<>(user.getAccounts());
        assertEquals(4, listOfAccounts.size(),
                    "Expected '4', but was: " + listOfAccounts.size());
        assertTrue(listOfAccounts.stream()
                                 .anyMatch(account -> account.getAccountNumber() == 2345),
                                 "Expected to find account with account number '2345', but non was found");
        assertTrue(listOfAccounts.stream()
                                 .anyMatch(account -> account.getAccountNumber() == 2346),
                                 "Expected to find account with account number '2346', but non was found");

    }


    @Test
    public void testConstructorWithoutAccountNumber() {
        //test if account numbers are set properly when automatically generated
        user.addAccount(test);
        AbstractAccount test2 = new CheckingAccount("testTwo", 100, user);
        assertEquals(1001, test2.getAccountNumber(), "Expected account number to be '1001', but was: " + test2.getAccountNumber());
        new CheckingAccount("testThree", 200, 1002, user);
        AbstractAccount test4 = new CheckingAccount("testFour", 300, user);
        assertEquals(1003, test4.getAccountNumber(), "Expected account number to be '1003', but was: " + test4.getAccountNumber());
    }

    @Test
    public void testDepositingNegativeAmount() {
        //test if normal deposit works properly
        test.deposit(100);
        assertEquals(100, test.getBalance(), "The expected value is 100.0, but was: " + test.getBalance());

        //throwing if depositing a negative amount
        assertThrows(IllegalArgumentException.class,
                    () -> test.deposit(-1),
                    "An IllegalArgmuentException should have been thrown");
    }

    @Test
    public void testWithdraw() {
        //test if withdrawal works properly
        test = new CheckingAccount("Kontonavn", 100, 1000, null);
        test.withdraw(80);
        assertEquals(20, test.getBalance(), "The expected value is 20.0, but was: " + test.getBalance());

        //throwing if withdrawing a negative amount
        assertThrows(IllegalArgumentException.class,
                    () -> test.withdraw(-1),
                    "An IllegalArgmuentException should have been thrown");

        //throwing if withdrawing more than current balance
        assertThrows(IllegalStateException.class,
                    () -> test.withdraw(21),
                    "An IllegalStateException should have been thrown");
    }

    


}
