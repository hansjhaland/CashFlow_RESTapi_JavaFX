package json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import core.AbstractAccount;
import core.CheckingAccount;
import core.User;

public class UserDeserializer extends JsonDeserializer<User>  {

    private CheckingAccountDeserializer checkingAccountDeserializer = new CheckingAccountDeserializer();

    @Override
    public User deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        TreeNode treeNode = parser.getCodec().readTree(parser);
        if (treeNode instanceof ObjectNode) {

            ObjectNode objectNode = (ObjectNode) treeNode;

            String name = "";
            int userID = 0;

            JsonNode textNode = objectNode.get("name");
            if (textNode instanceof TextNode) {
                name = textNode.asText();
            }

            JsonNode intNode = objectNode.get("userID");
            if (intNode instanceof IntNode) {
               userID = intNode.asInt();
            }
            
            User user = new User(userID);
            user.setName(name);
            JsonNode accountsNode = objectNode.get("accounts");
            if (accountsNode instanceof ArrayNode) {
                for (JsonNode elementNode: accountsNode) {
                    CheckingAccount account = checkingAccountDeserializer.deserialize(elementNode);
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
