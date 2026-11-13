package org.example.model;

import java.util.List;

public class Level {
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