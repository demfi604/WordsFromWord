package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.example.GameModels.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class LevelService {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public Config readConfig(String path) throws IOException {
        String raw = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
        return gson.fromJson(raw, Config.class);
    }

    public List<String> readWords(String path) throws IOException {
        String raw = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
        return gson.fromJson(raw, new TypeToken<List<String>>() {}.getType());
    }

    public void writeOutput(String path, LevelsOutput output) throws IOException {
        Files.createDirectories(Paths.get(path).getParent());
        Files.write(Paths.get(path), gson.toJson(output).getBytes(StandardCharsets.UTF_8));
    }

    public LevelsOutput buildLevels(List<String> dictionary, List<String> mainWords, Config config) {
        if (dictionary == null || mainWords == null || config == null) {
            return new LevelsOutput(Collections.emptyList());
        }

        List<String> normalizedDict = dictionary.stream()
                .filter(Objects::nonNull)
                .map(WordValidator::cleanWord)
                .filter(w -> !w.isEmpty())
                .distinct()
                .collect(Collectors.toList());

        List<String> normalizedMain = mainWords.stream()
                .filter(Objects::nonNull)
                .map(WordValidator::cleanWord)
                .filter(w -> !w.isEmpty())
                .distinct()
                .collect(Collectors.toList());

        List<Level> levels = new ArrayList<>();
        int nextId = 1;

        for (String mainWord : normalizedMain) {
            Map<Character, Integer> letterPool = WordValidator.countLetters(mainWord);

            List<SubWord> subWords = normalizedDict.stream()
                    .filter(w -> w.length() >= config.minSubWordLength)
                    .filter(w -> !w.equals(mainWord))
                    .filter(w -> WordValidator.isSubWordValid(w, letterPool))
                    .map(w -> new SubWord(w, ""))
                    .collect(Collectors.toList());

            if (subWords.size() >= config.minWordsToPass) {
                String diff = detectDifficulty(mainWord.length(), subWords.size(), config.difficultyThresholds);
                levels.add(new Level(nextId++, mainWord, diff, config.minWordsToPass, subWords));
            }
        }

        return new LevelsOutput(levels);
    }

    private String detectDifficulty(int wordLen, int subCount, Config.DifficultyThresholds t) {
        int r1 = wordLen <= t.easyMaxMainWordLength ? 1 : (wordLen <= t.mediumMaxMainWordLength ? 2 : 3);
        int r2 = subCount <= t.easyMaxSubWords ? 1 : (subCount <= t.mediumMaxSubWords ? 2 : 3);
        int maxRank = Math.max(r1, r2);
        return maxRank == 1 ? "EASY" : (maxRank == 2 ? "MEDIUM" : "HARD");
    }
}
