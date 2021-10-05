package ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import core.User;
import core.AbstractAccount;
import core.CheckingAccount;

public class CashFlowControllerTest{
    
    private CashFlowController controller;


    @BeforeEach
    public void setUp() {
        controller = new CashFlowController();
        /*Teste filepath*/
    }
    
    @Test
    public void testController() {
        assertNotNull(this.controller);
        
    }
    
    @Test
    public void testOnlyLetters() {
        //String konto = "konto";
        User user = new User(123456);
        /*AbstractAccount account = new CheckingAccount(konto,12,1000);
        User user = new User(1000,new AbstractAccount[]{account});*/
        user.setName("name");


        assertEquals(user.getUserID(),123456,"sjekker om konto ID er riktig");
    }
}
