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

import core.AbstractAccount;
import core.CheckingAccount;
import core.SavingsAccount;
import core.BSUAccount;

public class AccountDeserializer extends JsonDeserializer<AbstractAccount>  {

    @Override
    public AbstractAccount deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        TreeNode treeNode = parser.getCodec().readTree(parser);
        return deserialize((JsonNode) treeNode);
    }

    public AbstractAccount deserialize(JsonNode jsonNode) {
        if (jsonNode instanceof ObjectNode) {
            ObjectNode objectNode = (ObjectNode) jsonNode;

            String type = "";
            String name = "";
            double balance = 0.0;
            int accountNumber = 0;

            JsonNode typeNode = objectNode.get("type");
            if (typeNode instanceof TextNode) {
               type = typeNode.asText();
            }

            JsonNode nameNode = objectNode.get("name");
            if (nameNode instanceof TextNode) {
               name = nameNode.asText();
            }

            JsonNode balanceNode = objectNode.get("balance");
            if (balanceNode instanceof DoubleNode) {
                balance = balanceNode.asDouble();
            }
            
            JsonNode acNumberNode = objectNode.get("accountNumber");
            if (acNumberNode instanceof IntNode) {
                accountNumber = acNumberNode.asInt();
            }

            switch(type){
                case "checking":
                    return new CheckingAccount(name, balance, accountNumber, null);
                case "savings":
                    return new SavingsAccount(name, balance, accountNumber, null);
                case "bsu":
                    return new BSUAccount(name, balance, accountNumber, null);
            }
            
        }
        return null;
    }
    
}
