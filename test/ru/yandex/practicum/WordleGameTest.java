package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class WordleGameTest {

    private WordleDictionary dictionary;
    private WordleGame game;
    private PrintWriter testLogger;

    @BeforeEach
    void setUp() {
        testLogger = new PrintWriter(System.out, true);

        List<String> testWords = Arrays.asList(
                "герой", "книга", "столб", "трава", "молот",
                "город", "берег", "ветер", "солод", "полет",
                "кошка", "собака", "мышка", "птица"
        );
        dictionary = new WordleDictionary(testWords);
    }

    @Test
    void testWordleGameInitialization() {
        game = new WordleGame(dictionary, testLogger);

        org.junit.jupiter.api.Assertions.assertNotNull(game);
        org.junit.jupiter.api.Assertions.assertEquals(6, game.getStepsLeft());
        org.junit.jupiter.api.Assertions.assertFalse(game.isGameOver());
        org.junit.jupiter.api.Assertions.assertFalse(game.isGameWon());
        org.junit.jupiter.api.Assertions.assertNotNull(game.getAnswer());
        org.junit.jupiter.api.Assertions.assertEquals(5, game.getAnswer().length());
    }

    @Test
    void testMakeGuessWithValidWord() throws WordNotFoundInDictionaryException {
        game = new WordleGame(dictionary, testLogger);
        String answer = game.getAnswer();

        WordleGame.GuessResult result = game.makeGuess(answer);

        org.junit.jupiter.api.Assertions.assertNotNull(result);
        org.junit.jupiter.api.Assertions.assertEquals("+++++", result.getPattern());
        org.junit.jupiter.api.Assertions.assertTrue(result.isGameWon());
        org.junit.jupiter.api.Assertions.assertTrue(game.isGameWon());
        org.junit.jupiter.api.Assertions.assertTrue(game.isGameOver());
    }

    @Test
    void testMakeGuessWithInvalidWord() {
        game = new WordleGame(dictionary, testLogger);

        org.junit.jupiter.api.Assertions.assertThrows(WordNotFoundInDictionaryException.class, () -> {
            game.makeGuess("абвгд");
        });
    }

    @Test
    void testStepsDecrement() throws WordNotFoundInDictionaryException {
        game = new WordleGame(dictionary, testLogger);
        int initialSteps = game.getStepsLeft();

        game.makeGuess("герой");

        org.junit.jupiter.api.Assertions.assertEquals(initialSteps - 1, game.getStepsLeft());
    }

    @Test
    void testGameOverAfterSixGuesses() throws WordNotFoundInDictionaryException {
        game = new WordleGame(dictionary, testLogger);

        for (int i = 0; i < 6; i++) {
            org.junit.jupiter.api.Assertions.assertFalse(game.isGameOver());
            game.makeGuess("герой");
        }

        org.junit.jupiter.api.Assertions.assertTrue(game.isGameOver());
        org.junit.jupiter.api.Assertions.assertEquals(0, game.getStepsLeft());
    }

    @Test
    void testGetHint() {
        game = new WordleGame(dictionary, testLogger);

        String hint = game.getHint();

        org.junit.jupiter.api.Assertions.assertNotNull(hint);
        org.junit.jupiter.api.Assertions.assertEquals(5, hint.length());
        org.junit.jupiter.api.Assertions.assertTrue(dictionary.contains(hint));
    }

    @Test
    void testGuessResultPattern() throws WordNotFoundInDictionaryException {
        game = new WordleGame(dictionary, testLogger);

        WordleGame.GuessResult result = game.makeGuess("герой");

        org.junit.jupiter.api.Assertions.assertNotNull(result.getPattern());
        org.junit.jupiter.api.Assertions.assertEquals(5, result.getPattern().length());
    }

    @Test
    void testLetterSetsUpdate() throws WordNotFoundInDictionaryException {
        game = new WordleGame(dictionary, testLogger);

        game.makeGuess("герой");

        String hint = game.getHint();
        org.junit.jupiter.api.Assertions.assertNotNull(hint);
    }
}