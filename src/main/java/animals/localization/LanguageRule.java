package animals.localization;

public interface LanguageRule {
    String getAnimal(String animal);

    String getQuestion(String statement);

    String capitalize(String sentence);

    String getLearnedPhrase(String learnPos, String learnNeg, String question);

    String toAnimalFact(String fact, String animal);

    String toNegative(String statement);

    String getPositiveFactFromQuestion(String question);

    String getNegativeFactFromQuestion(String question);

}
