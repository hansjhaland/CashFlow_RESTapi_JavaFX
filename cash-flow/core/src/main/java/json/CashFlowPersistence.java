package json;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import core.User;



public class CashFlowPersistence {

    private ObjectMapper mapper;
    private Path saveFilePath = null;

    public CashFlowPersistence() {
        mapper = new ObjectMapper();
        mapper.registerModule(new CashFlowModule());
    }

    public User readUser(Reader reader) throws IOException {
        return mapper.readValue(reader, User.class);
    }

    public void writeUser(User user, Writer writer) throws IOException {
        mapper.writerWithDefaultPrettyPrinter().writeValue(writer, user);
    }

    public void setSaveFilePath(String saveFile) {
        this.saveFilePath = Paths.get(System.getProperty("user.home"), saveFile);
    }

    public void setSaveFilePath(Path path){
        this.saveFilePath = path;
    }

    private void checkSaveFilePath(Path saveFilePath) {
        if (saveFilePath == null) {
            throw new IllegalStateException("The save file doesn't exist");
        }
    }

    public User loadUser() throws IOException, IllegalStateException {
        checkSaveFilePath(saveFilePath);
        try (Reader reader = new FileReader(saveFilePath.toFile(), StandardCharsets.UTF_8)) {
            return readUser(reader);
        }
    }

    public void saveUser(User user) throws IOException, IllegalStateException {
        checkSaveFilePath(saveFilePath);
        try (Writer writer = new FileWriter(saveFilePath.toFile(), StandardCharsets.UTF_8)) {
            writeUser(user, writer);
        }
    }

}