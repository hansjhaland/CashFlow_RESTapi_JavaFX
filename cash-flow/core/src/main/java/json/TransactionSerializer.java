package json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import core.Transaction;
import java.io.IOException;

/**
 * Serialization class for Transaction objects.
 */
public class TransactionSerializer extends JsonSerializer<Transaction> {

  /**
   * Method for serializing a Transaction object to specific JSON format.
   *
   * @param jasonGen JsonGenerator object
   * @param serializerProvider SerializerProvider object
   * @throws IOException if I/O problem when processing JSON content.
   */
  @Override
  public void serialize(Transaction transaction, JsonGenerator jasonGen,
      SerializerProvider serializerProvider) throws IOException {
    jasonGen.writeStartObject();
    jasonGen.writeStringField("payer", transaction.getPayer());
    jasonGen.writeNumberField("payersAccountNumber", transaction.getPayersAccountNumber());
    jasonGen.writeStringField("recipient", transaction.getRecipient());
    jasonGen.writeNumberField("recipientsAccountNumber", transaction.getRecipientsAccountNumber());
    jasonGen.writeNumberField("amount", transaction.getAmount());
    jasonGen.writeEndObject();
  }
}
