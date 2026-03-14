package ru.yandex.practicum;

// Исключение для пустого словаря
class EmptyDictionaryException extends RuntimeException {
    public EmptyDictionaryException(String message) {
        super(message);
    }
}
