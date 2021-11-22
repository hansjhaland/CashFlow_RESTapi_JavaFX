package json;

import java.io.IOException;

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
import core.BSUAccount;
import core.CheckingAccount;
import core.SavingsAccount;
import core.Transaction;

public class AccountDeserializer extends JsonDeserializer<AbstractAccount> {

  private TransactionDeserializer transactionDeserializer = new TransactionDeserializer();

  /**
   * Method for deserialization of an AbstractAccount object.
   * 
   * @param parser a JsonParser object.
   * @param ctxt a DeserializationContext object.
   * @throws IOException if I/O problem when processing JSON content.
   * @throws JsonProcessingException if porblem other than I/O is encountered when processing JSON
   *         content.
   * @return AbstractAccount object which is returned from helper method
   */
  @Override
  public AbstractAccount deserialize(JsonParser parser, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    TreeNode treeNode = parser.getCodec().readTree(parser);
    return deserialize((JsonNode) treeNode);
  }

  /**
   * Helper method for deserialization of an AbstractAccount object.
   * 
   * @param jsonNode a JsonNode object.
   * @return an AbstractAccount object of variyng type depending on the content of the "type" node in
   *         jsonNode.
   */
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

      AbstractAccount account = null;
      switch (type) {
        case "checking":
          account = new CheckingAccount(name, balance, accountNumber, null);
          break;
        case "savings":
          account = new SavingsAccount(name, balance, accountNumber, null);
          break;
        case "bsu":
          account = new BSUAccount(name, balance, accountNumber, null);
          break;
        default:
          throw new IllegalStateException("Could not create account of existing type.");
      }
      JsonNode transactionHistoryNode = objectNode.get("transactionHistory");
      if (transactionHistoryNode instanceof ArrayNode) {
        for (JsonNode transactionNode : transactionHistoryNode) {
          Transaction transaction = transactionDeserializer.deserialize(transactionNode);
          account.addToTransactionHistory(transaction);
        }
      }
      return account;

    }
    return null;
  }

}
