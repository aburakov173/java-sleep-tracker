package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.result.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class MinDurationFunction implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        long minDuration = sessions.stream()
                .filter(Objects::nonNull) // Защита от null
                .mapToLong(SleepingSession::getDurationInMinutes)
                .filter(duration -> duration >= 0) // Фильтрация отрицательных значений
                .min()
                .orElse(-1);

        return new SleepAnalysisResult("Минимальная продолжительность сессии (минуты)", minDuration);
    }
}