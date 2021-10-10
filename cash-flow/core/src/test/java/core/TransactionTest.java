package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import core.Transaction.TransactionType;

public class TransactionTest {

    private User user1;
    private User user2;
    private AbstractAccount account1;
    private AbstractAccount account2;
    
    @BeforeEach
    public void setUp() {
        user1 = new User(123456);
        user1.setName("userOne");
        user2 = new User(654321);
        user2.setName("userTwo");
        account1 = new CheckingAccount("accountOne", 100, 1234, user1);
        account2 = new CheckingAccount("accountTwo", 200, 4321, user2);
    }

    @Test
    public void testConstructor() {
        //testing that the type is set to 'TRANSFER'
        Transaction transaction = new Transaction(account1, account2, 50);
        TransactionType type = TransactionType.TRANSFER;
        assertEquals(type, transaction.getType(), "Expected that the transaction type to be a 'TRANSFER', but was: " + transaction.getType());
        
        //testing that the fields are set correctly
        assertEquals(50, transaction.getAmount(), "Expected the amount transferred to be '50', but was: " + transaction.getAmount());
        assertEquals(account2.getAccountNumber(), transaction.getRecipientsAccountNumber(), "Expected the recipients account number to be '4321', but was: " + transaction.getRecipientsAccountNumber());
        assertEquals(account2.getName(), transaction.getRecipient(), "Expected the recipients name to be 'accountTwo', but was: " + transaction.getRecipient());
        assertEquals(account1.getName(), transaction.getPayer(), "Expected the payers name to be 'accountOne', but was: " + transaction.getPayer());
        assertEquals(account1.getAccountNumber(), transaction.getPayersAccountNumber(), "Expected the payers account number to be '1234', but was: "+ transaction.getPayersAccountNumber());
        
        //testing that the type is set to 'DEPOSIT'
        transaction = new Transaction(null, account1, 50);
        type = TransactionType.DEPOSIT;
        assertEquals(type, transaction.getType(), "Expected that the transaction type to be a 'DEPOSIT', but was: " + transaction.getType());
                
        //testing that the fields are set correctly
        assertEquals(50, transaction.getAmount(), "Expected the amount deposited to be '50', but was: " + transaction.getAmount());
        assertEquals(account1.getAccountNumber(), transaction.getRecipientsAccountNumber(), "Expected the recipients account number to be '1234', but was: " + transaction.getRecipientsAccountNumber());
        assertEquals(account1.getName(), transaction.getRecipient(), "Expected the recipients name to be 'accountOne', but was: " + transaction.getRecipient());
        assertEquals("", transaction.getPayer(), "Expected the payers name to be empty, but was: " + transaction.getPayer());
        assertEquals(0, transaction.getPayersAccountNumber(), "Expected the payers account number to be '0', but was: "+ transaction.getPayersAccountNumber());
        
        //testing that the type is set to 'WITHDRAWAL'
        transaction = new Transaction(account1, null, 50);
        type = TransactionType.WITHDRAWAL;
        assertEquals(type, transaction.getType(), "Expected that the transaction type to be a 'WITHDRAWAL', but was: " + transaction.getType());
                
        //testing that the fields are set correctly
        assertEquals(50, transaction.getAmount(), "Expected the amount withdrawn to be '50', but was: " + transaction.getAmount());
        assertEquals(0, transaction.getRecipientsAccountNumber(), "Expected the recipients account number to be '0', but was: " + transaction.getRecipientsAccountNumber());
        assertEquals("", transaction.getRecipient(), "Expected the recipients name to be empty, but was: " + transaction.getRecipient());
        assertEquals(account1.getName(), transaction.getPayer(), "Expected the payers name to be 'accountOne', but was: " + transaction.getPayer());
        assertEquals(account1.getAccountNumber(), transaction.getPayersAccountNumber(), "Expected the payers account number to be '1234', but was: "+ transaction.getPayersAccountNumber());

        //testing throwing if payerAccount and recievingAccount are both 'null'
        assertThrows(IllegalArgumentException.class,
                    () -> new Transaction(null, null, 50),
                    "An IllegalArgumentException should have been thrown");

        //testing throwing if amount is not larger than 0
        assertThrows(IllegalArgumentException.class,
                    () -> new Transaction(account1, account2, 0),
                    "An IllegalArgumentException should have been thrown");
    }

    
    @Test
    public void testTransferBetweenTwoAccounts() {
        //test transfer between two accounts
        account1.transfer(account2, 50);
        assertEquals(50, account1.getBalance(), "Expected that the balance is '50', but was: " + account1.getBalance());
        assertEquals(250, account2.getBalance(), "Expected that the balance is '250', but was: " + account2.getBalance());

        //checking that the corresponding transaction contains the right information
        Transaction transaction = account1.getTransactionHistory().get(0);
        assertEquals(TransactionType.TRANSFER, transaction.getType(), "Expected the transaction type to be a 'TRANSFER', but was: " + transaction.getType());
        assertEquals(account1.getAccountNumber(), transaction.getPayersAccountNumber(), "Expected payers account number to be '1234', but was: " + transaction.getPayersAccountNumber());
        assertEquals(account2.getAccountNumber(), transaction.getRecipientsAccountNumber(), "Expected recipients account number to be '4321', but was: " + transaction.getRecipientsAccountNumber());
        assertEquals(account1.getName(), transaction.getPayer(), "Expected the payers name to be 'accountOne', but was: " + transaction.getPayer());
        assertEquals(account2.getName(), transaction.getRecipient(), "Expected the recipients name to be 'accountTwo', but was: " + transaction.getRecipient());
    }

