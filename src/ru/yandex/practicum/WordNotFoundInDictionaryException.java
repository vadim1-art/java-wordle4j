package ru.yandex.practicum;

// Исключение для слова не из словаря
class WordNotFoundInDictionaryException extends WordleException {
    public WordNotFoundInDictionaryException(String message) {
        super(message);
    }
}
