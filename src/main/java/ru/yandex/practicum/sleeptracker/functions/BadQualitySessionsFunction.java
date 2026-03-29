package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.result.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import ru.yandex.practicum.sleeptracker.enums.SleepQuality;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class BadQualitySessionsFunction implements Function<List<SleepingSession>, SleepAnalysisResult> {

    private static final String DESCRIPTION = "Количество сессий с плохим качеством сна";

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        // Обработка null-входа
        if (sessions == null) {
            return new SleepAnalysisResult(DESCRIPTION, 0L);
        }

        long badCount = sessions.stream()
                // Фильтруем null-элементы
                .filter(Objects::nonNull)
                // Оставляем только сессии с плохим качеством сна
                .filter(session -> session.getQuality() == SleepQuality.BAD)
                .count();

        return new SleepAnalysisResult(DESCRIPTION, badCount);
    }
}