package json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import core.AbstractAccount;
import core.User;

public class UserSerializer extends JsonSerializer<User> {

  /**
   * Method for serializing an User object to specific JSON format.
   * 
   * @param account User object to be serialized.
   * @param jGen JsonGenerator object
   * @param serializerProvider SerializerProvider object
   * @throws IOException if I/O problem when processing JSON content.
   */
  @Override
  public void serialize(User user, JsonGenerator jGen, SerializerProvider serializerProvider) throws IOException {
    jGen.writeStartObject();
    jGen.writeStringField("name", user.getName());
    jGen.writeNumberField("userID", user.getUserID());
    jGen.writeArrayFieldStart("accounts");
    for (AbstractAccount account : user.getAccounts()) {
      jGen.writeObject(account);
    }
    jGen.writeEndArray();
    jGen.writeEndObject();
  }
}
