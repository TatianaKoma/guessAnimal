package animals.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class JsonFileService extends FileService {
    @Override
    public ObjectMapper getObjectMapper() {
        return new JsonMapper();
    }

    @Override
    public String getFileType(String local) {
        return local.equals("eo") ? "animals_eo.json" : "animals.json";
    }
}
