package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.GameModels.Config;
import org.example.GameModels.LevelsOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        LevelService service = new LevelService();
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

        Config config = service.readConfig("input/config.json");
        List<String> dictionary = service.readWords("input/dictionary.json");
        List<String> mainWords = service.readWords("input/main_words.json");

        long start = System.currentTimeMillis();
        LevelsOutput result = service.buildLevels(dictionary, mainWords, config);
        long end = System.currentTimeMillis();

        System.out.println(gson.toJson(result));
        System.out.println("Время генерации: " + (end - start) + " мс");

        service.writeOutput("output/levels.json", result);

        runPerformanceTest(service, config);
    }

    private static void runPerformanceTest(LevelService service, Config config) {
        List<String> bigDictionary = new ArrayList<>(100_000);
        for (int i = 0; i < 100_000; i++) {
            bigDictionary.add("слово" + i);
        }
        bigDictionary.addAll(Arrays.asList("кот", "ток", "кто", "окно"));

        long start = System.currentTimeMillis();
        LevelsOutput out = service.buildLevels(bigDictionary, Collections.singletonList("КОТЁНОК"), config);
        long end = System.currentTimeMillis();

//        List<String> manyMainWords = new ArrayList<>(1000);
//        for (int i = 0; i < 1000; i++) {
//            manyMainWords.add("КОТЁНОК" + i);
//        }
//
//        long start = System.currentTimeMillis();
//        LevelsOutput out = generator.generate(bigDictionary, manyMainWords, config);
//        long end = System.currentTimeMillis();

        System.out.println("\n=== ТЕСТ ПРОИЗВОДИТЕЛЬНОСТИ ===");
        System.out.println("Слов в словаре: " + bigDictionary.size());
        System.out.println("Время выполнения: " + (end - start) + " мс");
        System.out.println("Сгенерировано уровней: " + out.levels.size());
    }
}


//        List<String> manyMainWords = new ArrayList<>(1000);
//        for (int i = 0; i < 1000; i++) {
//            manyMainWords.add("КОТЁНОК" + i);
//        }
//
//        long start = System.currentTimeMillis();
//        LevelsOutput out = generator.generate(bigDictionary, manyMainWords, config);
//        long end = System.currentTimeMillis();

