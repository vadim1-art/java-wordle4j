package ru.yandex.practicum;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class WordleDictionaryLoader {

    private PrintWriter logger;

    public WordleDictionaryLoader(PrintWriter logger) {
        this.logger = logger;
    }

    public WordleDictionary loadDictionary(String fileName) throws IOException {
        logger.println("Загрузка словаря из файла: " + fileName);

        List<String> words = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String normalizedWord = WordleDictionary.normalizeWord(line.trim());

                if (normalizedWord.length() == 5 && normalizedWord.matches("[а-я]+")) {
                    words.add(normalizedWord);
                }
            }
        }

        if (words.isEmpty()) {
            throw new EmptyDictionaryException("Словарь не содержит подходящих слов (5 букв)");
        }

        logger.println("Загружено слов длиной 5 букв: " + words.size());
        return new WordleDictionary(words);
    }
}