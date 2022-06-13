package animals.localization;

public interface LanguageRule {
    String getAnimal(String animal);

    String getStatement(String first, String second);

    String getQuestion(String statement);

    String capitalize(String sentence);

    String getLearnedPhrase(String learnPos, String learnNeg, String question);

    String toAnimalFact(String fact, String animal);

    String toNegative(String statement);

    String getPositiveFactFromQuestion(String str);

    String getNegativeFactFromQuestion(String str);

}
