package ru.yandex.practicum;

// Исключение для неправильной длины слова
class InvalidWordLengthException extends WordleException {
    public InvalidWordLengthException(String message) {
        super(message);
    }
}
