package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WordleDictionaryTest {

    private WordleDictionary dictionary;
    private List<String> testWords;

    @BeforeEach
    void setUp() {
        testWords = Arrays.asList(
                "герой", "книга", "столб", "трава", "молот",
                "город", "берег", "ветер", "солод", "полет"
        );
        dictionary = new WordleDictionary(testWords);
    }

    @Test
    void testDictionarySize() {
        org.junit.jupiter.api.Assertions.assertEquals(testWords.size(), dictionary.size());
    }

    @Test
    void testGetWords() {
        List<String> words = dictionary.getWords();

        org.junit.jupiter.api.Assertions.assertEquals(testWords.size(), words.size());
        org.junit.jupiter.api.Assertions.assertTrue(words.containsAll(testWords));
    }

    @Test
    void testContains() {
        org.junit.jupiter.api.Assertions.assertTrue(dictionary.contains("герой"));
        org.junit.jupiter.api.Assertions.assertTrue(dictionary.contains("книга"));
        org.junit.jupiter.api.Assertions.assertFalse(dictionary.contains("абвгд"));
        org.junit.jupiter.api.Assertions.assertFalse(dictionary.contains("несуществующее"));
    }

    @Test
    void testGetRandomWord() {
        String randomWord = dictionary.getRandomWord();

        org.junit.jupiter.api.Assertions.assertNotNull(randomWord);
        org.junit.jupiter.api.Assertions.assertTrue(dictionary.contains(randomWord));
        org.junit.jupiter.api.Assertions.assertEquals(5, randomWord.length());
    }

    @Test
    void testGetRandomWordFromEmptyDictionary() {
        WordleDictionary emptyDictionary = new WordleDictionary(Arrays.asList());

        org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class, () -> {
            emptyDictionary.getRandomWord();
        });
    }

    @Test
    void testFilterWords() {
        Set<Character> correctLetters = new HashSet<>();
        Set<Character> presentLetters = new HashSet<>();
        Set<Character> absentLetters = new HashSet<>();
        List<String> guessedWords = Arrays.asList("г????", "?е???");

        correctLetters.add('г');
        presentLetters.add('е');

        List<String> filtered = dictionary.filterWords(correctLetters, presentLetters, absentLetters, guessedWords);

        org.junit.jupiter.api.Assertions.assertNotNull(filtered);
    }

    @Test
    void testFilterByAbsentLetters() {
        Set<Character> correctLetters = new HashSet<>();
        Set<Character> presentLetters = new HashSet<>();
        Set<Character> absentLetters = new HashSet<>();
        absentLetters.add('х');
        absentLetters.add('ц');

        List<String> filtered = dictionary.filterWords(correctLetters, presentLetters, absentLetters, Arrays.asList());

        for (String word : filtered) {
            for (char c : absentLetters) {
                org.junit.jupiter.api.Assertions.assertFalse(word.contains(String.valueOf(c)));
            }
        }
    }

    @Test
    void testFilterByPresentLetters() {
        Set<Character> correctLetters = new HashSet<>();
        Set<Character> presentLetters = new HashSet<>();
        presentLetters.add('г');
        presentLetters.add('р');

        Set<Character> absentLetters = new HashSet<>();

        List<String> filtered = dictionary.filterWords(correctLetters, presentLetters, absentLetters, Arrays.asList());

        for (String word : filtered) {
            for (char c : presentLetters) {
                org.junit.jupiter.api.Assertions.assertTrue(word.contains(String.valueOf(c)));
            }
        }
    }

    @Test
    void testGeneratePattern() {
        String pattern1 = WordleDictionary.generatePattern("гонец", "герой");
        org.junit.jupiter.api.Assertions.assertEquals(5, pattern1.length());

        String pattern2 = WordleDictionary.generatePattern("герой", "герой");
        org.junit.jupiter.api.Assertions.assertEquals("+++++", pattern2);

        String pattern3 = WordleDictionary.generatePattern("ааааа", "ббббб");
        org.junit.jupiter.api.Assertions.assertEquals("-----", pattern3);
    }

    @Test
    void testGeneratePatternSpecific() {
        String pattern = WordleDictionary.generatePattern("книга", "гиган");
        org.junit.jupiter.api.Assertions.assertNotNull(pattern);
    }

    @Test
    void testNormalizeWord() {
        org.junit.jupiter.api.Assertions.assertEquals("герой", WordleDictionary.normalizeWord("Герой"));
        org.junit.jupiter.api.Assertions.assertEquals("герой", WordleDictionary.normalizeWord("ГЕРОЙ"));
        org.junit.jupiter.api.Assertions.assertEquals("ежик", WordleDictionary.normalizeWord("ёжик"));
        org.junit.jupiter.api.Assertions.assertEquals("ежик", WordleDictionary.normalizeWord("Ёжик"));
        org.junit.jupiter.api.Assertions.assertEquals("клен", WordleDictionary.normalizeWord("Клён"));
        org.junit.jupiter.api.Assertions.assertEquals("клен", WordleDictionary.normalizeWord("клен"));
    }

    @Test
    void testNormalizeWordWithMixedCase() {
        org.junit.jupiter.api.Assertions.assertEquals("берег", WordleDictionary.normalizeWord("Берег"));
        org.junit.jupiter.api.Assertions.assertEquals("берег", WordleDictionary.normalizeWord("БЕРЕГ"));
        org.junit.jupiter.api.Assertions.assertEquals("берег", WordleDictionary.normalizeWord("берег"));
    }
}