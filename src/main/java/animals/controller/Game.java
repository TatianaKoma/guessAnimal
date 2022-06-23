package animals.controller;

import animals.model.Node;
import animals.service.FileService;
import animals.service.KnowledgeTreeService;
import animals.service.MenuService;
import animals.service.ResourceBundleService;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Scanner;

import static animals.service.ResourceBundleService.getLocalString;

public class Game {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final LocalTime MORNING_START = LocalTime.of(5, 0);
    private static final LocalTime DAY_END = LocalTime.of(17, 59);

    public FileService fileService;
    public KnowledgeTreeService knowledgeTreeService;

    public Game(FileService fileService, KnowledgeTreeService knowledgeTreeService) {
        this.fileService = fileService;
        this.knowledgeTreeService = knowledgeTreeService;
    }

    public void run() throws IOException {
        System.out.println(getGreeting() + "\n");
        Node root = fileService.load();
        if (root.getData() == null) {
            System.out.println(getLocalString("ask.favorite.animal"));
            String firstAnimal = knowledgeTreeService.getLanguageRule().getAnimal(SCANNER.nextLine().toLowerCase().trim());
            root = new Node(firstAnimal);
            fileService.save(root);
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
                    fileService.save(root);
                }
                case SHOW_LIST -> knowledgeTreeService.printAllAnimals(root);
                case SEARCH_ANIMAL -> {
                    System.out.println(getLocalString("enter.animal"));
                    String userAnimal = SCANNER.nextLine();
                    knowledgeTreeService.getAllFactsAboutAnimal(userAnimal, root);
                    System.out.println();
                }
                case STATISTICS -> {
                    knowledgeTreeService.showStats(fileService.load());
                    System.out.println();
                }
                case KNOWLEDGE_TREE -> {
                    knowledgeTreeService.printTree(fileService.load());
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
                String userNewAnimal = knowledgeTreeService.getLanguageRule().getAnimal(SCANNER.nextLine().strip().toLowerCase());
                String positive = getStatement(firstAnimal, userNewAnimal);
                String negative = knowledgeTreeService.getLanguageRule().toNegative(positive);
                System.out.println(getLocalString("is.correct") + userNewAnimal + "?");

                String learn1 = knowledgeTreeService.getLanguageRule().toAnimalFact(positive, firstAnimal);
                String learn1Neg = knowledgeTreeService.getLanguageRule().toAnimalFact(negative, firstAnimal);
                String learn2 = knowledgeTreeService.getLanguageRule().toAnimalFact(positive, userNewAnimal);
                String learn2Neg = knowledgeTreeService.getLanguageRule().toAnimalFact(negative, userNewAnimal);
                String question = knowledgeTreeService.getLanguageRule().getQuestion(positive);
                Node secondNode = new Node(userNewAnimal);
                if (!askYesNo()) {
                    node.setYes(new Node(node.getData()));
                    node.setData(question);
                    node.setNo(secondNode);
                    System.out.println(knowledgeTreeService.getLanguageRule().getLearnedPhrase(learn1, learn2Neg, question));
                } else {
                    node.setNo(new Node(node.getData()));
                    node.setData(question);
                    node.setYes(secondNode);
                    System.out.println(knowledgeTreeService.getLanguageRule().getLearnedPhrase(learn1Neg, learn2, question));
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

    public static String getStatement(String first, String second) {
        while (true) {
            System.out.printf(getLocalString("specify"), first, second);
            String response = SCANNER.nextLine().toLowerCase().trim();

            if (response.matches(ResourceBundleService.PATTERN_STATEMENT)) {
                return response.replaceFirst("(.+)\\.+", "$1");
            }
            System.out.printf(getLocalString("examples"));
        }
    }
}
