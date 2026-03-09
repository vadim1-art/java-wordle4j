package ru.yandex.practicum;

// Базовое игровое исключение
class WordleException extends Exception {
    public WordleException(String message) {
        super(message);
    }
}

// Исключение для слова не из словаря
class WordNotFoundInDictionaryException extends WordleException {
    public WordNotFoundInDictionaryException(String message) {
        super(message);
    }
}

// Исключение для неправильной длины слова
class InvalidWordLengthException extends WordleException {
    public InvalidWordLengthException(String message) {
        super(message);
    }
}

// Исключение для недопустимых символов
class InvalidCharactersException extends WordleException {
    public InvalidCharactersException(String message) {
        super(message);
    }
}

// Исключение для ошибок инициализации игры
class GameInitializationException extends RuntimeException {
    public GameInitializationException(String message) {
        super(message);
    }
}

// Исключение для пустого словаря
class EmptyDictionaryException extends RuntimeException {
    public EmptyDictionaryException(String message) {
        super(message);
    }
}