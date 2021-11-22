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

import core.Transaction;


public class TransactionDeserializer extends JsonDeserializer<Transaction> {

  /**
   * Method for deserialization of a Transaction object.
   * 
   * @param parser a JsonParser object.
   * @param ctxt a DeserializationContext object.
   * @throws IOException if I/O problem when processing JSON content.
   * @throws JsonProcessingException if porblem other than I/O is encountered when processing JSON
   *         content.
   * @return Transaction object which is returned from helper method
   */
  @Override
  public Transaction deserialize(JsonParser parser, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    TreeNode treeNode = parser.getCodec().readTree(parser);
    return deserialize((JsonNode) treeNode);
  }

  /**
   * Helper method for deserialization of a Transaction object.
   * 
   * @param jsonNode a JsonNode object.
   * @return a Transaction object.
   */
  public Transaction deserialize(JsonNode jsonNode) {
    if (jsonNode instanceof ObjectNode) {
      ObjectNode objectNode = (ObjectNode) jsonNode;

      String payer = "";
      int payersAccountNumber = 0;
      String recipient = "";
      int recipientsAccountNumber = 0;
      double amount = 0.0;

      JsonNode payerNode = objectNode.get("payer");
      if (payerNode instanceof TextNode) {
        payer = payerNode.asText();
      }

      JsonNode payersAccountNumberNode = objectNode.get("payersAccountNumber");
      if (payersAccountNumberNode instanceof IntNode) {
        payersAccountNumber = payersAccountNumberNode.asInt();
      }

      JsonNode recipientNode = objectNode.get("recipient");
      if (recipientNode instanceof TextNode) {
        recipient = recipientNode.asText();
      }

      JsonNode recipientsAccountNumberNode = objectNode.get("recipientsAccountNumber");
      if (recipientsAccountNumberNode instanceof IntNode) {
        recipientsAccountNumber = recipientsAccountNumberNode.asInt();
      }

      JsonNode amountNode = objectNode.get("amount");
      if (amountNode instanceof DoubleNode) {
        amount = amountNode.asDouble();
      }

      return new Transaction(payer, payersAccountNumber, recipient, recipientsAccountNumber, amount);
    }
    return null;
  }

}