    @Test
    public void testTransfer_moreThanCurrentBalance() {
        //test transfering more than current balance
        assertThrows(IllegalStateException.class,
                    () -> account1.transfer(account2, 150),
                    "An IllegalStateException should have been thrown");
    }

    @Test
    public void testTransfer_moreThan25000InBsuAccount() {
        //test transfering to a BSUAccount so that the balance exceeds 25000
        account1 = new BSUAccount("BSU", 24000, user1);
        account2 = new CheckingAccount("otherAccount", 2000, user1);
        assertThrows(IllegalStateException.class, 
                    () -> account2.transfer(account1, 2000),
                    "An IllegalStateException should have been thrown");
    }

    @Test
    public void testTransfer_maxWithdrawalsFromSavingsAccount() {
        //test transfering from a SavingsAccount when number of transfers exceed 10
        account1 = new SavingsAccount("name", 200, user1);
        for (int i = 0; i < 10; i++) {
            account1.transfer(account2, 5);
        }
        assertThrows(IllegalStateException.class,
                    () -> account1.transfer(account2, 5),
                    "An IllegalStateException should have been thrown");
    }

    @Test
    public void testDeposit() {
        //test depositing and checking that the generated transaction consists of
        //the right information
        account1.deposit(50);
        Transaction transaction = account1.getTransactionHistory().get(0);
        assertEquals(TransactionType.DEPOSIT, transaction.getType(), "Expected that the transaction type to be a 'DEPOSIT', but was:" + transaction.getType());
        assertEquals(50, transaction.getAmount(), "Expected the amount to be '50', but was: " + transaction.getAmount());
        assertEquals(0, transaction.getPayersAccountNumber(), "Expected payers account number to be '0', but was: " + transaction.getPayersAccountNumber());
        assertEquals(account1.getAccountNumber(), transaction.getRecipientsAccountNumber(), "Expected recipients account number to be '1234', but was: " + transaction.getRecipientsAccountNumber());
        assertEquals("", transaction.getPayer(), "Expected the payers name to be empty, but was: " + transaction.getPayer());
        assertEquals(account1.getName(), transaction.getRecipient(), "Expected the recipients name to be 'accountOne', but was: " + transaction.getRecipient());
    }

    @Test
    public void testWithdraw() {
        //test withdrawing and checking that the generated transaction consists of
        //the right information
        account1.withdraw(50);
        Transaction transaction = account1.getTransactionHistory().get(0);
        assertEquals(TransactionType.WITHDRAWAL, transaction.getType(), "Expected that the transaction type to be a 'WITHDRAWAL', but was:" + transaction.getType());
        assertEquals(50, transaction.getAmount(), "Expected the amount to be '50', but was: " + transaction.getAmount());
        assertEquals(account1.getAccountNumber(), transaction.getPayersAccountNumber(), "Expected payers account number to be '1234', but was: " + transaction.getPayersAccountNumber());
        assertEquals(0, transaction.getRecipientsAccountNumber(), "Expected recipients account number to be '0', but was: " + transaction.getRecipientsAccountNumber());
        assertEquals(account1.getName(), transaction.getPayer(), "Expected the payers name to be 'accountOne', but was: " + transaction.getPayer());
        assertEquals("", transaction.getRecipient(), "Expected the recipients name to be empty, but was: " + transaction.getRecipient());
    }
}
