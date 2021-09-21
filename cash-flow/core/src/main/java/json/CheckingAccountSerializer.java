package json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import core.CheckingAccount;

public class CheckingAccountSerializer extends JsonSerializer<CheckingAccount> {

    @Override
    public void serialize(CheckingAccount account, JsonGenerator jGen, SerializerProvider serializerProvider) throws IOException{
        jGen.writeStartObject();
        jGen.writeStringField("start", account.getName());
        jGen.writeStringField("end", null);
        jGen.writeEndObject();
    }
}
