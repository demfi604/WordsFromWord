package org.example;

import java.util.List;

public class GameModels {

    public static class Config {
        public int minSubWordLength;
        public int minWordsToPass;
        public DifficultyThresholds difficultyThresholds;

        public static class DifficultyThresholds {
            public int easyMaxMainWordLength;
            public int mediumMaxMainWordLength;
            public int easyMaxSubWords;
            public int mediumMaxSubWords;
        }
    }

    public static class SubWord {
        public String text;
        public String hint;

        public SubWord(String text, String hint) {
            this.text = text;
            this.hint = hint;
        }
    }

    public static class Level {
        public int id;
        public String mainWord;
        public String difficulty;
        public int minWordsToPass;
        public List<SubWord> subWords;

        public Level(int id, String mainWord, String difficulty, int minWordsToPass, List<SubWord> subWords) {
            this.id = id;
            this.mainWord = mainWord;
            this.difficulty = difficulty;
            this.minWordsToPass = minWordsToPass;
            this.subWords = subWords;
        }
    }

    public static class LevelsOutput {
        public List<Level> levels;

        public LevelsOutput(List<Level> levels) {
            this.levels = levels;
        }
    }
}
