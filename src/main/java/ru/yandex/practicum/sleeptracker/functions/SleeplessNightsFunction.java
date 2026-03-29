package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import ru.yandex.practicum.sleeptracker.enums.*;
import ru.yandex.practicum.sleeptracker.result.SleepAnalysisResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SleeplessNightsFunction implements Function<List<SleepingSession>, SleepAnalysisResult> {

    private static final LocalTime NIGHT_START = LocalTime.of(0, 0);
    private static final LocalTime NIGHT_END = LocalTime.of(6, 0);

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Количество бессонных ночей", 0L);
        }

        Set<LocalDate> nightsWithSleep = sessions.stream()
                .filter(this::isNightSleep)
                .flatMap(session -> getNightDates(session).stream())
                .collect(Collectors.toSet());

        LocalDateTime firstSessionStart = sessions.stream()
                .map(SleepingSession::getSleepStart)
                .min(LocalDateTime::compareTo)
                .orElseThrow();

        LocalDateTime lastSessionEnd = sessions.stream()
                .map(SleepingSession::getSleepEnd)
                .max(LocalDateTime::compareTo)
                .orElseThrow();

        LocalDate firstNight = getFirstNightInPeriod(firstSessionStart);
        LocalDate lastNight = getLastNightInPeriod(lastSessionEnd);

        if (firstNight.isAfter(lastNight)) {
            return new SleepAnalysisResult("Количество бессонных ночей", 0L);
        }

        long totalNights = ChronoUnit.DAYS.between(firstNight, lastNight) + 1;

        long sleeplessNights = totalNights - nightsWithSleep.size();

        return new SleepAnalysisResult("Количество бессонных ночей",
                Math.max(0, sleeplessNights));
    }

    private LocalDate getFirstNightInPeriod(LocalDateTime start) {
        LocalTime startTime = start.toLocalTime();

        if (startTime.isBefore(NIGHT_END)) {
            return start.toLocalDate().minusDays(1);
        }

        return start.toLocalDate();
    }

    private LocalDate getLastNightInPeriod(LocalDateTime end) {
        LocalTime endTime = end.toLocalTime();

        if (endTime.isBefore(NIGHT_END) || endTime.equals(NIGHT_START)) {
            return end.toLocalDate().minusDays(1);
        }

        if (endTime.isBefore(LocalTime.of(18, 0))) {
            return end.toLocalDate().minusDays(1);
        }

        return end.toLocalDate();
    }

    private Set<LocalDate> getNightDates(SleepingSession session) {
        LocalDate startDate = session.getSleepStart().toLocalDate();
        LocalDate endDate = session.getSleepEnd().toLocalDate();
        LocalTime startTime = session.getSleepStart().toLocalTime();

        if (startDate.equals(endDate)) {

            if (startTime.isBefore(NIGHT_END)) {
                return Set.of(startDate.minusDays(1));
            }

            return Set.of(startDate);
        }

        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);

        return java.util.stream.LongStream.range(0, daysBetween)
                .mapToObj(startDate::plusDays)
                .collect(Collectors.toSet());
    }

    private boolean isNightSleep(SleepingSession session) {
        LocalDate startDate = session.getSleepStart().toLocalDate();
        LocalDate endDate = session.getSleepEnd().toLocalDate();
        LocalTime startTime = session.getSleepStart().toLocalTime();
        LocalTime endTime = session.getSleepEnd().toLocalTime();

        if (!startDate.equals(endDate)) {
            return true;
        }

        if (startTime.isBefore(NIGHT_END)) {
            return true;
        }

        if (endTime.isBefore(NIGHT_END) || endTime.equals(LocalTime.of(0, 0))) {
            return true;
        }

        return false;
    }
}