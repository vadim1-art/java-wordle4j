package ru.yandex.practicum;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;


public class Wordle {

    private static final String DICTIONARY_FILE = "words_ru.txt";
    private static final String LOG_FILE = "log-file.txt";
    private static final int WORD_SIZE = 5;
    private static PrintWriter logger;

    public static void main(String[] args) {
        try {
            initializeLogger();

            logger.println("=== Запуск игры Wordle ===");
            logger.println("Время запуска: " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

            Path dictPath = Paths.get(DICTIONARY_FILE);
            if (!Files.exists(dictPath)) {
                throw new GameInitializationException("Файл словаря не найден: " + DICTIONARY_FILE);
            }

            WordleDictionaryLoader loader = new WordleDictionaryLoader(logger);
            WordleDictionary dictionary = loader.loadDictionary(DICTIONARY_FILE);

            logger.println("Словарь загружен. Всего слов: " + dictionary.size());

            WordleGame game = new WordleGame(dictionary, logger);

            playGame(game);

        } catch (GameInitializationException e) {
            System.err.println("Ошибка инициализации игры: " + e.getMessage());
            if (logger != null) {
                logger.println("КРИТИЧЕСКАЯ ОШИБКА: " + e.getMessage());
                e.printStackTrace(logger);
            }
        } catch (Exception e) {
            System.err.println("Непредвиденная ошибка: " + e.getMessage());
            if (logger != null) {
                logger.println("НЕПРЕДВИДЕННАЯ ОШИБКА: " + e.getMessage());
                e.printStackTrace(logger);
            }
        } finally {
            if (logger != null) {
                logger.println("=== Завершение игры ===");
                logger.close();
            }
        }
    }

    private static void initializeLogger() throws IOException {
        FileWriter fileWriter = new FileWriter(LOG_FILE, StandardCharsets.UTF_8, true);
        logger = new PrintWriter(fileWriter, true);
    }

    private static void playGame(WordleGame game) {
        try (Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8)) {
            System.out.println("Добро пожаловать в игру Wordle!");
            System.out.println("Отгадайте слово из 5 букв. У вас " + game.getStepsLeft() + " попыток.");
            System.out.println("Если нужна подсказка, нажмите Enter (пустая строка)");

            while (!game.isGameOver()) {
                System.out.print("\nВведите слово: ");
                String input = scanner.nextLine().trim().toLowerCase();

                try {
                    if (input.isEmpty()) {
                        String hint = game.getHint();
                        if (hint != null) {
                            System.out.println("Подсказка: попробуйте слово \"" + hint + "\"");
                        } else {
                            System.out.println("Нет доступных подсказок");
                        }
                        continue;
                    }

                    if (input.length() != WORD_SIZE) {
                        throw new InvalidWordLengthException("Слово должно состоять из 5 букв");
                    }

                    if (!input.matches("[а-яё]+")) {
                        throw new InvalidCharactersException("Слово должно содержать только русские буквы");
                    }

                    WordleGame.GuessResult result = game.makeGuess(input);

                    System.out.println(input);
                    System.out.println(result.getPattern());

                    if (result.isGameWon()) {
                        System.out.println("\nПоздравляю! Вы отгадали слово \"" + game.getAnswer() + "\"!");
                        System.out.println("Количество попыток: " + (6 - game.getStepsLeft()));
                        break;
                    } else if (game.getStepsLeft() == 0) {
                        System.out.println("\nК сожалению, попытки закончились. Загаданное слово: \"" + game.getAnswer() + "\"");
                        break;
                    }

                    System.out.println("Осталось попыток: " + game.getStepsLeft());

                } catch (WordNotFoundInDictionaryException e) {
                    System.out.println("Ошибка: такого слова нет в словаре. Попробуйте другое слово.");
                } catch (InvalidWordLengthException | InvalidCharactersException e) {
                    System.out.println("Ошибка: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Произошла ошибка при обработке слова");
                    logger.println("Ошибка при обработке ввода пользователя: " + e.getMessage());
                }
            }

            if (game.isGameOver() && !game.isGameWon()) {
                System.out.println("\nИгра окончена. Загаданное слово: \"" + game.getAnswer() + "\"");
            }
        }
    }
}