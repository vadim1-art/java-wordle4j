package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class WordleGame {

    private String answer;
    private int stepsLeft;
    private WordleDictionary dictionary;
    private PrintWriter logger;

    private List<String> guesses;
    private List<String> guessPatterns;
    private Set<Character> correctLetters;
    private Set<Character> presentLetters;
    private Set<Character> absentLetters;
    private Set<String> usedHints;

    private boolean gameWon;

    public WordleGame(WordleDictionary dictionary, PrintWriter logger) {
        this.dictionary = dictionary;
        this.logger = logger;
        this.answer = dictionary.getRandomWord();
        this.stepsLeft = 6;
        this.guesses = new ArrayList<>();
        this.guessPatterns = new ArrayList<>();
        this.correctLetters = new HashSet<>();
        this.presentLetters = new HashSet<>();
        this.absentLetters = new HashSet<>();
        this.usedHints = new HashSet<>();
        this.gameWon = false;

        logger.println("Новая игра. Загадано слово: " + answer);
    }

    public int getStepsLeft() {
        return stepsLeft;
    }

    public String getAnswer() {
        return answer;
    }

    public boolean isGameOver() {
        return stepsLeft == 0 || gameWon;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public GuessResult makeGuess(String guess) throws WordNotFoundInDictionaryException {
        String normalizedGuess = WordleDictionary.normalizeWord(guess);

        if (!dictionary.contains(normalizedGuess)) {
            throw new WordNotFoundInDictionaryException("Слово \"" + guess + "\" не найдено в словаре");
        }

        stepsLeft--;

        guesses.add(normalizedGuess);

        String pattern = WordleDictionary.generatePattern(normalizedGuess, answer);
        guessPatterns.add(pattern);

        updateLetterSets(normalizedGuess, pattern);

        if (normalizedGuess.equals(answer)) {
            gameWon = true;
        }

        logger.println(String.format("Ход: %s -> %s (осталось попыток: %d)",
                normalizedGuess, pattern, stepsLeft));

        return new GuessResult(pattern, gameWon);
    }

    public String getHint() {
        List<String> possibleWords = dictionary.filterWords(
                correctLetters, presentLetters, absentLetters, getGuessedPatterns());

        possibleWords.removeAll(guesses);
        possibleWords.removeAll(usedHints);

        if (possibleWords.isEmpty()) {
            return null;
        }

        int randomIndex = (int) (Math.random() * possibleWords.size());
        String hint = possibleWords.get(randomIndex);
        usedHints.add(hint);

        logger.println("Подсказка: " + hint);
        return hint;
    }

    private void updateLetterSets(String guess, String pattern) {
        for (int i = 0; i < 5; i++) {
            char letter = guess.charAt(i);
            char mark = pattern.charAt(i);

            if (mark == '+') {
                correctLetters.add(letter);
                presentLetters.add(letter);
                absentLetters.remove(letter);
            } else if (mark == '^') {
                presentLetters.add(letter);
                absentLetters.remove(letter);
            } else if (mark == '-') {
                if (!presentLetters.contains(letter)) {
                    absentLetters.add(letter);
                }
            }
        }
    }

    private List<String> getGuessedPatterns() {
        List<String> patterns = new ArrayList<>();
        for (int i = 0; i < guesses.size(); i++) {
            String guess = guesses.get(i);
            String pattern = guessPatterns.get(i);

            StringBuilder guessPattern = new StringBuilder("?????");
            for (int j = 0; j < 5; j++) {
                if (pattern.charAt(j) == '+') {
                    guessPattern.setCharAt(j, guess.charAt(j));
                }
            }
            patterns.add(guessPattern.toString());
        }
        return patterns;
    }

    public static class GuessResult {
        private final String pattern;
        private final boolean gameWon;

        public GuessResult(String pattern, boolean gameWon) {
            this.pattern = pattern;
            this.gameWon = gameWon;
        }

        public String getPattern() {
            return pattern;
        }

        public boolean isGameWon() {
            return gameWon;
        }
    }
}