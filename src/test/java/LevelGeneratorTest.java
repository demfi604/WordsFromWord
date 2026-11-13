import org.example.model.Config;
import org.example.model.LevelsOutput;
import org.example.service.LevelGenerator;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

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

    @Test
    public void testLetterFrequenciesAndExclusions() {
        LevelGenerator gen = new LevelGenerator();
        LevelsOutput out = gen.generate(
                Arrays.asList("мама", "десант", "адрес", "ДЕМОНСТРАЦИЯ", "ад", "десант"),
                Collections.singletonList("ДЕМОНСТРАЦИЯ"),
                createConfig(3, 2)
        );
        assertEquals(1, out.levels.size());
        assertEquals(2, out.levels.get(0).subWords.size());
    }

    @Test
    public void testRussianLetterYo() {
        LevelGenerator gen = new LevelGenerator();
        LevelsOutput out = gen.generate(
                Arrays.asList("тёс", "мёд", "мост"),
                Collections.singletonList("МЁДОТЁС"),
                createConfig(3, 2)
        );
        assertEquals(1, out.levels.size());
        assertEquals(3, out.levels.get(0).subWords.size());
    }

    @Test
    public void testMinWordsToPassThreshold() {
        LevelGenerator gen = new LevelGenerator();
        LevelsOutput out = gen.generate(
                Arrays.asList("сон", "арбуз"),
                Collections.singletonList("СОНАТА"),
                createConfig(3, 2)
        );
        assertTrue(out.levels.isEmpty());
    }

    @Test
    public void testDifficultyLevels() {
        LevelGenerator gen = new LevelGenerator();
        LevelsOutput out = gen.generate(
                Arrays.asList("кот", "ток", "кто"),
                Collections.singletonList("КОТОК"),
                createConfig(3, 2)
        );
        assertEquals("EASY", out.levels.get(0).difficulty);
    }
}