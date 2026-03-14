package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.*;

public class WordleGame {

    private String answer;
    private int stepsLeft;
    private WordleDictionary dictionary;
    private PrintWriter logger;

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

        String pattern = WordleDictionary.generatePattern(normalizedGuess, answer);
        updateLetterSets(normalizedGuess, pattern);

        if (normalizedGuess.equals(answer)) {
            gameWon = true;
        }

        logger.println(String.format("Ход: %s -> %s (осталось попыток: %d)",
                normalizedGuess, pattern, stepsLeft));

        return new GuessResult(pattern, gameWon);
    }

    public String getHint() {
        List<String> possibleWords = findPossibleWordsWithPositions();

        possibleWords.removeAll(usedHints);

        if (possibleWords.isEmpty()) {
            logger.println("Подсказок больше нет");
            return null;
        }

        String hint = selectBestHintWord(possibleWords);
        usedHints.add(hint);

        logger.println("Подсказка: " + hint);
        logger.println("Осталось возможных слов: " + possibleWords.size());
        return hint;
    }

    private List<String> findPossibleWordsWithPositions() {
        List<String> allWords = dictionary.getWords();
        List<String> possible = new ArrayList<>();

        for (String word : allWords) {
            if (isWordPossible(word)) {
                possible.add(word);
            }
        }
        return possible;
    }

    private boolean isWordPossible(String word) {
        for (int i = 0; i < 5; i++) {
            if (exactPositions.containsKey(i)) {
                if (word.charAt(i) != exactPositions.get(i)) {
                    return false;
                }
            }
        }

        for (char letter : presentLetters) {
            if (word.indexOf(letter) == -1) {
                return false;
            }
        }

        for (char letter : absentLetters) {
            if (word.indexOf(letter) != -1) {
                return false;
            }
        }

        return true;
    }

    private String selectBestHintWord(List<String> possibleWords) {
        if (possibleWords.size() <= 3) {
            return possibleWords.get(0);
        }

        String bestWord = null;
        int maxUniqueLetters = 0;

        for (String word : possibleWords) {
            Set<Character> uniqueLetters = new HashSet<>();
            for (char c : word.toCharArray()) {
                uniqueLetters.add(c);
            }

            if (uniqueLetters.size() > maxUniqueLetters) {
                maxUniqueLetters = uniqueLetters.size();
                bestWord = word;
            }
        }

        return bestWord;
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

    private Map<Integer, Character> exactPositions;

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