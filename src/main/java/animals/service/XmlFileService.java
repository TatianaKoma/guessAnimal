package animals.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class XmlFileService extends FileService{
    @Override
    public ObjectMapper getObjectMapper() {
        return new XmlMapper();
    }

    @Override
    public String getFileName() {
        String local = System.getProperty("user.language");
        return local.equals("eo") ? "animals_eo.xml" : "animals.xml";
    }
}
