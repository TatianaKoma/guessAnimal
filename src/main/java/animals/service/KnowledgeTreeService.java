package animals.service;

import animals.localization.LanguageRule;
import animals.localization.LanguagesRules_en;
import animals.localization.LanguagesRules_eo;
import animals.model.Node;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static animals.service.ResourceBundleService.getLocalString;

public class KnowledgeTreeService {
    public static final String THE_KNOWLEDGE_TREE_STATS = getLocalString("title");
    public static final String ROOT_NODE = getLocalString("root");
    public static final String TOTAL_NUMBER_OF_NODES = getLocalString("nodes");
    public static final String TOTAL_NUMBER_OF_ANIMALS = getLocalString("animals");
    public static final String TOTAL_NUMBER_OF_STATEMENTS = getLocalString("statements");
    public static final String HEIGHT_OF_THE_TREE = getLocalString("height");
    public static final String MINIMUM_DEPTH = getLocalString("minimum");
    public static final String AVERAGE_DEPTH = getLocalString("average");

    LanguageRule lr = getLanguageRule();

    private LanguageRule getLanguageRule() {
        return System.getProperty("user.language").equals("eo") ? new LanguagesRules_eo() : new LanguagesRules_en();
    }

    public Map<String, List<String>> getAnimals(Node root) {
        animals.clear();
        collectAnimals(root, new LinkedList<>());
        return animals;
    }

    private final Map<String, List<String>> animals = new HashMap<>();

    public void printAllAnimals(Node root) {
        System.out.println(getLocalString("list.animals"));
        getAnimals(root).keySet().stream()
                .sorted()
                .map(name -> " - " + name.replaceFirst("an? ", ""))
                .forEach(System.out::println);
        System.out.println();
    }

    public void getAllFactsAboutAnimal(String animal, Node root) {
        String correctAnimal = lr.getAnimal(animal);
        final var animals = getAnimals(root);
        if (animals.containsKey(correctAnimal) && animals.get(animal) != null) {
            System.out.println(getLocalString("search.facts") + animal.replaceFirst("an? |the ", "") + ": ");
            final var facts = animals.get(correctAnimal);
            facts.forEach(fact -> System.out.println(" - " + fact));
        } else {
            System.out.println(getLocalString("no.facts") + correctAnimal.replaceFirst("an? |the ", "") + ".");
        }
    }

    public void printTree(Node root) {
        System.out.println();
        printNode(root, false, " ");
    }

    public void printNode(Node node, boolean isYes, String prefix) {
        final String symbol;
        symbol = isYes ? "├" : "└";
        if (node.isLeaf()) {
            System.out.printf("%s%s %s%n", prefix, symbol, node.getData());
            return;
        }
        System.out.printf("%s%s %s%n", prefix, symbol, node.getData());
        prefix += isYes ? "│" : " ";
        printNode(node.getYes(), true, prefix);
        printNode(node.getNo(), false, prefix);
    }

    public void showStats(Node root) {
        final var stats = getAnimals(root).values().stream()
                .mapToInt(List::size).summaryStatistics();
        System.out.printf("%-30s %n%n", THE_KNOWLEDGE_TREE_STATS);
        System.out.printf("%-30s %s%n", ROOT_NODE, lr.getPositiveFactFromQuestion(root.getData()));
        System.out.printf("%-30s %d%n", TOTAL_NUMBER_OF_NODES, stats.getCount() * 2 - 1);
        System.out.printf("%-30s %d%n", TOTAL_NUMBER_OF_ANIMALS, stats.getCount());
        System.out.printf("%-30s %d%n", TOTAL_NUMBER_OF_STATEMENTS, stats.getCount() - 1);
        System.out.printf("%-30s %d%n", HEIGHT_OF_THE_TREE, stats.getMax());
        System.out.printf("%-30s %d%n", MINIMUM_DEPTH, stats.getMin());
        System.out.printf("%-30s %.1f%n", AVERAGE_DEPTH, stats.getAverage());
    }

    private void collectAnimals(final Node node, final Deque<String> facts) {
        if (node.isLeaf()) {
            animals.put(node.getData(), List.copyOf(facts));
            return;
        }
        final var statement = node.getData();
        facts.add(lr.getPositiveFactFromQuestion(statement));
        collectAnimals(node.getYes(), facts);
        facts.removeLast();
        facts.add(lr.getNegativeFactFromQuestion(statement));
        collectAnimals(node.getNo(), facts);
        facts.removeLast();
    }
}
