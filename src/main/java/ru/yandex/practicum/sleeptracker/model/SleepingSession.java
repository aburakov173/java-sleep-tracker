package ru.yandex.practicum.sleeptracker.model;

import ru.yandex.practicum.sleeptracker.enums.SleepQuality;

import java.time.LocalDateTime;
import java.time.Duration;

public class SleepingSession {
    private final LocalDateTime sleepStart;
    private final LocalDateTime sleepEnd;
    private final SleepQuality quality;

    public SleepingSession(LocalDateTime sleepStart, LocalDateTime sleepEnd, SleepQuality quality) {
        if (sleepStart == null) {
            throw new IllegalArgumentException("Время начала сна не может быть null");
        }
        if (sleepEnd == null) {
            throw new IllegalArgumentException("Время окончания сна не может быть null");
        }
        if (sleepEnd.isBefore(sleepStart)) {
            throw new IllegalArgumentException("Время окончания сна не может быть раньше времени начала");
        }
        if (quality == null) {
            throw new IllegalArgumentException("Качество сна не может быть null");
        }

        this.sleepStart = sleepStart;
        this.sleepEnd = sleepEnd;
        this.quality = quality;
    }

    public LocalDateTime getSleepStart() {
        return sleepStart;
    }

    public LocalDateTime getSleepEnd() {
        return sleepEnd;
    }

    public SleepQuality getQuality() {
        return quality;
    }

    public long getDurationInMinutes() {
        return Duration.between(sleepStart, sleepEnd).toMinutes();
    }

    @Override
    public String toString() {
        return String.format("Sleep: %s -> %s (%s)", sleepStart, sleepEnd, quality);
    }
}