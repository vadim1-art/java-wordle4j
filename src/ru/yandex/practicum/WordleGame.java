package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class WordleGame {
    private String answer;
    private int steps;
    private WordleDictionary dictionary;
    private List<String> attempts;
    private static List<String> results;
    private boolean gameWon;

    public WordleGame(String answer, int steps, WordleDictionary dictionary) {
        this.answer = answer;
        this.steps = steps;
        this.dictionary = dictionary;
        this.attempts = new ArrayList<>();
        this.results = new ArrayList<>();
        this.gameWon = false;
    }

    public static String hintWord(String answer, String secretWord, List<String> dictionary) {
        return dictionary.stream()
                .filter(word -> IntStream.range(0, 5)
                        .filter(i -> secretWord.charAt(i) == '-')
                        .allMatch(i -> word.charAt(i) == answer.charAt(i)))
                .findFirst()
                .orElse(null);
    }

    public String wordTypeChanges(String word) {
        if (word.length() != 5) {
            throw new IllegalArgumentException("Слово должно быть длиной 5 символов");
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            char c = word.charAt(i);
            if (c == answer.charAt(i)) {
                result.append("+");
            } else if (answer.indexOf(c) != -1) {
                result.append("^");
            } else {
                result.append("-");
            }
        }
        return result.toString();
    }

    public void makeAttempt(String word, String result) {
        attempts.add(word);
        results.add(result);
        steps--;

        if (word.equals(answer)) {
            gameWon = true;
        }
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public String getAnswer() {
        return answer;
    }

    public int getSteps() {
        return steps;
    }

    public WordleDictionary getDictionary() {
        return dictionary;
    }

    public List<String> getAttempts() {
        return new ArrayList<>(attempts);
    }

    public List<String> getResults() {
        return new ArrayList<>(results);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Попыток осталось: ").append(steps).append("\n");

        if (!attempts.isEmpty()) {
            sb.append("Ваши попытки:\n");
            for (int i = 0; i < attempts.size(); i++) {
                sb.append(i + 1).append(". ").append(attempts.get(i))
                        .append(" - ").append(results.get(i)).append("\n");
            }
        }

        return sb.toString();
    }
}