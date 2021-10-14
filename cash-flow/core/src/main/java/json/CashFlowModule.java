package json;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.core.util.VersionUtil;

import core.AbstractAccount;
import core.Transaction;
import core.User;


public class CashFlowModule extends SimpleModule {
    private static final String NAME = "CashFlowModule";
    private static final VersionUtil VERSION_UTIL = new VersionUtil() {};
    
    /**
     * Constructs a CashFlowModule object that adds serializers and deserializers for User, AbstractAccount and Transaction objects.
     */
    public CashFlowModule() {
        super(NAME, VERSION_UTIL.version());
        addSerializer(AbstractAccount.class, new AccountSerializer());
        addSerializer(User.class, new UserSerializer());
        addDeserializer(AbstractAccount.class, new AccountDeserializer());
        addDeserializer(User.class, new UserDeserializer());
        addSerializer(Transaction.class, new TransactionSerializer());
        addDeserializer(Transaction.class, new TransactionDeserializer());
    }
}
