package ru.yandex.practicum;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Scanner;

public class Wordle {

    private static WordleDictionary dictionary;
    private static PrintWriter logWriter;

    static {
        try {
            logWriter = new PrintWriter(new FileWriter("log-file", true));
            log("============ НАЧАЛО ИГРЫ ============");

            dictionary = WordleDictionaryLoader.workingWithFile();
        } catch (Exception e) {
            log("Не удалось загрузить словарь: " + e.getMessage());
            logStackTrace(e);
            System.out.println("Извините, произошла внутренняя ошибка при запуске игры.");
            System.out.println("Пожалуйста, обратитесь к администратору.");
            System.exit(1);
        }
    }

    public static void log(String message) {
        if (logWriter != null) {
            logWriter.println(message);
            logWriter.flush();
        }
    }

    public static void logStackTrace(Exception e) {
        if (logWriter != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            log("СТЕК ТРЕЙС:");
            log(sw.toString());
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Добро пожаловать в игру Wordle!");
        System.out.println("Угадайте слово из 5 букв.");
        System.out.println("У вас есть 6 попыток.");
        System.out.println("(+) - буква на месте, (^) - буква есть, но не на месте, (-) - буквы нет");
        System.out.println("(hint/подсказка) - введите 'hint' для получения подсказки");
        System.out.println();

        try {
            playGame(scanner);
        } catch (Exception e) {
            log("Непредвиденная ошибка в игровом процессе: " + e.getMessage());
            logStackTrace(e);
            System.out.println("Извините, произошла внутренняя ошибка. Игра будет закрыта.");
        }

        scanner.close();
        log("========== КОНЕЦ ИГРЫ ==========");
        if (logWriter != null) {
            logWriter.close();
        }
    }

    private static void playGame(Scanner scanner) {
        String targetWord = dictionary.getRandomWord();
        WordleGame game = new WordleGame(targetWord, 6, dictionary);
        log("Новая игра. Загадано слово: " + targetWord);

        while (game.getSteps() > 0 && !game.isGameWon()) {
            System.out.println(game);
            System.out.print("Введите слово (осталось попыток: " + game.getSteps() + "): ");

            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("hint") || input.equals("подсказка")) {
                handleHintCommand(game);
                continue;
            } else if (input.equals("exit") || input.equals("выход")) {
                System.out.println("Игра прервана. Загаданное слово было: '" + targetWord + "'");
                log("Игрок прервал игру. Загаданное слово: " + targetWord);
                break;
            }

            try {
                if (!dictionary.isValidWord(input)) {
                    System.out.println("Некорректное слово. Слово должно состоять из 5 букв и быть в словаре.");
                    log("Игрок ввел некорректное слово: " + input);
                    continue;
                }

                String result = game.wordTypeChanges(input);
                log("Попытка: " + input + " -> " + result);
                game.makeAttempt(input, result);

            } catch (Exception e) {
                log("Ошибка при обработке слова '" + input + "': " + e.getMessage());
                logStackTrace(e);
                System.out.println("Произошла внутренняя ошибка при проверке слова. Попробуйте другое слово.");
            }
        }

        printGameResult(game);

        System.out.print("\nХотите сыграть еще? (да/нет): ");
        String answer = scanner.nextLine().trim().toLowerCase();
        if (answer.equals("да") || answer.equals("yes") || answer.equals("y") || answer.equals("lf")) {
            System.out.println();
            playGame(scanner);
        } else {
            System.out.println("Спасибо за игру!");
            log("Игрок завершил игру");
        }
    }

    private static void handleHintCommand(WordleGame game) {
        if (game.getResults().isEmpty()) {
            System.out.println("Сделайте хотя бы одну попытку, чтобы получить подсказку!");
            log("Игрок запросил подсказку, но попыток еще не было");
            return;
        }

        String lastResult = game.getResults().get(game.getResults().size() - 1);
        String lastAttempt = game.getAttempts().get(game.getAttempts().size() - 1);

        String hint = WordleGame.hintWord(game.getAnswer(), lastResult, dictionary.getWords());

        if (hint != null && !hint.isEmpty()) {
            System.out.println("\nПопробуйте слово \"" + hint + "\"");
            System.out.println("   (основано на вашей последней попытке: " + lastAttempt + " -> " + lastResult + ")\n");
            log("Игрок запросил подсказку. Последний результат: " + lastResult + ", предложено: " + hint);
        } else {
            System.out.println("\nК сожалению, не удалось найти подходящую подсказку.");
            System.out.println("   Продолжайте угадывать самостоятельно!\n");
            log("Игрок запросил подсказку, но подходящих слов не найдено. Последний результат: " + lastResult);
        }
    }

    private static void printGameResult(WordleGame game) {
        System.out.println("\n=== ИГРА ОКОНЧЕНА ===");
        System.out.println(game);

        if (game.isGameWon()) {
            System.out.println("Вы угадали слово!");
            log("Игра окончена. Победа! Загаданное слово: " + game.getAnswer());
        } else {
            System.out.println("Вы проиграли. Загаданное слово было: '" + game.getAnswer() + "'");
            log("Игра окончена. Поражение. Загаданное слово: " + game.getAnswer());
        }
    }
}