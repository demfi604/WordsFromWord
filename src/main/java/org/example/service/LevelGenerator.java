package org.example.service;

import org.example.model.Config;
import org.example.model.Level;
import org.example.model.LevelsOutput;
import org.example.model.SubWord;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class LevelGenerator {

    public LevelsOutput generate(List<String> dictionary, List<String> mainWords, Config config) {
        if (dictionary == null || mainWords == null || config == null) {
            return new LevelsOutput(Collections.emptyList());
        }

        List<String> cleanDictionary = dictionary.stream()
                .filter(Objects::nonNull)
                .map(WordUtils::normalize)
                .filter(w -> !w.isEmpty())
                .distinct()
                .collect(Collectors.toList());

        AtomicInteger idCounter = new AtomicInteger(1);
        List<Level> generatedLevels = new ArrayList<>();

        List<String> uniqueMainWords = mainWords.stream()
                .filter(Objects::nonNull)
                .map(WordUtils::normalize)
                .filter(w -> !w.isEmpty())
                .distinct()
                .collect(Collectors.toList());

        for (String mainWord : uniqueMainWords) {
            Map<Character, Integer> mainWordFreq = WordUtils.getLetterFrequencies(mainWord);

            List<SubWord> validSubWords = cleanDictionary.stream()
                    .filter(word -> word.length() >= config.minSubWordLength)
                    .filter(word -> !word.equals(mainWord))
                    .filter(word -> WordUtils.canFormWord(word, mainWordFreq))
                    .map(word -> new SubWord(word, ""))
                    .collect(Collectors.toList());

            if (validSubWords.size() < config.minWordsToPass) {
                continue;
            }

            String difficulty = calculateDifficulty(mainWord.length(), validSubWords.size(), config.difficultyThresholds);

            Level level = new Level(
                    idCounter.getAndIncrement(),
                    mainWord,
                    difficulty,
                    config.minWordsToPass,
                    validSubWords
            );
            generatedLevels.add(level);
        }

        return new LevelsOutput(generatedLevels);
    }

    private String calculateDifficulty(int mainWordLength, int subWordsCount, Config.DifficultyThresholds thresholds) {
        int mainWordRank = getRank(mainWordLength, thresholds.easyMaxMainWordLength, thresholds.mediumMaxMainWordLength);
        int subWordsRank = getRank(subWordsCount, thresholds.easyMaxSubWords, thresholds.mediumMaxSubWords);

        int finalRank = Math.max(mainWordRank, subWordsRank);
        switch (finalRank) {
            case 1: return "EASY";
            case 2: return "MEDIUM";
            default: return "HARD";
        }
    }

    private int getRank(int value, int easyMax, int mediumMax) {
        if (value <= easyMax) return 1;
        if (value <= mediumMax) return 2;
        return 3;
    }
}