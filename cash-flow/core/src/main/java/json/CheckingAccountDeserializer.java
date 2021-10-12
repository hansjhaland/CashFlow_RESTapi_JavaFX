package json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import core.CheckingAccount;

public class CheckingAccountDeserializer extends JsonDeserializer<CheckingAccount>  {

    @Override
    public CheckingAccount deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        TreeNode treeNode = parser.getCodec().readTree(parser);
        return deserialize((JsonNode) treeNode);
    }

    public CheckingAccount deserialize(JsonNode jsonNode) {
        if (jsonNode instanceof ObjectNode) {
            ObjectNode objectNode = (ObjectNode) jsonNode;

            String name = "";
            double balance = 0.0;
            int accountNumber = 0;

            JsonNode textNode = objectNode.get("name");
            if (textNode instanceof TextNode) {
               name = textNode.asText();
            }

            JsonNode doubleNode = objectNode.get("balance");
            if (doubleNode instanceof DoubleNode) {
                balance = doubleNode.asDouble();
            }
            
            JsonNode intNode = objectNode.get("accountNumber");
            if (intNode instanceof IntNode) {
                accountNumber = intNode.asInt();
            }

            return new CheckingAccount(name, balance, accountNumber, null);
        }
        return null;
    }
    
}
