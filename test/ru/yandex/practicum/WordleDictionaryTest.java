package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleDictionaryTest {

    private WordleDictionary dictionary;
    private List<String> words;

    @BeforeEach
    void setUp() {
        words = Arrays.asList("мелок", "молот", "метла", "миска", "мука");
        dictionary = new WordleDictionary(words);
    }

    @Test
    void shouldReturnRandomWord() {
        String randomWord = dictionary.getRandomWord();

        assertNotNull(randomWord);
        assertTrue(words.contains(randomWord));
    }

    @Test
    void shouldReturnFirstWordWhenRandomFails() {
        WordleDictionary emptyDict = new WordleDictionary(Arrays.asList("первый"));

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
        dictionary = new WordleDictionary(Arrays.asList("ежик", "елка", "мелок"));

        boolean isValid = dictionary.isValidWord("ёжик");

        assertTrue(isValid);
    }

    @Test
    void shouldHandleNullInIsValidWord() {
        assertFalse(dictionary.isValidWord(null));
    }
}