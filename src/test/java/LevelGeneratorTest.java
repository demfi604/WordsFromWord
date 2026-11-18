import org.example.GameModels.Config;
import org.example.GameModels.Level;
import org.example.GameModels.LevelsOutput;
import org.example.LevelService;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
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

    private String formatWords(LevelsOutput out) {
        if (out.levels.isEmpty()) return "[]";
        return "[" + out.levels.get(0).subWords.stream()
                .map(sw -> sw.text)
                .collect(Collectors.joining(", ")) + "]";
    }

    @Test
    public void test1_RepeatedLettersInMainWord() {
        LevelService service = new LevelService();
        LevelsOutput out = service.buildLevels(
                Arrays.asList("рама", "нора", "пар"),
                Collections.singletonList("ПАНОРАМА"),
                createConfig(3, 1)
        );
        System.out.println("[Тест 1] Повторяющиеся буквы (ПАНОРАМА): найдено -> " + formatWords(out));
        assertEquals(1, out.levels.size());
    }

    @Test
    public void test2_ExceedingLetterFrequency() {
        LevelService service = new LevelService();
        LevelsOutput out = service.buildLevels(
                Arrays.asList("мама", "десант"),
                Collections.singletonList("ДЕМОНСТРАЦИЯ"),
                createConfig(3, 1)
        );
        System.out.println("[Тест 2] Превышение кратности букв ('мама' отсеяно): найдено -> " + formatWords(out));
        assertEquals(1, out.levels.get(0).subWords.size());
    }

    @Test
    public void test3_MainWordExcludedFromSubWords() {
        LevelService service = new LevelService();
        LevelsOutput out = service.buildLevels(
                Arrays.asList("ДЕМОНСТРАЦИЯ", "десант", "адрес"),
                Collections.singletonList("ДЕМОНСТРАЦИЯ"),
                createConfig(3, 1)
        );
        System.out.println("[Тест 3] Исключение главного слова из подслов: найдено -> " + formatWords(out));
        assertEquals(2, out.levels.get(0).subWords.size());
    }

    @Test
    public void test4_DuplicateWordsInDictionary() {
        LevelService service = new LevelService();
        LevelsOutput out = service.buildLevels(
                Arrays.asList("десант", "ДЕСАНТ", "  десант ", "адрес"),
                Collections.singletonList("ДЕМОНСТРАЦИЯ"),
                createConfig(3, 1)
        );
        System.out.println("[Тест 4] Удаление дубликатов из словаря: найдено -> " + formatWords(out));
        assertEquals(2, out.levels.get(0).subWords.size());
    }

    @Test
    public void test5_ShortWordsFiltered() {
        LevelService service = new LevelService();
        LevelsOutput out = service.buildLevels(
                Arrays.asList("ад", "он", "сон", "нос"),
                Collections.singletonList("ДЕМОНСТРАЦИЯ"),
                createConfig(3, 1)
        );
        System.out.println("[Тест 5] Отсев коротких слов (< 3 букв): найдено -> " + formatWords(out));
        assertEquals(2, out.levels.get(0).subWords.size());
    }

    @Test
    public void test6_CaseInsensitivityAndLetterYo() {
        LevelService service = new LevelService();
        LevelsOutput out = service.buildLevels(
                Arrays.asList("тёс", "МЁД", "МоСт"),
                Collections.singletonList("МЁДОТЁС"),
                createConfig(3, 1)
        );
        System.out.println("[Тест 6] Разный регистр и буква Ё (МЁДОТЁС): найдено -> " + formatWords(out));
        assertEquals(3, out.levels.get(0).subWords.size());
    }

    @Test
    public void test7_NoMatchingSubWords() {
        LevelService service = new LevelService();
        LevelsOutput out = service.buildLevels(
                Arrays.asList("арбуз", "телефон"),
                Collections.singletonList("КОТОК"),
                createConfig(3, 1)
        );
        System.out.println("[Тест 7] Словарь без подходящих подслов: создано уровней -> " + out.levels.size());
        assertTrue(out.levels.isEmpty());
    }

    @Test
    public void test8_ExactMinWordsToPass() {
        LevelService service = new LevelService();
        LevelsOutput out = service.buildLevels(
                Arrays.asList("кот", "ток", "лишнее"),
                Collections.singletonList("КОТОК"),
                createConfig(3, 2)
        );
        Level lvl = out.levels.get(0);
        System.out.println("[Тест 8] Ровно minWordsToPass (2 слова): найдено -> " + formatWords(out) + ", сложность -> " + lvl.difficulty);
        assertEquals(2, lvl.subWords.size());
    }

    @Test
    public void test9_LargeDictionaryPerformance() {
        LevelService service = new LevelService();
        List<String> bigDict = new ArrayList<>(100_003);
        for (int i = 0; i < 100_000; i++) {
            bigDict.add("слово" + i);
        }
        bigDict.addAll(Arrays.asList("кот", "ток", "кто"));

        long start = System.currentTimeMillis();
        LevelsOutput out = service.buildLevels(bigDict, Collections.singletonList("КОТЁНОК"), createConfig(3, 1));
        long timeMs = System.currentTimeMillis() - start;

        System.out.println("[Тест 9] Производительность (" + bigDict.size() + " слова): время -> " + timeMs + " мс, найдено -> " + formatWords(out));
        assertEquals(3, out.levels.get(0).subWords.size());
    }
}
