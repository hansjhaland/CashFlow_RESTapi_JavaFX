package json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import core.AbstractAccount;
import core.User;
import java.io.IOException;

/**
 * A class that convertes an JSON object to an object.
 */
public class UserDeserializer extends JsonDeserializer<User> {

  private AccountDeserializer checkingAccountDeserializer = new AccountDeserializer();

  /**
   * Method for deserialization of a User object.
   *
   * @param parser a JsonParser object.
   * @param ctxt a DeserializationContext object.
   * @return User object.
   * @throws IOException if I/O problem when processing JSON content.
   * @throws JsonProcessingException if problem other than I/O is encountered 
   *        when processing JSON content.
   */
  @Override
  public User deserialize(JsonParser parser, DeserializationContext ctxt) 
      throws IOException, JsonProcessingException {
    TreeNode treeNode = parser.getCodec().readTree(parser);
    if (treeNode instanceof ObjectNode) {

      ObjectNode objectNode = (ObjectNode) treeNode;

      String name = "";
      int userId = 0;

      JsonNode textNode = objectNode.get("name");
      if (textNode instanceof TextNode) {
        name = textNode.asText();
      }

      JsonNode intNode = objectNode.get("userID");
      if (intNode instanceof IntNode) {
        userId = intNode.asInt();
      }

      User user = new User(userId);
      user.setName(name);
      JsonNode accountsNode = objectNode.get("accounts");
      if (accountsNode instanceof ArrayNode) {
        for (JsonNode elementNode : accountsNode) {
          AbstractAccount account = checkingAccountDeserializer.deserialize(elementNode);
          if (account != null) {
            user.addAccount(account);
            account.setOwner(user);
          }
        }
      }
      return user;
    }
    return null;
  }

}
