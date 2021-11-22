package json;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import core.AbstractAccount;
import core.BsuAccount;
import core.CheckingAccount;
import core.SavingsAccount;
import core.Transaction;
import java.io.IOException;

/**
 * A class for serializer for an account object.
 */
public class AccountSerializer extends JsonSerializer<AbstractAccount> {

  /**
   * Method for serializing an AbstractAccount object to specific JSON format.
   *
   * @param account AbstractAccount object to be serialized.
   * @param jsonGen JsonGenerator object
   * @param serializerProvider SerializerProvider object
   * @throws IOException if I/O problem when processing JSON content.
   */
  @Override
  public void serialize(AbstractAccount account, JsonGenerator jsonGen, 
          SerializerProvider serializerProvider)
      throws IOException {
    jsonGen.writeStartObject();
    if (account instanceof CheckingAccount) {
      jsonGen.writeStringField("type", "checking");
    }
    if (account instanceof SavingsAccount) {
      jsonGen.writeStringField("type", "savings");
    }
    if (account instanceof BsuAccount) {
      jsonGen.writeStringField("type", "Bsu");
    }
    jsonGen.writeStringField("name", account.getName());
    jsonGen.writeNumberField("balance", account.getBalance());
    jsonGen.writeNumberField("accountNumber", account.getAccountNumber());
    jsonGen.writeFieldName("transactionHistory");
    jsonGen.writeStartArray();
    for (Transaction transaction : account.getTransactionHistory()) {
      jsonGen.writeObject(transaction);
    }
    jsonGen.writeEndArray();
    jsonGen.writeEndObject();

  }
}
