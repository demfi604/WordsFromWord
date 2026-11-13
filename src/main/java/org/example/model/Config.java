package org.example.model;

public class Config {
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