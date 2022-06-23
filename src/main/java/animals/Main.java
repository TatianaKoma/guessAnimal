package animals;

import animals.controller.Game;
import animals.localization.LanguageRule;
import animals.localization.LanguagesRulesEn;
import animals.localization.LanguagesRulesEo;
import animals.service.FileService;
import animals.service.JsonFileService;
import animals.service.KnowledgeTreeService;
import animals.service.XmlFileService;
import animals.service.YamlFileService;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String type = args.length > 0 && args[0].equals("-type") ? args[1] : "json";
        LanguageRule languageRule = getLanguageRule();
        KnowledgeTreeService knowledgeTreeService = new KnowledgeTreeService(languageRule);
        FileService fileService;
        if (type.equals("xml")) {
            fileService = new XmlFileService();
        } else if (type.equals("yaml")) {
            fileService = new YamlFileService();
        } else {
            fileService = new JsonFileService();
        }
        Game game = new Game(fileService, knowledgeTreeService);
        game.run();
    }

    private static LanguageRule getLanguageRule() {
        return System.getProperty("user.language").equals("eo") ? new LanguagesRulesEo() : new LanguagesRulesEn();
    }
}
