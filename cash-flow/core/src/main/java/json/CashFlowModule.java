package json;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import core.AbstractAccount;
import core.Transaction;
import core.User;

/**
 * A class that combines all of the serializers and deserializers.
 */

public class CashFlowModule extends SimpleModule {
  private static final String NAME = "CashFlowModule";

  /**
   * Constructs a CashFlowModule object that adds serializers and deserializers for User,
   * AbstractAccount and Transaction objects.
   */
  public CashFlowModule() {
    super(NAME, Version.unknownVersion());
    addSerializer(AbstractAccount.class, new AccountSerializer());
    addSerializer(User.class, new UserSerializer());
    addDeserializer(AbstractAccount.class, new AccountDeserializer());
    addDeserializer(User.class, new UserDeserializer());
    addSerializer(Transaction.class, new TransactionSerializer());
    addDeserializer(Transaction.class, new TransactionDeserializer());
  }
}
