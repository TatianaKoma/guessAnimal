package animals.controller;

import animals.localization.LanguageRule;
import animals.localization.LanguagesRules_en;
import animals.localization.LanguagesRules_eo;
import animals.model.Node;
import animals.service.FileService;
import animals.service.KnowledgeTreeService;
import animals.service.MenuService;

import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.Scanner;

import static animals.service.ResourceBundleService.getLocalString;

public class Game {
    public static final String FILE_LOCATION_NAME = getFileLocationName();
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final LocalTime MORNING_START = LocalTime.of(5, 1);
    private static final LocalTime MORNING_END = LocalTime.of(12, 0);
    private static final LocalTime DAY_START = LocalTime.of(12, 1);
    private static final LocalTime DAY_END = LocalTime.of(18, 0);

    FileService fs = new FileService();
    KnowledgeTreeService kts = new KnowledgeTreeService();
    LanguageRule lr = getLanguageRule();

    private LanguageRule getLanguageRule() {
        return System.getProperty("user.language").equals("eo") ? new LanguagesRules_eo() : new LanguagesRules_en();
    }

    public void run(String type) {
        String FILE_NAME = FILE_LOCATION_NAME + type;
        System.out.println(checkTimeOfDay() + "\n");

        var exist = fs.checkFile(FILE_NAME);
        Node root;
        if (exist) {
            root = fs.load(type, FILE_NAME);
        } else {
            System.out.println(getLocalString("ask.favorite.animal"));
            String firstAnimal = lr.getAnimal(SCANNER.nextLine().toLowerCase().trim());
            root = new Node(firstAnimal);
            fs.save(root, type, FILE_NAME);
        }
        System.out.println(getLocalString("welcome") + "\n");
        boolean quit = false;
        while (!quit) {
            System.out.println(MenuService.getMenuStr());
            int choice = SCANNER.nextInt();
            SCANNER.nextLine();
            switch (MenuService.getInstance(choice)) {
                case PLAY:
                    do {
                        System.out.println(getLocalString("think"));
                        System.out.println(getLocalString("enter"));
                        SCANNER.nextLine();
                        play(root);
                        System.out.println(getLocalString("play.again"));
                    } while (askYesNo());
                    fs.save(root, type, FILE_NAME);
                    break;
                case SHOW_LIST:
                    kts.printAllAnimals(fs.load(type, FILE_NAME));
                    break;
                case SEARCH_ANIMAL:
                    System.out.println(getLocalString("enter.animal"));
                    String userAnimal = SCANNER.nextLine();
                    kts.getAllFactsAboutAnimal(userAnimal, root);
                    System.out.println();
                    break;
                case STATISTICS:
                    kts.showStats(fs.load(type, FILE_NAME));
                    System.out.println();
                    break;
                case KNOWLEDGE_TREE:
                    kts.printTree(fs.load(type, FILE_NAME));
                    System.out.println();
                    break;
                case EXIT:
                    quit = true;
                    SCANNER.close();
                    System.out.println(getLocalString("bye"));
                    break;
                case UNDEFINED:
                    System.out.println(getLocalString("wrong"));
            }
        }
    }

    public void play(Node node) {
        System.out.println(node.askQ());
        if (!node.isLeaf()) {
            if (askYesNo()) {
                play(node.getYes());
            } else {
                play(node.getNo());
            }
        } else {
            if (!askYesNo()) {
                String firstAnimal = node.getData();
                System.out.println(getLocalString("give.up"));
                String userNewAnimal = lr.getAnimal(SCANNER.nextLine().strip().toLowerCase());

                String positive = lr.getStatement(firstAnimal, userNewAnimal);
                String negative = lr.toNegative(positive);
                System.out.println(getLocalString("is.correct") + userNewAnimal + "?");

                String learn1 = lr.toAnimalFact(positive, firstAnimal);
                String learn1Neg = lr.toAnimalFact(negative, firstAnimal);
                String learn2 = lr.toAnimalFact(positive, userNewAnimal);
                String learn2Neg = lr.toAnimalFact(negative, userNewAnimal);
                String question = lr.getQuestion(positive);
                Node secondNode = new Node(userNewAnimal);
                if (!askYesNo()) {
                    node.yes = new Node(node.getData());
                    node.data = question;
                    node.no = secondNode;
                    node.setLeaf(false);
                    System.out.println(lr.getLearnedPhrase(learn1, learn2Neg, question));
                } else {
                    node.no = new Node(node.data);
                    node.data = question;
                    node.yes = secondNode;
                    node.setLeaf(false);
                    System.out.println(lr.getLearnedPhrase(learn1Neg, learn2, question));
                }
            }
        }
    }

    public boolean askYesNo() {
        final var bundle = ResourceBundle.getBundle("application");
        while (true) {
            String answer = SCANNER.nextLine().trim();
            if (answer.matches(bundle.getString("pattern.yes"))) {
                return true;
            }
            if (answer.matches(bundle.getString("pattern.no"))) {
                return false;
            }
            System.out.println(bundle.getString("ask.again"));
        }
    }

    public String checkTimeOfDay() {
        LocalTime now = LocalTime.now();
        if (now.isAfter(MORNING_START) && now.isBefore(MORNING_END)) {
            return getLocalString("hi.morning");
        } else if (now.isAfter(DAY_START) && now.isBefore(DAY_END)) {
            return getLocalString("hi.day");
        } else {
            return getLocalString("hi.evening");
        }
    }

    private static String getFileLocationName() {
        String local = System.getProperty("user.language");
        return local.equals("eo") ? "animals_eo." : "animals.";
    }
}
