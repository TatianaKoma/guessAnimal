package animals.localization;

import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class LanguagesRulesEn implements LanguageRule {
    private static final Pattern STATEMENT = Pattern.compile("it (can|has|is) .+");
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Map<String, String> POS_TO_NEG = Map.of("it can", "it can't",
            "it has", "it doesn't have", "it is", "it isn't");

    @Override
    public String getAnimal(String animal) {
        if (!animal.matches("an? .+")) {
            boolean isVowel = animal.matches("(the )?[aeiou].+");
            String article = isVowel ? "an " : "a ";
            animal = animal.replaceFirst("(the )?", article);
        }
        return animal;
    }

    @Override
    public String getStatement(String first, String second) {
        while (true) {
            System.out.printf("Specify a fact that distinguishes %s from %s.%n" +
                    "The sentence should satisfy one of the following templates:%n" +
                    "- It can ...%n- It has ...%n- It is a/an ...%n", first, second);
            String response = SCANNER.nextLine().toLowerCase().trim();
            if (STATEMENT.matcher(response).matches()) {
                return response.replaceFirst("(.+)\\.+", "$1");
            }
            System.out.printf("The examples of a statement:%n" +
                    " - It can fly%n - It has horn%n - It is a mammal%n");
        }
    }

    @Override
    public String getQuestion(String statement) {
        String question = statement.startsWith("it has")
                ? statement.replaceFirst("it has", "does it have")
                : statement.replaceFirst("it (can|is)", "$1 it");
        return capitalize(question) + "?";
    }

    @Override
    public String capitalize(String sentence) {
        return sentence.substring(0, 1).toUpperCase() + sentence.substring(1).toLowerCase();
    }

    public String getLearnedPhrase(String learnPos, String learnNeg, String question) {
        return "I have learned the following facts about animals:\n" +
                "- " + learnPos + "\n" +
                "- " + learnNeg + "\n" +
                "I can distinguish these animals by asking the question:\n" +
                "- " + question;
    }

    @Override
    public String toAnimalFact(String fact, String animal) {
        return fact.replaceFirst("it", animal.replaceFirst("an?", "The")) + ".";
    }

    @Override
    public String toNegative(String statement) {
        for (Map.Entry<String, String> element : POS_TO_NEG.entrySet()) {
            if (statement.startsWith(element.getKey())) {
                return statement.replace(element.getKey(), element.getValue());
            }
        }
        return statement;
    }

    @Override
    public String getPositiveFactFromQuestion(String str) {
        if (str.startsWith("Does it have")) {
            str = str.replaceFirst("Does it have", "It has");
            return str.substring(0, str.length() - 1) ;
        } else if (str.startsWith("Can it")) {
            str = str.replaceFirst("Can it", "It can");
            return str.substring(0, str.length() - 1) ;
        } else if(str.startsWith("Is it")) {
            str = str.replaceFirst("Is it", "It is");
            return str.substring(0, str.length() - 1) ;
        }
        return str;
    }

    @Override
    public String getNegativeFactFromQuestion(String str) {
        if (str.startsWith("Does it have")) {
            str = str.replaceFirst("Does it have", "It doesn't have");
        } else if (str.startsWith("Can it")) {
            str = str.replaceFirst("Can it", "It can't");
        } else {
            str = str.replaceFirst("Is it", "It isn't");
        }
        return str.substring(0, str.length() - 1) + ".";
    }
}
