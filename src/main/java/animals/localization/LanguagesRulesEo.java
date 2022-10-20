package animals.localization;

public class LanguagesRulesEo implements LanguageRule {

    @Override
    public String getAnimal(String animal) {
        return animal;
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
    public String getPositiveFactFromQuestion(String question) {
        if (question.startsWith("Ĉu ĝi"))
            return question.replaceFirst("Ĉu ĝi", "Ĝi");
        else {
            return question.replaceFirst("Ĉu ", "Ĝi");
        }
    }

    @Override
    public String getNegativeFactFromQuestion(String question) {
        if (question.startsWith("Ĉu ĝi"))
            return question.replaceFirst("Ĉu ĝi", "Ĝi ne");
        else {
            return question.replaceFirst("Ĉu ", "Ĝi ne");
        }
    }
}
