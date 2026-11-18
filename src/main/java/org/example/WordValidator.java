package org.example;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class WordValidator {
    private static final Locale RU_LOCALE = new Locale("ru", "RU");

    public static String cleanWord(String word) {
        return word == null ? "" : word.trim().toUpperCase(RU_LOCALE);
    }

    public static Map<Character, Integer> countLetters(String word) {
        Map<Character, Integer> counts = new HashMap<>();
        for (char ch : word.toCharArray()) {
            counts.put(ch, counts.getOrDefault(ch, 0) + 1);
        }
        return counts;
    }

    public static boolean isSubWordValid(String candidate, Map<Character, Integer> sourceCounts) {
        if (candidate.isEmpty()) return false;

        Map<Character, Integer> used = new HashMap<>();
        for (char ch : candidate.toCharArray()) {
            int maxAllowed = sourceCounts.getOrDefault(ch, 0);
            int current = used.getOrDefault(ch, 0) + 1;
            if (current > maxAllowed) {
                return false;
            }
            used.put(ch, current);
        }
        return true;
    }
}
