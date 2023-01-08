import java.util.*;

public class CosineSimilarity {
    private final Map<String, Map<String, Integer>> languageWithNGram = new HashMap<>();
    private final Map<String, Double> similarityScoresWithLanguage = new HashMap<>();

    public String cosineSimilarity(String mysteryText, Map<String, String> languageModels, int n) {

        Map<String, Integer> mysteryTextNGram = generateNGrams(mysteryText, n);

        languageModels.entrySet()
                .forEach(languageSet ->
                        languageWithNGram.put(languageSet.getKey(), generateNGrams(String.valueOf(languageSet), n))
                );

        languageWithNGram.keySet()
                .forEach(language -> calculateAndSaveSimilarity(language, mysteryTextNGram)
                );

        return similarityScoresWithLanguage.entrySet()
                .stream()
                .max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1)
                .get()
                .getKey();
    }

    public Map<String, Integer> generateNGrams(String text, int n) {
        Map<String, Integer> nGrams = new HashMap<>();
        for (int i = 0; i < text.length() - n + 1; i++) {
            String nGram = text.substring(i, i + n);
            nGrams.put(nGram, nGrams.getOrDefault(nGram, 0) + 1);
        }
        return nGrams;
    }

    public void calculateAndSaveSimilarity(String language, Map<String, Integer> mysteryTextNGram) {
        double dotProduct = 0;
        for (String nGram : mysteryTextNGram.keySet()) {
            dotProduct += mysteryTextNGram.get(nGram) * languageWithNGram.get(language).getOrDefault(nGram, 0);
        }
        // Calculate the Euclidean norm for both text vectors
        double norm1 = 0;
        for (int count : mysteryTextNGram.values()) {
            norm1 += count * count;
        }
        norm1 = Math.sqrt(norm1);

        double norm2 = 0;
        for (int count : languageWithNGram.get(language).values()) {
            norm2 += count * count;
        }
        norm2 = Math.sqrt(norm2);
        similarityScoresWithLanguage.put(language, dotProduct / (norm1 * norm2));
    }
}