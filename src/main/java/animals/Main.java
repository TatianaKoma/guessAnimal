package animals;

import animals.controller.Game;
import animals.service.FileService;
import animals.service.JsonFileService;
import animals.service.XmlFileService;
import animals.service.YamlFileService;

public class Main {
    public static void main(String[] args) {
        String type = args.length > 0 && args[0].equals("-type") ? args[1] : "json";
        FileService fs;
        if (type.equals("xml")) {
            fs = new XmlFileService();
        } else if (type.equals("yaml")) {
            fs = new YamlFileService();
        } else {
            fs = new JsonFileService();
        }
        Game game = new Game(fs);
        game.run();
    }
}
