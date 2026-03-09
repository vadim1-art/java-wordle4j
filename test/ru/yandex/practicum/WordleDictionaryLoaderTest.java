package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleDictionaryLoaderTest {

    @TempDir
    Path tempDir;
    private Path dictFile;
    private PrintWriter logWriter;

    @BeforeEach
    void setUp() throws IOException {
        dictFile = tempDir.resolve("words_ru.txt");
        // Создаем PrintWriter для тестов (можно null, но лучше создать)
        logWriter = new PrintWriter(System.out);

        Files.write(dictFile, List.of(
                "кот", "дом", "лес", "пять",
                "шесть", "семь", "восемь", "ёжик",
                "СЛОВО", "  пробел  "
        ));
    }

    @Test
    void shouldLoadValidFiveLetterWords() throws IOException {
        Files.write(dictFile, List.of("пять", "дом", "лес"));

        WordleDictionary dictionary = WordleDictionaryLoader.workingWithFile(logWriter);

        assertNotNull(dictionary);
        assertTrue(dictionary.getWords().contains("пять"));
        assertEquals(1, dictionary.getWords().size());
    }

    @Test
    void shouldFilterOnlyFiveLetterWords() throws IOException {
        Files.write(dictFile, List.of(
                "кот", "дом", "лес", "слово", "пять", "шесть"
        ));

        WordleDictionary dictionary = WordleDictionaryLoader.workingWithFile(logWriter);

        assertEquals(3, dictionary.getWords().size());
        assertTrue(dictionary.getWords().contains("слово"));
        assertTrue(dictionary.getWords().contains("пять"));
        assertTrue(dictionary.getWords().contains("шесть"));
    }

    @Test
    void shouldConvertToLowerCase() throws IOException {
        Files.write(dictFile, List.of("СЛОВО", "ПЯТЬ", "дОм"));

        WordleDictionary dictionary = WordleDictionaryLoader.workingWithFile(logWriter);

        assertTrue(dictionary.getWords().contains("слово"));
        assertTrue(dictionary.getWords().contains("пять"));
        assertTrue(dictionary.getWords().contains("дом"));
    }

    @Test
    void shouldReplaceYoWithE() throws IOException {
        Files.write(dictFile, List.of("ёжик", "ёлка", "медёк"));

        WordleDictionary dictionary = WordleDictionaryLoader.workingWithFile(logWriter);

        List<String> words = dictionary.getWords();
        assertTrue(words.contains("ежик"));
        assertTrue(words.contains("елка"));
        assertTrue(words.contains("медек"));
        assertFalse(words.contains("ёжик"));
    }

    @Test
    void shouldThrowExceptionWhenFileNotFound() {
        Path nonExistentFile = tempDir.resolve("nonexistent.txt");
        // Переименовываем файл, чтобы он не существовал
        dictFile.toFile().delete();

        assertThrows(IOException.class, () ->
                WordleDictionaryLoader.workingWithFile(logWriter)
        );
    }

    @Test
    void shouldReturnEmptyDictionaryWhenNoFiveLetterWords() throws IOException {
        Files.write(dictFile, List.of("кот", "дом", "лес"));

        WordleDictionary dictionary = WordleDictionaryLoader.workingWithFile(logWriter);

        assertTrue(dictionary.getWords().isEmpty());
    }

    @Test
    void shouldTrimWhitespace() throws IOException {
        Files.write(dictFile, List.of("  слово  ", "\tпять\t", "дом"));

        WordleDictionary dictionary = WordleDictionaryLoader.workingWithFile(logWriter);

        assertTrue(dictionary.getWords().contains("слово"));
        assertTrue(dictionary.getWords().contains("пять"));
        assertTrue(dictionary.getWords().contains("дом"));
    }

    @Test
    void shouldHandleEmptyFile() throws IOException {
        Files.write(dictFile, List.of());

        WordleDictionary dictionary = WordleDictionaryLoader.workingWithFile(logWriter);

        assertTrue(dictionary.getWords().isEmpty());
    }

    @Test
    void shouldHandleFileWithOnlyInvalidWords() throws IOException {
        Files.write(dictFile, List.of("кот", "дом", "лес"));

        WordleDictionary dictionary = WordleDictionaryLoader.workingWithFile(logWriter);

        assertTrue(dictionary.getWords().isEmpty());
    }
}