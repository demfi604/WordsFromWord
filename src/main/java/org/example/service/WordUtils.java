package org.example.service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class WordUtils {
    private static final Locale RU_LOCALE = new Locale("ru", "RU");

    public static String normalize(String word) {
        if (word == null) return "";
        return word.trim().toUpperCase(RU_LOCALE);
    }

    public static Map<Character, Integer> getLetterFrequencies(String word) {
        Map<Character, Integer> freq = new HashMap<>();
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            freq.put(c, freq.getOrDefault(c, 0) + 1);
        }
        return freq;
    }

    public static boolean canFormWord(String candidate, Map<Character, Integer> mainWordFreq) {
        if (candidate.isEmpty()) return false;

        Map<Character, Integer> candidateFreq = new HashMap<>();
        for (int i = 0; i < candidate.length(); i++) {
            char c = candidate.charAt(i);
            int available = mainWordFreq.getOrDefault(c, 0);
            if (available == 0) {
                return false;
            }
            int used = candidateFreq.getOrDefault(c, 0) + 1;
            if (used > available) {
                return false;
            }
            candidateFreq.put(c, used);
        }
        return true;
    }
}