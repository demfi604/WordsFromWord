package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.model.Config;
import org.example.model.LevelsOutput;
import org.example.service.LevelGenerator;
import org.example.util.JsonFileHandler;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        LevelGenerator generator = new LevelGenerator();

        // Читаем данные из папки input
        Config config = JsonFileHandler.loadConfig("input/config.json");
        List<String> dictionary = JsonFileHandler.loadStringList("input/dictionary.json");
        List<String> mainWords = JsonFileHandler.loadStringList("input/main_words.json");

        // Генерируем уровни
        LevelsOutput result = generator.generate(dictionary, mainWords, config);

        // Выводим в консоль и сохраняем в папку output
        System.out.println("=== РЕЗУЛЬТАТ ГЕНЕРАЦИИ ===");
        System.out.println(gson.toJson(result));

        Files.createDirectories(Paths.get("output"));
        JsonFileHandler.saveOutput("output/levels.json", result);
        System.out.println("Файл успешно сохранён в output/levels.json");

        runPerformanceTest(generator, config);
    }

    private static void runPerformanceTest(LevelGenerator generator, Config config) {
        List<String> bigDictionary = new ArrayList<>(100_000);
        for (int i = 0; i < 100_000; i++) {
            bigDictionary.add("слово" + i);
        }
        bigDictionary.addAll(Arrays.asList("кот", "ток", "кто", "окно"));

        long start = System.currentTimeMillis();
        LevelsOutput out = generator.generate(bigDictionary, Collections.singletonList("КОТЁНОК"), config);
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