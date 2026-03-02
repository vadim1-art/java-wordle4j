package ru.yandex.practicum;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class WordleDictionaryLoader {

    public static WordleDictionary workingWithFile() throws IOException {
        Path path = Paths.get("words_ru.txt");

        if (!Files.exists(path)) {
            Wordle.log("Файл словаря не найден: " + path.toAbsolutePath());
            throw new IOException("Файл словаря не найден");
        }

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            List<String> words = reader.lines()
                    .map(String::trim)
                    .filter(word -> !word.isEmpty())
                    .filter(word -> word.length() == 5)
                    .map(String::toLowerCase)
                    .map(word -> word.replace('ё', 'е'))
                    .toList();

            if (words.isEmpty()) {
                Wordle.log("В словаре нет слов длиной 5 букв");
            } else {
                Wordle.log("Загружено " + words.size() + " слов из файла");
            }

            return new WordleDictionary(words);
        } catch (IOException e) {
            Wordle.log("Ошибка при чтении файла словаря: " + e.getMessage());
            throw e;
        }
    }
}