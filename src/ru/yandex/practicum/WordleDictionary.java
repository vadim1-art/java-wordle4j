package ru.yandex.practicum;

import java.util.*;

public class WordleDictionary {
    private List<String> words;
    private Set<String> wordSet;
    private Random random = new Random();

    public WordleDictionary(List<String> words) {
        this.words = words;
        this.wordSet = new HashSet<>(words);
    }

    public String getRandomWord() {
        try {
            int randomIndex = random.nextInt(words.size());
            return words.get(randomIndex);
        } catch (Exception e) {
            Wordle.log("Ошибка при получении случайного слова: " + e.getMessage());
            return words.get(0);
        }
    }

    public boolean isValidWord(String word) {
        try {
            return Optional.ofNullable(word)
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .map(w -> w.replace('ё', 'е'))
                    .filter(w -> w.length() == 5)
                    .filter(w -> wordSet.contains(w))
                    .isPresent();
        } catch (Exception e) {
            Wordle.log("Ошибка при проверке слова '" + word + "': " + e.getMessage());
            return false;
        }
    }

    public List<String> getWords() {
        return words;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        WordleDictionary that = (WordleDictionary) o;
        return Objects.equals(words, that.words);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(words);
    }
}