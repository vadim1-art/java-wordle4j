package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;

public class WordleDictionaryLoaderTest {

    private PrintWriter testLogger;
    private WordleDictionaryLoader loader;

    @BeforeEach
    void setUp() {
        testLogger = new PrintWriter(System.out, true);
        loader = new WordleDictionaryLoader(testLogger);
    }

    @Test
    void testLoadDictionaryFromFile(@TempDir Path tempDir) throws IOException {
        Path dictFile = tempDir.resolve("test_dict.txt");

        try (FileWriter writer = new FileWriter(dictFile.toFile())) {
            writer.write("герой\n");
            writer.write("книга\n");
            writer.write("столб\n");
            writer.write("трава\n");
            writer.write("молот\n");
            writer.write("длинноеслово\n");
            writer.write("кот\n");
        }

        WordleDictionary dictionary = loader.loadDictionary(dictFile.toString());

        org.junit.jupiter.api.Assertions.assertNotNull(dictionary);
        org.junit.jupiter.api.Assertions.assertEquals(5, dictionary.size());
        org.junit.jupiter.api.Assertions.assertTrue(dictionary.contains("герой"));
        org.junit.jupiter.api.Assertions.assertTrue(dictionary.contains("книга"));
        org.junit.jupiter.api.Assertions.assertFalse(dictionary.contains("длинноеслово"));
        org.junit.jupiter.api.Assertions.assertFalse(dictionary.contains("кот"));
    }

    @Test
    void testLoadDictionaryWithEyoReplacement(@TempDir Path tempDir) throws IOException {
        Path dictFile = tempDir.resolve("test_dict_eyo.txt");

        try (FileWriter writer = new FileWriter(dictFile.toFile())) {
            writer.write("ёжик\n");
            writer.write("клён\n");
            writer.write("берёза\n");
            writer.write("елка\n");
        }

        WordleDictionary dictionary = loader.loadDictionary(dictFile.toString());

        org.junit.jupiter.api.Assertions.assertNotNull(dictionary);

        org.junit.jupiter.api.Assertions.assertTrue(dictionary.contains("ежик"));
        org.junit.jupiter.api.Assertions.assertTrue(dictionary.contains("клен"));
        org.junit.jupiter.api.Assertions.assertTrue(dictionary.contains("елка"));

        org.junit.jupiter.api.Assertions.assertFalse(dictionary.contains("береза"));
    }

    @Test
    void testLoadDictionaryWithUppercase(@TempDir Path tempDir) throws IOException {
        Path dictFile = tempDir.resolve("test_dict_uppercase.txt");

        try (FileWriter writer = new FileWriter(dictFile.toFile())) {
            writer.write("ГЕРОЙ\n");
            writer.write("Книга\n");
            writer.write("СтОлБ\n");
        }

        WordleDictionary dictionary = loader.loadDictionary(dictFile.toString());

        org.junit.jupiter.api.Assertions.assertNotNull(dictionary);
        org.junit.jupiter.api.Assertions.assertTrue(dictionary.contains("герой"));
        org.junit.jupiter.api.Assertions.assertTrue(dictionary.contains("книга"));
        org.junit.jupiter.api.Assertions.assertTrue(dictionary.contains("столб"));
    }

    @Test
    void testLoadDictionaryWithInvalidLengthWords(@TempDir Path tempDir) throws IOException {
        Path dictFile = tempDir.resolve("test_dict_invalid.txt");

        try (FileWriter writer = new FileWriter(dictFile.toFile())) {
            writer.write("герой\n");
            writer.write("кот\n");
            writer.write("столб\n");
            writer.write("оченьдлинноеслово\n");
        }

        WordleDictionary dictionary = loader.loadDictionary(dictFile.toString());

        org.junit.jupiter.api.Assertions.assertNotNull(dictionary);
        org.junit.jupiter.api.Assertions.assertEquals(2, dictionary.size());
        org.junit.jupiter.api.Assertions.assertTrue(dictionary.contains("герой"));
        org.junit.jupiter.api.Assertions.assertTrue(dictionary.contains("столб"));
        org.junit.jupiter.api.Assertions.assertFalse(dictionary.contains("кот"));
    }

    @Test
    void testLoadEmptyDictionary(@TempDir Path tempDir) {
        Path dictFile = tempDir.resolve("empty_dict.txt");


        org.junit.jupiter.api.Assertions.assertThrows(EmptyDictionaryException.class, () -> {
            try (FileWriter writer = new FileWriter(dictFile.toFile())) {
            }
            loader.loadDictionary(dictFile.toString());
        });
    }

    @Test
    void testLoadNonexistentFile() {
        String nonExistentFile = "несуществующий_файл.txt";

        org.junit.jupiter.api.Assertions.assertThrows(IOException.class, () -> {
            loader.loadDictionary(nonExistentFile);
        });
    }

    @Test
    void testLoadDictionaryWithSpecialCharacters(@TempDir Path tempDir) throws IOException {
        Path dictFile = tempDir.resolve("test_dict_special.txt");

        try (FileWriter writer = new FileWriter(dictFile.toFile())) {
            writer.write("герой\n");
            writer.write("  книга  \n");
            writer.write("\tстолб\t\n");
            writer.write("трава\n");
        }

        WordleDictionary dictionary = loader.loadDictionary(dictFile.toString());

        org.junit.jupiter.api.Assertions.assertNotNull(dictionary);
        org.junit.jupiter.api.Assertions.assertEquals(4, dictionary.size());
        org.junit.jupiter.api.Assertions.assertTrue(dictionary.contains("герой"));
        org.junit.jupiter.api.Assertions.assertTrue(dictionary.contains("книга"));
        org.junit.jupiter.api.Assertions.assertTrue(dictionary.contains("столб"));
        org.junit.jupiter.api.Assertions.assertTrue(dictionary.contains("трава"));
    }
}