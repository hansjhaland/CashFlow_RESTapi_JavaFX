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

public class TransactionSerializer extends JsonSerializer<Transaction> {

    @Override
    public void serialize(Transaction transaction, JsonGenerator jGen, SerializerProvider serializerProvider) throws IOException{
        jGen.writeStartObject();
        jGen.writeStringField("payer", transaction.getPayer());
        jGen.writeNumberField("payersAccountNumber", transaction.getPayersAccountNumber());
        jGen.writeStringField("recipient", transaction.getRecipient());
        jGen.writeNumberField("recipientsAccountNumber", transaction.getRecipientsAccountNumber());
        jGen.writeNumberField("amount", transaction.getAmount());
        jGen.writeEndObject();
    }
}
