package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleGameTest {

    private WordleGame game;
    private WordleDictionary dictionary;
    private String answer;

    @BeforeEach
    void setUp() {
        answer = "мелок";
        List<String> words = Arrays.asList("мелок", "молот", "метла", "миска", "мука");
        dictionary = new WordleDictionary(words);
        game = new WordleGame(answer, 6, dictionary);
    }

    @Test
    void shouldCreateGameWithCorrectInitialState() {
        assertEquals(answer, game.getAnswer());
        assertEquals(6, game.getSteps());
        assertEquals(dictionary, game.getDictionary());
        assertFalse(game.isGameWon());
        assertTrue(game.getAttempts().isEmpty());
        assertTrue(game.getResults().isEmpty());
    }

    @Test
    void shouldWordTypeChangesReturnCorrectPattern() {
        String result = game.wordTypeChanges("молот");
        assertEquals("+--^+", result);
    }

    @Test
    void shouldMarkAllCorrect() {
        String result = game.wordTypeChanges(answer);
        assertEquals("+++++", result);
    }

    @Test
    void shouldMarkNoMatches() {
        String result = game.wordTypeChanges("рубль");
        assertEquals("-----", result);
    }

    @Test
    void shouldThrowExceptionOnWrongLength() {
        assertThrows(IllegalArgumentException.class, () ->
                game.wordTypeChanges("дом")
        );
    }

    @Test
    void shouldMakeAttemptAndUpdateState() {
        String word = "молот";
        String result = "+--^+";

        game.makeAttempt(word, result);

        assertEquals(5, game.getSteps());
        assertEquals(1, game.getAttempts().size());
        assertEquals(1, game.getResults().size());
        assertEquals(word, game.getAttempts().get(0));
        assertEquals(result, game.getResults().get(0));
        assertFalse(game.isGameWon());
    }

    @Test
    void shouldWinGameWhenGuessCorrect() {
        game.makeAttempt(answer, "+++++");
        assertTrue(game.isGameWon());
    }

    @Test
    void hintWordShouldReturnWordMatchingMinusPositions() {
        String secretWord = "+--^+";
        String hint = WordleGame.hintWord(answer, secretWord, dictionary.getWords());
        assertEquals("миска", hint);
    }

    @Test
    void hintWordShouldReturnNullWhenNoMatch() {
        String secretWord = "+++++";
        String hint = WordleGame.hintWord(answer, secretWord, dictionary.getWords());
        assertNull(hint);
    }

    @Test
    void shouldReturnCorrectToString() {
        game.makeAttempt("молот", "+--^+");
        game.makeAttempt("миска", "+^^+^");

        String toString = game.toString();

        assertTrue(toString.contains("Попыток осталось: 4"));
        assertTrue(toString.contains("1. молот - +--^+"));
        assertTrue(toString.contains("2. миска - +^^+^"));
    }

    @Test
    void shouldReturnEmptyAttemptsListWhenNoAttempts() {
        assertTrue(game.getAttempts().isEmpty());
    }

    @Test
    void shouldReturnEmptyResultsListWhenNoAttempts() {
        assertTrue(game.getResults().isEmpty());
    }
}