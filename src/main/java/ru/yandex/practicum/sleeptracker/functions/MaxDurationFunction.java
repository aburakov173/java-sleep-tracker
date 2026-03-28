package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.result.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class MaxDurationFunction implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        long maxDuration = sessions.stream()
                .filter(Objects::nonNull) // Защита от null
                .mapToLong(SleepingSession::getDurationInMinutes)
                .filter(duration -> duration >= 0) // Фильтрация отрицательных значений
                .max()
                .orElse(-1); // -1 = нет данных

        return new SleepAnalysisResult("Максимальная продолжительность сессии (минуты)", maxDuration);
    }
}