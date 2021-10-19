package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserTest {

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User(123456);
        user.setName("user");
    }
    
    @Test
    public void testConstructor() {
        //test acceptable values
        User user1 = new User(100000);
        User user2 = new User(999999);
        assertEquals(100000, user1.getUserID(), "Expected '100000', but was: " + user1.getUserID());
        assertEquals(999999, user2.getUserID(), "Expected '999999', but was: " + user2.getUserID());

        //throwing if the userID is not between 100000 and 999999
        assertThrows(IllegalArgumentException.class,
                    () -> new User(99999),
                    "An IllegalArgumentException should have been thrown");
        assertThrows(IllegalArgumentException.class,
                    () -> new User(1000000),
                    "An IllegalArgumentException should have been thrown");
    }

    @Test
    public void testAddingAccounts() {
        //test adding an account, and returning true
        AbstractAccount account1 = new CheckingAccount("Kontonavn", 0, 1000, null);
        assertTrue(user.addAccount(account1), "Expected the account to be added and 'addAccount' to return 'true', but returned 'false'");
        List<AbstractAccount> listOfAccounts = new ArrayList<>(user.getAccounts());
        assertEquals(account1, listOfAccounts.get(0), "Expected to find the account in users list of accounts, but found no such account");

        //test adding the same account, and returning false
        assertFalse(user.addAccount(account1), "Expected the account to not be added and 'addAccount' to return 'false', but returned 'true'");
        listOfAccounts = new ArrayList<>(user.getAccounts());
        assertEquals(1, listOfAccounts.size(), "Expected the number of accounts to be '1', but was: " + listOfAccounts.size());

        //test adding different accounts with the same account number
        AbstractAccount account2 = new CheckingAccount("Kontonavn", 0, 1000, null);
        assertFalse(user.addAccount(account2), "Expected the account to not be added and 'addAccount' to return 'false', but returned 'true'");
        listOfAccounts = new ArrayList<>(user.getAccounts());
        assertEquals(1, listOfAccounts.size(), "Expected the number of accounts to be '1', but was: " + listOfAccounts.size());


        //test if adding an account without an owner leads to the user adding the account becoming
        //the new owner of this account
        assertEquals(user.getUserID(), listOfAccounts.get(0).getOwnerID(), "Expected the specified user to be the owner of the account, but was: " + listOfAccounts.get(0).getOwnerID());
        
        //test if adding an account with a different owner leads to the user adding the account becoming
        //the new owner of this account, and the previous owner not being the owner anymore
        User otherUser = new User(234567);
        otherUser.setName("otherUser");
        AbstractAccount testAccount = new CheckingAccount("testAccount", 0, 1234, otherUser);
        user.addAccount(testAccount);
        assertTrue(user.getAccounts().contains(testAccount), "Expected that the user owning the account is 'true', but was 'false'");
        assertEquals(user.getUserID(), testAccount.getOwnerID(), "Expected that the new owner of the account is the user but was: " + testAccount.getOwnerID());
        assertFalse(otherUser.getAccounts().contains(testAccount), "Expected that the otherUser owning the account is 'false', but was 'true'");
    }

    @Test
    public void testReachingMaximumNumberOfAccounts() {
        //test adding 9000 accounts with account numbers from 1000 to 9999 leads to 
        //not being able to create a new account because the number of accounts
        //has reached a mixumum
        for (int i = 1000; i < 10000; i++) {
            new CheckingAccount("name", 0, i, user);
        }
        assertEquals(9000, user.getAccounts().size(), "Expected that the users number of accounts is '9000', but was: " + user.getAccounts().size());
        assertThrows(IllegalStateException.class,
                    () -> new CheckingAccount("name", 0, user),
                    "An IllegalStateExpection should have been thrown");
        
        //test adding an account which doesn't have the user as an owner
        AbstractAccount account = new CheckingAccount("name", 0, null);
        assertFalse(user.addAccount(account), "Expected that the account was not added, but it was");
    }

    @Test
    public void testRemovingAcconts() {
        //test removing an account, and returning true
        AbstractAccount account = new CheckingAccount("name", 0, 1234, user);
        assertTrue(user.removeAccount(account), "Expected that the removal of the account was 'true', but was 'false'");
        assertEquals(0, user.getAccounts().size(), "Expected that the number of accounts is '0', but was: " + user.getAccounts().size());

        //test removing the account again, and returning false
        assertFalse(user.removeAccount(account), "Expected that the removal of this account was 'false', but was 'true'");

        //test removing an account that the user doesn't own, but with the same account number
        AbstractAccount newAccount = new CheckingAccount("name", 0, 1234, null);
        assertFalse(user.removeAccount(newAccount), "Expecter that the removal of this account was 'false', but was 'true'");
    }

    @Test
    public void testSettingName() {
        //throwing if name is longer than 20 characters
        user.setName("qwertyuiopasdfghjklz");
        assertEquals("qwertyuiopasdfghjklz", user.getName(), "Expected the name to be changed to 'qwertyuiopasdfghjklz', but was: " + user.getName());
        assertThrows(IllegalArgumentException.class,
                    () -> user.setName("qwertyuiopasdfghjklzx"),
                    "An IllegalArgumentException should have been thrown");

        //throwing if the name contains characters other than letters and spaces
        user.setName("Tilfeldig brukernavn");
        assertThrows(IllegalArgumentException.class,
                    () -> user.setName("Bruk3rnavn med ta1l"),
                    "An IllegalArgumentException should have been thrown");
        assertThrows(IllegalArgumentException.class,
                    () -> user.setName("navn-med_tegn"),
                    "An IllegalArgumentException should have been thrown");
        assertThrows(IllegalArgumentException.class,
                    () -> user.setName("Navn! med tegn?"),
                    "An IllegalArgumentException should have been thrown");
    }

    @Test
    public void testGettingAccountNumbers() {
        List<Integer> accountNumbers = new ArrayList<>();
        AbstractAccount account1 = new CheckingAccount("name", 0, 1111, null);
        
        //test adding account
        user.addAccount(account1);
        accountNumbers.add(account1.getAccountNumber());
        assertTrue(user.getAccountNumbers().contains(account1.getAccountNumber()), "Expected that account1's account number was added to the user's list of account numbers, but it wasn't");
        
        //test removing this account
        user.removeAccount(account1);
        accountNumbers.remove((Integer) account1.getAccountNumber());
        assertFalse(user.getAccountNumbers().contains(account1.getAccountNumber()), "Expectes that account1's account number was removed from the user's list of account numbers, but it wasn't");
    }
    @Test
    public void testGetAccount() {
        //test getting the right account when given the accountnumber
        AbstractAccount account = new CheckingAccount("name", 100, 1234, user);
        assertEquals(account, user.getAccount(1234));
        
        //test returning null if there's not an account that has this account number
        assertNull(user.getAccount(9999));
    }
}
