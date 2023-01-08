import java.util.*;

public class CosineSimilarity {
    private final LanguageModel languageModel = new LanguageModel();

    public void cosineSimilarity(String mysteryText, Map<String, String> languageModels, int n) {

        Map<String, Integer> mysteryTextNGram = languageModel.generateNGrams(mysteryText, n);

        Map<String, Map<String, Integer>> languageWithNGram = new HashMap<>();
        Map<String, Double> similarityScoresWithLanguage = new HashMap<>();

        languageModels.entrySet()
                .forEach(languageSet -> languageWithNGram.put(languageSet.getKey(), languageModel.generateNGrams(String.valueOf(languageSet), n)));

        languageWithNGram.keySet()
                .forEach(stringIntegerMap -> {
                    double dotProduct = 0;
                    for (String nGram : mysteryTextNGram.keySet()) {
                        dotProduct += mysteryTextNGram.get(nGram) * languageWithNGram.get(stringIntegerMap).getOrDefault(nGram, 0);
                    }
                    // Calculate the Euclidean norm for both text vectors
                    double norm1 = 0;
                    for (int count : mysteryTextNGram.values()) {
                        norm1 += count * count;
                    }
                    norm1 = Math.sqrt(norm1);

                    double norm2 = 0;
                    for (int count : languageWithNGram.get(stringIntegerMap).values()) {
                        norm2 += count * count;
                    }
                    norm2 = Math.sqrt(norm2);
                    similarityScoresWithLanguage.put(stringIntegerMap, dotProduct / (norm1 * norm2));
                });

        String mostSimilarLanguage = similarityScoresWithLanguage.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();

        System.out.println(mostSimilarLanguage);
    }
}