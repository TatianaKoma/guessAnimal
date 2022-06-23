package animals.localization;

import java.util.Map;

public class LanguagesRulesEn implements LanguageRule {
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
    public String getPositiveFactFromQuestion(String question) {
        if (question.startsWith("Does it have")) {
            question = question.replaceFirst("Does it have", "It has");
            return question.substring(0, question.length() - 1);
        } else if (question.startsWith("Can it")) {
            question = question.replaceFirst("Can it", "It can");
            return question.substring(0, question.length() - 1);
        } else if (question.startsWith("Is it")) {
            question = question.replaceFirst("Is it", "It is");
            return question.substring(0, question.length() - 1);
        }
        return question;
    }

    @Override
    public String getNegativeFactFromQuestion(String question) {
        if (question.startsWith("Does it have")) {
            question = question.replaceFirst("Does it have", "It doesn't have");
        } else if (question.startsWith("Can it")) {
            question = question.replaceFirst("Can it", "It can't");
        } else {
            question = question.replaceFirst("Is it", "It isn't");
        }
        return question.substring(0, question.length() - 1) + ".";
    }
}
