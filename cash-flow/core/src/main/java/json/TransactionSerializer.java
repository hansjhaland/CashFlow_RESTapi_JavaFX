package json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import core.Transaction;


public class TransactionSerializer extends JsonSerializer<Transaction> {

  /**
   * Method for serializing a Transaction object to specific JSON format.
   * 
   * @param account Transaction object to be serialized.
   * @param jGen JsonGenerator object
   * @param serializerProvider SerializerProvider object
   * @throws IOException if I/O problem when processing JSON content.
   */
  @Override
  public void serialize(Transaction transaction, JsonGenerator jGen, SerializerProvider serializerProvider)
      throws IOException {
    jGen.writeStartObject();
    jGen.writeStringField("payer", transaction.getPayer());
    jGen.writeNumberField("payersAccountNumber", transaction.getPayersAccountNumber());
    jGen.writeStringField("recipient", transaction.getRecipient());
    jGen.writeNumberField("recipientsAccountNumber", transaction.getRecipientsAccountNumber());
    jGen.writeNumberField("amount", transaction.getAmount());
    jGen.writeEndObject();
  }
}
