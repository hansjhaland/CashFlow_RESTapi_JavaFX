package json;

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
        addSerializer(CheckingAccount.class, new CheckingAccountSerializer());
        addSerializer(User.class, new UserSerializer());
    }

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new CashFlowModule());
        User user = new User(123456);
        CheckingAccount account = new CheckingAccount("ac", 200, 5555, user);
        try{
            System.out.println(mapper.writeValueAsString(user));
        } catch (JsonProsessingException e){
            System.out.println("Virket ikke");
        }
    }

}
