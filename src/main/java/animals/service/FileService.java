package animals.service;

import animals.model.Node;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public abstract class FileService {

    public void save(Node root) throws IOException {
        String fileName = getFileName();
        try {
            ObjectMapper objectMapper = getObjectMapper();
            objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValue(new File(fileName), root);
        } catch (IOException e) {
            throw new IOException("Not able to save file", e);
        }
    }

    public Node load() {
        String fileName = getFileName();
        Node root = new Node();
        if (checkFile()) {
            try {
                ObjectMapper objectMapper = getObjectMapper();
                root = objectMapper.readValue(new File(fileName), Node.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return root;
    }

    private boolean checkFile() {
        File file = new File(getFileName());
        return file.exists() && file.length() > 0;
    }

    public String getFileName() {
        String local = getLocal();
        return getFileType(local);
    }

    private String getLocal() {
        return System.getProperty("user.language");
    }

    public abstract ObjectMapper getObjectMapper();

    public abstract String getFileType(String local);
}
