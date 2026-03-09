package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleDictionaryTest {

    private WordleDictionary dictionary;
    private List<String> words;
    private PrintWriter logWriter;

    @BeforeEach
    void setUp() {
        words = Arrays.asList("мелок", "молот", "метла", "миска", "мука");
        logWriter = new PrintWriter(System.out);
        dictionary = new WordleDictionary(words, logWriter);
    }

    @Test
    void shouldReturnRandomWord() {
        String randomWord = dictionary.getRandomWord();

        assertNotNull(randomWord);
        assertTrue(words.contains(randomWord));
    }

    @Test
    void shouldReturnFirstWordWhenRandomFails() {
        WordleDictionary emptyDict = new WordleDictionary(Arrays.asList("первый"), logWriter);

        String word = emptyDict.getRandomWord();

        assertEquals("первый", word);
    }

    @Test
    void shouldValidateCorrectWord() {
        String validWord = "мелок";

        boolean isValid = dictionary.isValidWord(validWord);

        assertTrue(isValid);
    }

    @Test
    void shouldRejectInvalidWord() {
        String invalidWord = "несуществующее";

        boolean isValid = dictionary.isValidWord(invalidWord);

        assertFalse(isValid);
    }

    @Test
    void shouldRejectWordWithWrongLength() {
        String tooShort = "дом";
        String tooLong = "длинное";

        assertFalse(dictionary.isValidWord(tooShort));
        assertFalse(dictionary.isValidWord(tooLong));
    }

    @Test
    void shouldValidateWordWithYo() {
        dictionary = new WordleDictionary(Arrays.asList("ежик", "елка", "мелок"), logWriter);

        boolean isValid = dictionary.isValidWord("ёжик");

        assertTrue(isValid);
    }

    @Test
    void shouldHandleNullInIsValidWord() {
        assertFalse(dictionary.isValidWord(null));
    }

    @Test
    void shouldGetWords() {
        List<String> result = dictionary.getWords();

        assertEquals(words, result);
    }
}