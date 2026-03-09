package ru.yandex.practicum;

// Исключение для ошибок инициализации игры
class GameInitializationException extends RuntimeException {
    public GameInitializationException(String message) {
        super(message);
    }
}
