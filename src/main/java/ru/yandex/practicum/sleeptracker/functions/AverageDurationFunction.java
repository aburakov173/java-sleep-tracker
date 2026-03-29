package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import ru.yandex.practicum.sleeptracker.result.SleepAnalysisResult;

import java.text.DecimalFormat;
import java.util.List;
import java.util.function.Function;
import java.util.OptionalDouble;

public class AverageDurationFunction implements Function<List<SleepingSession>, SleepAnalysisResult> {

    private static final DecimalFormat DF = new DecimalFormat("0.00");

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        // Проверка на null
        if (sessions == null) {
            return new SleepAnalysisResult(
                    "Средняя продолжительность сессии (минуты)",
                    "Ошибка: список сессий не может быть null"
            );
        }

        // Обработка пустой коллекции
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult(
                    "Средняя продолжительность сессии (минуты)",
                    "Нет данных для расчёта"
            );
        }

        // Расчёт среднего значения
        double average = sessions.stream()
                .mapToLong(SleepingSession::getDurationInMinutes)
                .average()
                .orElse(0.0);

        // Форматирование результата
        String formattedResult = DF.format(average);

        return new SleepAnalysisResult(
                "Средняя продолжительность сессии (минуты)",
                formattedResult
        );
    }
}