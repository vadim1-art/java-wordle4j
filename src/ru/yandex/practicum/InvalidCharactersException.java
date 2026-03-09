package ru.yandex.practicum;

// Исключение для недопустимых символов
class InvalidCharactersException extends WordleException {
    public InvalidCharactersException(String message) {
        super(message);
    }
}
