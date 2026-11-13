import org.example.model.Config;
import org.example.model.LevelsOutput;
import org.example.model.SubWord;
import org.example.service.LevelGenerator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class LevelGeneratorTest {

    private Config createConfig(int minLen, int minPass) {
        Config config = new Config();
        config.minSubWordLength = minLen;
        config.minWordsToPass = minPass;

        Config.DifficultyThresholds t = new Config.DifficultyThresholds();
        t.easyMaxMainWordLength = 7;
        t.mediumMaxMainWordLength = 10;
        t.easyMaxSubWords = 15;
        t.mediumMaxSubWords = 30;
        config.difficultyThresholds = t;

        return config;
    }

    private String formatSubWords(LevelsOutput out) {
        if (out.levels.isEmpty()) return "[]";
        return out.levels.get(0).subWords.stream()
                .map(s -> s.text)
                .collect(Collectors.toList())
                .toString();
    }

    // 1. Главное слово с повторяющимися буквами
    @Test
    public void test1_MainWordWithRepeatedLetters() {
        LevelGenerator gen = new LevelGenerator();
        LevelsOutput out = gen.generate(
                Arrays.asList("мама", "рама", "марс"),
                Collections.singletonList("ПАНОРАМА"),
                createConfig(3, 1)
        );
        System.out.println("[Тест 1] Повторяющиеся буквы (ПАНОРАМА): найдено -> " + formatSubWords(out));
        assertEquals(1, out.levels.size());
        assertEquals("РАМА", out.levels.get(0).subWords.get(0).text);
    }

    // 2. Подслово использует букву больше раз, чем есть в главном слове
    @Test
    public void test2_SubWordExceedsLetterFrequency() {
        LevelGenerator gen = new LevelGenerator();
        LevelsOutput out = gen.generate(
                Arrays.asList("мама", "десант"),
                Collections.singletonList("ДЕМОНСТРАЦИЯ"),
                createConfig(3, 1)
        );
        System.out.println("[Тест 2] Превышение кратности букв ('мама' отсеяно): найдено -> " + formatSubWords(out));
        assertEquals(1, out.levels.size());
        assertEquals(1, out.levels.get(0).subWords.size());
        assertEquals("ДЕСАНТ", out.levels.get(0).subWords.get(0).text);
    }

    // 3. Главное слово само присутствует в словаре
    @Test
    public void test3_MainWordInDictionaryExcluded() {
        LevelGenerator gen = new LevelGenerator();
        LevelsOutput out = gen.generate(
                Arrays.asList("ДЕМОНСТРАЦИЯ", "десант", "адрес"),
                Collections.singletonList("ДЕМОНСТРАЦИЯ"),
                createConfig(3, 2)
        );
        System.out.println("[Тест 3] Исключение главного слова из подслов: найдено -> " + formatSubWords(out));
        assertEquals(1, out.levels.size());
        assertEquals(2, out.levels.get(0).subWords.size());
    }

    // 4. Словарь содержит дубликаты
    @Test
    public void test4_DictionaryContainsDuplicates() {
        LevelGenerator gen = new LevelGenerator();
        LevelsOutput out = gen.generate(
                Arrays.asList("десант", "десант", "ДЕСАНТ", "адрес", "адрес"),
                Collections.singletonList("ДЕМОНСТРАЦИЯ"),
                createConfig(3, 2)
        );
        System.out.println("[Тест 4] Удаление дубликатов из словаря: найдено -> " + formatSubWords(out));
        assertEquals(1, out.levels.size());
        assertEquals(2, out.levels.get(0).subWords.size());
    }

    // 5. Слова короче minSubWordLength
    @Test
    public void test5_WordsShorterThanMinLengthExcluded() {
        LevelGenerator gen = new LevelGenerator();
        LevelsOutput out = gen.generate(
                Arrays.asList("ад", "он", "сон", "нос"),
                Collections.singletonList("ДЕМОНСТРАЦИЯ"),
                createConfig(3, 2)
        );
        System.out.println("[Тест 5] Отсев коротких слов (< 3 букв): найдено -> " + formatSubWords(out));
        assertEquals(1, out.levels.size());
        assertEquals(2, out.levels.get(0).subWords.size());
    }

    // 6. Разный регистр и буква Ё
    @Test
    public void test6_MixedCaseAndRussianLetterYo() {
        LevelGenerator gen = new LevelGenerator();
        LevelsOutput out = gen.generate(
                Arrays.asList("ТёС", "мЁд", "МоСт"),
                Collections.singletonList("мёдотёс"),
                createConfig(3, 2)
        );
        System.out.println("[Тест 6] Разный регистр и буква Ё (" + out.levels.get(0).mainWord + "): найдено -> " + formatSubWords(out));
        assertEquals(1, out.levels.size());
        assertEquals("МЁДОТЁС", out.levels.get(0).mainWord);
        assertEquals(3, out.levels.get(0).subWords.size());
    }

    // 7. Словарь без подходящих подслов
    @Test
    public void test7_DictionaryWithoutMatchingSubWords() {
        LevelGenerator gen = new LevelGenerator();
        LevelsOutput out = gen.generate(
                Arrays.asList("яблоко", "груша", "банан"),
                Collections.singletonList("КОТОК"),
                createConfig(3, 1)
        );
        System.out.println("[Тест 7] Словарь без подходящих слов: создано уровней -> " + out.levels.size());
        assertTrue(out.levels.isEmpty());
    }

    // 8. Ровно minWordsToPass подслов (граничное условие)
    @Test
    public void test8_ExactMinWordsToPassThreshold() {
        LevelGenerator gen = new LevelGenerator();
        LevelsOutput out = gen.generate(
                Arrays.asList("кот", "ток", "арбуз"),
                Collections.singletonList("КОТОК"),
                createConfig(3, 2)
        );
        System.out.println("[Тест 8] Ровно minWordsToPass (2 слова): найдено -> " + formatSubWords(out) + ", сложность -> " + out.levels.get(0).difficulty);
        assertEquals(1, out.levels.size());
        assertEquals(2, out.levels.get(0).subWords.size());
    }

    // 9. Большой словарь — проверка производительности
    @Test
    public void test9_LargeDictionaryPerformance() {
        LevelGenerator gen = new LevelGenerator();
        List<String> bigDictionary = new ArrayList<>(100_000);
        for (int i = 0; i < 100_000; i++) {
            bigDictionary.add("слово" + i);
        }
        bigDictionary.addAll(Arrays.asList("кот", "ток", "кто"));

        long start = System.currentTimeMillis();
        LevelsOutput out = gen.generate(bigDictionary, Collections.singletonList("КОТОК"), createConfig(3, 2));
        long duration = System.currentTimeMillis() - start;

        System.out.println("[Тест 9] Производительность (100 003 слова): время -> " + duration + " мс, найдено -> " + formatSubWords(out));
        assertEquals(1, out.levels.size());
        assertEquals(3, out.levels.get(0).subWords.size());
        assertTrue("Обработка 100 000 слов должна занимать менее 2 секунд", duration < 2000);
    }
}
