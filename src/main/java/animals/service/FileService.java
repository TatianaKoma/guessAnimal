package animals.service;

import animals.model.Node;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public abstract class FileService {

    public void save(Node root) {
        String fileName = getFileName();
        ObjectMapper objectMapper = getObjectMapper();
        try {
            objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValue(new File(fileName), root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Node load() {
        String fileName = getFileName();
        Node root = new Node();
        ObjectMapper objectMapper = getObjectMapper();
        if (checkFile()) {
            try {
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

    public abstract ObjectMapper getObjectMapper();

    public abstract String getFileName();
}
