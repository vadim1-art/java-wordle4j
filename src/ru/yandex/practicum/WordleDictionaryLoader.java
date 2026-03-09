package ru.yandex.practicum;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class WordleDictionaryLoader {

    public static WordleDictionary workingWithFile(PrintWriter logWriter) throws IOException {
        Path path = Paths.get("words_ru.txt");

        if (!Files.exists(path)) {
            log(logWriter, "Файл словаря не найден: " + path.toAbsolutePath());
            throw new IOException("Файл словаря не найден: " + path.toAbsolutePath());
        }

        log(logWriter, "Загрузка словаря из файла: " + path.toAbsolutePath());

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            List<String> words = reader.lines()
                    .map(String::trim)
                    .filter(word -> !word.isEmpty())
                    .filter(word -> word.length() == 5)
                    .map(String::toLowerCase)
                    .map(word -> word.replace('ё', 'е'))
                    .collect(Collectors.toList());

            log(logWriter, "Загружено слов длиной 5 букв: " + words.size());

            if (words.isEmpty()) {
                log(logWriter, "ПРЕДУПРЕЖДЕНИЕ: В словаре нет слов длиной 5 букв");
            }

            return new WordleDictionary(words, logWriter);
        }
    }

    private static void log(PrintWriter logWriter, String message) {
        if (logWriter != null) {
            logWriter.println("[WordleDictionaryLoader] " + message);
            logWriter.flush();
        }
    }
}