package ru.yandex.practicum;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleDictionaryLoaderTest {

    @TempDir
    Path tempDir;

    private Path createTestDictionary(String... lines) throws IOException {
        Path dictFile = tempDir.resolve("test_dict.txt");
        Files.write(dictFile, List.of(lines));
        return dictFile;
    }

    @Test
    void shouldLoadValidFiveLetterWords() throws IOException {
        Path dictFile = createTestDictionary(
                "кот", "дом", "лес", "пять",
                "шесть", "семь", "восемь"
        );

        WordleDictionary dictionary = WordleDictionaryLoader.workingWithFile();

        assertNotNull(dictionary);
        assertTrue(dictionary.getWords().contains("пять"));
    }

    @Test
    void shouldFilterOnlyFiveLetterWords() throws IOException {
        Path dictFile = createTestDictionary(
                "кот",
                "дом",
                "лес",
                "слово",
                "пять",
                "шесть",
                "длинноеслово"
        );

        WordleDictionary dictionary = WordleDictionaryLoader.workingWithFile();

        assertEquals(3, dictionary.getWords().size());
        assertTrue(dictionary.getWords().contains("слово"));
        assertTrue(dictionary.getWords().contains("пять"));
        assertTrue(dictionary.getWords().contains("шесть"));
    }

    @Test
    void shouldConvertToLowerCase() throws IOException {
        Path dictFile = createTestDictionary("СЛОВО", "Пять", "ДоМ");

        WordleDictionary dictionary = WordleDictionaryLoader.workingWithFile();

        assertTrue(dictionary.getWords().contains("слово"));
        assertTrue(dictionary.getWords().contains("пять"));
    }

    @Test
    void shouldReplaceYoWithE() throws IOException {
        Path dictFile = createTestDictionary("ёжик", "ёлка", "медёк");

        WordleDictionary dictionary = WordleDictionaryLoader.workingWithFile();

        assertTrue(dictionary.getWords().contains("ежик"));
        assertTrue(dictionary.getWords().contains("елка"));
        assertTrue(dictionary.getWords().contains("медек"));
        assertFalse(dictionary.getWords().contains("ёжик"));
    }

    @Test
    void shouldTrimWhitespace() throws IOException {
        Path dictFile = createTestDictionary("  слово  ", "\tпять\t", "\nдом\n");

        WordleDictionary dictionary = WordleDictionaryLoader.workingWithFile();

        assertTrue(dictionary.getWords().contains("слово"));
        assertTrue(dictionary.getWords().contains("пять"));
    }

    @Test
    void shouldThrowExceptionWhenFileNotFound() {
        System.setProperty("user.dir", tempDir.toString());

        assertThrows(IOException.class, () ->
                WordleDictionaryLoader.workingWithFile()
        );
    }

    @Test
    void shouldReturnEmptyDictionaryWhenNoFiveLetterWords() throws IOException {
        createTestDictionary("кот", "дом", "лес");

        WordleDictionary dictionary = WordleDictionaryLoader.workingWithFile();

        assertTrue(dictionary.getWords().isEmpty());
    }
}