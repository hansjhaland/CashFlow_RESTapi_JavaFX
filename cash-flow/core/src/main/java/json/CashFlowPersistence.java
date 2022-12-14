package json;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.User;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A class that reads and writes to file.
 */
public class CashFlowPersistence {

  private ObjectMapper mapper;
  private Path saveFilePath = null;

  /**
   * Constructor for a CashFlowPersistence object that sets the mapper field and registers a
   * CashFlowModule to the mapper.
   */
  public CashFlowPersistence() {
    mapper = new ObjectMapper();
    mapper.registerModule(new CashFlowModule());
  }

  /**
   * Method for reading a User object with deserialization.
   *
   * @param reader a Reader object.
   * @return a user object constructed with deserialization.
   * @throws IOException if I/O problem when processing JSON content.
   */
  public User readUser(Reader reader) throws IOException {
    return mapper.readValue(reader, User.class);
  }

  /**
   * Method for writing a User object with serialization.
   *
   * @param user a User object to be serialized.
   * @param writer a Writer object.
   * @throws IOException if I/O problem when processing JSON content.
   */
  public void writeUser(User user, Writer writer) throws IOException {
    mapper.writerWithDefaultPrettyPrinter().writeValue(writer, user);
  }

  /**
   * Method for setting the saveFilePath field given a file name.
   *
   * @param saveFile name of the JSON file.
   */
  public void setSaveFilePath(String saveFile) {
    this.saveFilePath = Paths.get(System.getProperty("user.home"), saveFile);
  }

  /**
   * Method for setting the saveFilePath field given a file path.
   *
   * @param path path of the JSON file.
   */
  public void setSaveFilePath(Path path) {
    checkSaveFilePath(path);
    this.saveFilePath = path;
  }

  /**
   * Validation method for deciding whether file exists or not.
   *
   * @param saveFilePath file path to be checked
   * @throws IllegalStateException if file path is null.
   */
  private void checkSaveFilePath(Path saveFilePath) {
    if (saveFilePath == null) {
      throw new IllegalStateException("The save file doesn't exist");
    }
  }

  /**
   * Method for constructing a User object from data stored in JSON file.
   *
   * @return a User object read from file.
   * @throws IOException if I/O problem occured.
   * @throws IllegalStateException if file path is null.
   */
  public User loadUser() throws IOException, IllegalStateException {
    checkSaveFilePath(saveFilePath);
    try (Reader reader = new FileReader(saveFilePath.toFile(), StandardCharsets.UTF_8)) {
      return readUser(reader);
    }
  }
  
  /**
   * Gets a user from file with deserialization.
   *
   * @param saveFile the file that is used to have data saved to.
   * @return user that have been deserialized from the file.
   * @throws IOException if it does not find the user in the file.
   * @throws IllegalStateException if it does not find the user in the file.
   */
  public User loadUser(String saveFile) throws IOException, IllegalStateException {
    setSaveFilePath(saveFile);
    checkSaveFilePath(saveFilePath);
    try (Reader reader = new FileReader(saveFilePath.toFile(), StandardCharsets.UTF_8)) {
      return readUser(reader);
    }
  }

  /**
   * Method for storing a User object to a JSON file.
   *
   * @param user User object to be stored.
   * @throws IOException if I/O problem occured.
   * @throws IllegalStateException if file path is null.
   */
  public void saveUser(User user) throws IOException, IllegalStateException {
    checkSaveFilePath(saveFilePath);
    try (Writer writer = new FileWriter(saveFilePath.toFile(), StandardCharsets.UTF_8)) {
      writeUser(user, writer);
    }
  }

  /**
   * Save user to file with serialization.
   *
   * @param user User that is saved.
   * @param saveFile File that gets saved to.
   * @throws IOException if it does not find the file.
   * @throws IllegalStateException if it does not find the file.
   */
  public void saveUser(User user, String saveFile) throws IOException, IllegalStateException {
    setSaveFilePath(saveFile);
    checkSaveFilePath(saveFilePath);
    try (Writer writer = new FileWriter(saveFilePath.toFile(), StandardCharsets.UTF_8)) {
      writeUser(user, writer);
    }
  }

  /**
   * Creates a objectmapper that is used for deserialization and serialization.
   *
   * @return the objectmapper.
   */
  public static ObjectMapper createObjectMapper() {
    return new ObjectMapper().registerModule(new CashFlowModule());
  }

  /**
   * Checks if a file excists.
   *
   * @param saveFileName the name of the file.
   * @return true or false if the file excistes or not.
   */
  public boolean doesFileExist(String saveFileName) {
    if (saveFileName != null) {
      return Paths.get(System.getProperty("user.home"), saveFileName).toFile().exists();
    }
    return false;
  }

}
