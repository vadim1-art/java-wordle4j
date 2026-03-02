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
        words = Arrays.asList("кот", "дом", "лес", "пять", "шесть");
        dictionary = new WordleDictionary(words);
    }

    @Test
    void shouldReturnRandomWord() {
        List<String> possibleWords = dictionary.getWords();

        String randomWord = dictionary.getRandomWord();

        assertNotNull(randomWord);
        assertTrue(possibleWords.contains(randomWord));
    }

    @Test
    void shouldReturnFirstWordWhenRandomFails() {
        WordleDictionary emptyDict = new WordleDictionary(Arrays.asList("первый"));

        String word = emptyDict.getRandomWord();

        assertEquals("первый", word);
    }

    @Test
    void shouldValidateCorrectWord() {
        String validWord = "пять";

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
        dictionary = new WordleDictionary(Arrays.asList("ежик", "елка"));

        boolean isValid = dictionary.isValidWord("ёжик");

        assertTrue(isValid);
    }

    @Test
    void shouldReturnWordsList() {
        List<String> returnedWords = dictionary.getWords();

        assertEquals(words, returnedWords);
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        WordleDictionary dict1 = new WordleDictionary(words);
        WordleDictionary dict2 = new WordleDictionary(words);
        WordleDictionary dict3 = new WordleDictionary(Arrays.asList("другие", "слова"));

        assertEquals(dict1, dict2);
        assertNotEquals(dict1, dict3);
        assertEquals(dict1.hashCode(), dict2.hashCode());
        assertNotEquals(dict1.hashCode(), dict3.hashCode());
    }

    @Test
    void shouldHandleNullInIsValidWord() {
        assertFalse(dictionary.isValidWord(null));
    }
}