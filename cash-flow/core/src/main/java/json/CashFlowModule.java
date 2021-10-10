package json;

import java.util.Iterator;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import core.AbstractAccount;
import core.CheckingAccount;
import core.User;


public class CashFlowModule extends SimpleModule {
    private static final String NAME = "CashFlowModule";
    private static final VersionUtil VERSION_UTIL = new VersionUtil() {};
  
    public CashFlowModule() {
        super(NAME, VERSION_UTIL.version());
        addSerializer(AbstractAccount.class, new AccountSerializer());
        addSerializer(User.class, new UserSerializer());
        addDeserializer(AbstractAccount.class, new AccountDeserializer());
        addDeserializer(User.class, new UserDeserializer());
    }
   
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new CashFlowModule());
        User user = new User(123456);
        user.setName("Hans");
        CheckingAccount account = new CheckingAccount("ac", 200, 5555, user);
        try{
            String json = mapper.writeValueAsString(user);
            User user2 = mapper.readValue(json, User.class);
            for (AbstractAccount account2 : user2.getAccounts()){
                System.out.println(account2);
            }
        } catch (JsonProcessingException e){
            System.out.println("Virket ikke");
            e.printStackTrace();
        }
    }

}
