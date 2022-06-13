package animals.controller;

import animals.localization.LanguageRule;
import animals.localization.LanguagesRulesEn;
import animals.localization.LanguagesRulesEo;
import animals.model.Node;
import animals.service.FileService;
import animals.service.KnowledgeTreeService;
import animals.service.MenuService;
import animals.service.ResourceBundleService;

import java.time.LocalTime;
import java.util.Scanner;

import static animals.service.ResourceBundleService.getLocalString;

public class Game {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final LocalTime MORNING_START = LocalTime.of(5, 0);
    private static final LocalTime DAY_END = LocalTime.of(17, 59);

    public FileService fs;
    public KnowledgeTreeService kts = new KnowledgeTreeService();
    public LanguageRule lr = getLanguageRule();

    public Game(FileService fs) {
        this.fs = fs;
    }

    private LanguageRule getLanguageRule() {
        return System.getProperty("user.language").equals("eo") ? new LanguagesRulesEo() : new LanguagesRulesEn();
    }

    public void run() {
        System.out.println(getGreeting() + "\n");
        Node root = fs.load();
        if (root.getData() == null) {
            System.out.println(getLocalString("ask.favorite.animal"));
            String firstAnimal = lr.getAnimal(SCANNER.nextLine().toLowerCase().trim());
            root = new Node(firstAnimal);
            fs.save(root);
        }
        System.out.println(getLocalString("welcome") + "\n");
        boolean quit = false;
        while (!quit) {
            System.out.println(MenuService.getMenuStr());
            int choice = SCANNER.nextInt();
            SCANNER.nextLine();
            switch (MenuService.getInstance(choice)) {
                case PLAY -> {
                    do {
                        System.out.println(getLocalString("think"));
                        System.out.println(getLocalString("enter"));
                        SCANNER.nextLine();
                        play(root);
                        System.out.println(getLocalString("play.again"));
                    } while (askYesNo());
                    fs.save(root);
                }
                case SHOW_LIST -> kts.printAllAnimals(fs.load());
                case SEARCH_ANIMAL -> {
                    System.out.println(getLocalString("enter.animal"));
                    String userAnimal = SCANNER.nextLine();
                    kts.getAllFactsAboutAnimal(userAnimal, root);
                    System.out.println();
                }
                case STATISTICS -> {
                    kts.showStats(fs.load());
                    System.out.println();
                }
                case KNOWLEDGE_TREE -> {
                    kts.printTree(fs.load());
                    System.out.println();
                }
                case EXIT -> {
                    quit = true;
                    SCANNER.close();
                    System.out.println(getLocalString("bye"));
                }
                case UNDEFINED -> System.out.println(getLocalString("wrong"));
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
                    node.setYes(new Node(node.getData()));
                    node.setData(question);
                    node.setNo(secondNode);
                    System.out.println(lr.getLearnedPhrase(learn1, learn2Neg, question));
                } else {
                    node.setNo(new Node(node.getData()));
                    node.setData(question);
                    node.setYes(secondNode);
                    System.out.println(lr.getLearnedPhrase(learn1Neg, learn2, question));
                }
            }
        }
    }

    public boolean askYesNo() {
        while (true) {
            String answer = SCANNER.nextLine().trim();
            if (answer.matches(ResourceBundleService.PATTERN_YES)) {
                return true;
            }
            if (answer.matches(ResourceBundleService.PATTERN_NO)) {
                return false;
            }
            System.out.println(getLocalString("ask.again"));
        }
    }

    public String getGreeting() {
        LocalTime now = LocalTime.now();
        if (now.isAfter(MORNING_START) && now.isBefore(LocalTime.NOON)) {
            return getLocalString("hi.morning");
        } else if (now.isAfter(LocalTime.NOON) && now.isBefore(DAY_END)) {
            return getLocalString("hi.day");
        } else {
            return getLocalString("hi.evening");
        }
    }
}
