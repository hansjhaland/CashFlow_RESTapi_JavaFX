package json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import core.AbstractAccount;
import core.CheckingAccount;
import core.SavingsAccount;
import core.BSUAccount;

public class AccountSerializer extends JsonSerializer<AbstractAccount> {

    @Override
    public void serialize(AbstractAccount account, JsonGenerator jGen, SerializerProvider serializerProvider) throws IOException{
        jGen.writeStartObject();
        if (account instanceof CheckingAccount){
            jGen.writeStringField("type", "checking");
        }
        if (account instanceof SavingsAccount){
            jGen.writeStringField("type", "savings");
        }
        if (account instanceof BSUAccount){
            jGen.writeStringField("type", "bsu");
        }
        jGen.writeStringField("name", account.getName());
        jGen.writeNumberField("balance", account.getBalance());
        jGen.writeNumberField("accountNumber", account.getAccountNumber());
        jGen.writeEndObject();
    }
}
