package animals.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

public class YamlFileService extends FileService{
    @Override
    public ObjectMapper getObjectMapper() {
        return new YAMLMapper();
    }

    @Override
    public String getFileName() {
        String local = System.getProperty("user.language");
        return local.equals("eo") ? "animals_eo.yaml" : "animals.yaml";
    }
}
