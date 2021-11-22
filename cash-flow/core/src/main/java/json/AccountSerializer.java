package json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import core.AbstractAccount;
import core.CheckingAccount;
import core.SavingsAccount;
import core.Transaction;
import core.BSUAccount;

public class AccountSerializer extends JsonSerializer<AbstractAccount> {

  /**
   * Method for serializing an AbstractAccount object to specific JSON format.
   * 
   * @param account AbstractAccount object to be serialized.
   * @param jGen JsonGenerator object
   * @param serializerProvider SerializerProvider object
   * @throws IOException if I/O problem when processing JSON content.
   */
  @Override
  public void serialize(AbstractAccount account, JsonGenerator jGen, SerializerProvider serializerProvider)
      throws IOException {
    jGen.writeStartObject();
    if (account instanceof CheckingAccount) {
      jGen.writeStringField("type", "checking");
    }
    if (account instanceof SavingsAccount) {
      jGen.writeStringField("type", "savings");
    }
    if (account instanceof BSUAccount) {
      jGen.writeStringField("type", "bsu");
    }
    jGen.writeStringField("name", account.getName());
    jGen.writeNumberField("balance", account.getBalance());
    jGen.writeNumberField("accountNumber", account.getAccountNumber());
    jGen.writeFieldName("transactionHistory");
    jGen.writeStartArray();
    for (Transaction transaction : account.getTransactionHistory()) {
      jGen.writeObject(transaction);
    }
    jGen.writeEndArray();
    jGen.writeEndObject();

  }
}
