package json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import core.AbstractAccount;
import core.User;
import java.io.IOException;

/**
 * Serialization class for User objects.
 */
public class UserSerializer extends JsonSerializer<User> {

  /**
   * Method for serializing an User object to specific JSON format.
   *
   * @param jasonGen JsonGenerator object
   * @param serializerProvider SerializerProvider object
   * @throws IOException if I/O problem when processing JSON content.
   */
  @Override
  public void serialize(User user, JsonGenerator jasonGen, SerializerProvider serializerProvider) 
      throws IOException {
    jasonGen.writeStartObject();
    jasonGen.writeStringField("name", user.getName());
    jasonGen.writeNumberField("userID", user.getUserId());
    jasonGen.writeArrayFieldStart("accounts");
    for (AbstractAccount account : user.getAccounts()) {
      jasonGen.writeObject(account);
    }
    jasonGen.writeEndArray();
    jasonGen.writeEndObject();
  }
}
