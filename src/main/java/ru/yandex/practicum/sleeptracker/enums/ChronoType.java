package ru.yandex.practicum.sleeptracker.enums;

public enum ChronoType {
    OWL("Сова"),           // ложится после 23:00, встаёт после 9:00
    LARK("Жаворонок"),     // ложится до 22:00, встаёт до 7:00
    DOVE("Голубь");        // все остальные случаи

    private final String russianName;

    private ChronoType(String russianName) {
        this.russianName = russianName;
    }

    @Override
    public String toString() {
        return russianName;
    }
}