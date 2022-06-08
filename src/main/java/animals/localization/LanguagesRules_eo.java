package animals.localization;

import java.util.Scanner;

public class LanguagesRules_eo implements LanguageRule {
    private static final Scanner SCANNER = new Scanner(System.in);

    @Override
    public String getAnimal(String animal) {
        return animal;
    }

    @Override
    public String getStatement(String first, String second) {
        while (true) {
            System.out.printf("Precizu fakton kiu distingas %s de %s.%n" +
                    "La frazo devas kontentigi unu el la jenaj ŝablonoj:%n" +
                    "- Ĝi povas ...%n- Ĝi havas ...%n- Ĝi estas...%n", first, second);
            String response = SCANNER.nextLine().toLowerCase().trim();
            if (response.startsWith("ĝi ")) {
                return response.replaceFirst("(.+)\\.+", "$1");
            }
            System.out.println("La ekzemploj de aserto:\nĜi povas flugi\nĜi havas kornojn\nĜi estas mamulo");
        }
    }

    @Override
    public String getQuestion(String statement) {
        return String.format("Ĉu %s?", statement.toLowerCase());
    }

    @Override
    public String capitalize(String sentence) {
        return sentence.substring(0, 1).toUpperCase() + sentence.substring(1).toLowerCase();
    }

    @Override
    public String getLearnedPhrase(String learnPos, String learnNeg, String question) {
        return "Mi lernis la jenajn faktojn pri bestoj:\n" +
                "- " + learnPos + "\n" +
                "- " + learnNeg + "\n" +
                "Mi povas distingi ĉi tiujn bestojn farante la demandon:\n" +
                "- " + question;
    }

    @Override
    public String toAnimalFact(String fact, String animal) {
        return fact.replaceFirst("Ĝi", "La") + ".";
    }

    @Override
    public String toNegative(String statement) {
        return statement.replaceFirst("ĝi", "ĝi ne");
    }

    @Override
    public String getPositiveFactFromQuestion(String str) {
        if (str.startsWith("Ĉu ĝi"))
            return str.replaceFirst("Ĉu ĝi", "Ĝi");
        else {
            return str.replaceFirst("Ĉu ", "Ĝi");
        }
    }

    @Override
    public String getNegativeFactFromQuestion(String str) {
        if (str.startsWith("Ĉu ĝi"))
            return str.replaceFirst("Ĉu ĝi", "Ĝi ne");
        else {
            return str.replaceFirst("Ĉu ", "Ĝi ne");
        }
    }
}
