package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class WordleDictionary {

    private List<String> words;

    public WordleDictionary(List<String> words) {
        this.words = new ArrayList<>(words);
    }

    public int size() {
        return words.size();
    }

    public List<String> getWords() {
        return new ArrayList<>(words);
    }

    public boolean contains(String word) {
        return words.contains(word);
    }

    public String getRandomWord() {
        if (words.isEmpty()) {
            throw new IllegalStateException("Словарь пуст");
        }
        int randomIndex = (int) (Math.random() * words.size());
        return words.get(randomIndex);
    }

    public List<String> filterWords(Set<Character> correctLetters,
                                    Set<Character> presentLetters,
                                    Set<Character> absentLetters,
                                    List<String> guessedWords) {
        return words.stream()
                .filter(word -> filterByAbsentLetters(word, absentLetters))
                .filter(word -> filterByPresentLetters(word, presentLetters))
                .filter(word -> filterByCorrectPositions(word, guessedWords))
                .collect(Collectors.toList());
    }

    private boolean filterByAbsentLetters(String word, Set<Character> absentLetters) {
        for (char c : word.toCharArray()) {
            if (absentLetters.contains(c)) {
                return false;
            }
        }
        return true;
    }

    private boolean filterByPresentLetters(String word, Set<Character> presentLetters) {
        for (char c : presentLetters) {
            if (!word.contains(String.valueOf(c))) {
                return false;
            }
        }
        return true;
    }

    private boolean filterByCorrectPositions(String word, List<String> guessedWords) {
        for (String guess : guessedWords) {
            for (int i = 0; i < 5; i++) {
                char guessChar = guess.charAt(i);
                if (guessChar != '?' && guessChar != word.charAt(i)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static String generatePattern(String guess, String answer) {
        StringBuilder pattern = new StringBuilder();
        boolean[] answerUsed = new boolean[5];

        for (int i = 0; i < 5; i++) {
            if (guess.charAt(i) == answer.charAt(i)) {
                pattern.append('+');
                answerUsed[i] = true;
            } else {
                pattern.append('?');
            }
        }

        for (int i = 0; i < 5; i++) {
            if (pattern.charAt(i) == '+') continue;

            char guessChar = guess.charAt(i);
            boolean found = false;

            for (int j = 0; j < 5; j++) {
                if (!answerUsed[j] && guessChar == answer.charAt(j)) {
                    found = true;
                    answerUsed[j] = true;
                    break;
                }
            }

            pattern.setCharAt(i, found ? '^' : '-');
        }

        return pattern.toString();
    }

    public static String normalizeWord(String word) {
        return word.toLowerCase().replace('ё', 'е');
    }
}