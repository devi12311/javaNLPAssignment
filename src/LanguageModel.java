import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.*;

public class LanguageModel {
    private final ConcurrentMap<String, String> languageModels;
    public LanguageModel() {
        languageModels = new ConcurrentHashMap<>();
    }
    public void addLanguage(String language, String folderPath) {
        File[] files = new File(folderPath).listFiles();
        if (files == null) {
            throw new IllegalArgumentException("No files found in " + folderPath);
        }

        List<Future<List<String>>> futures = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(8);

        for (File file : files) {
            if (!file.isDirectory()) {
                // submit a task to read the .txt files in the language subfolder concurrently
                futures.add(executor.submit(() -> Files.lines(file.toPath()).collect(toList())));
            }
        }

        // wait for all tasks to complete and collect the results
        String text = futures.stream().map(future -> {
                    try {
                        return future.get().stream().map(string -> string.replaceAll("[.,!?]*", "").toLowerCase()).collect(joining());
                    } catch (InterruptedException | ExecutionException e) {
                        // handle the exception
                        return Collections.emptyList();
                    }
                })
                .collect(toList()).toString();

        languageModels.put(language, text);
        executor.shutdown();
    }
    public String classifyText(String folderPath) throws IOException, IllegalArgumentException {
        File mysteryFile = new File(folderPath, "mystery.txt");
        CosineSimilarity cosineSimilarity = new CosineSimilarity();
        if (!mysteryFile.exists()) {
            throw new IllegalArgumentException("mystery.txt not found in " + folderPath);
        }

        String mysteryText = Files.lines(mysteryFile.toPath()).collect(toList()).stream().map(string -> string.replaceAll("[.,!?]*", "")).collect(joining());
        return cosineSimilarity.cosineSimilarity(mysteryText, languageModels, 3);
    }
}