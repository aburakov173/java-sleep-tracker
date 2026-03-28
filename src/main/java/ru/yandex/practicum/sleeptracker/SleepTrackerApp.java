package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.enums.SleepQuality;
import ru.yandex.practicum.sleeptracker.functions.*;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import ru.yandex.practicum.sleeptracker.result.SleepAnalysisResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SleepTrackerApp {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    // Список всех функций
    private final List<Function<List<SleepingSession>, SleepAnalysisResult>> analysisFunctions;

    public SleepTrackerApp() {
        analysisFunctions = new ArrayList<>();

        analysisFunctions.add(new TotalSessionsFunction());
        analysisFunctions.add(new MinDurationFunction());
        analysisFunctions.add(new MaxDurationFunction());
        analysisFunctions.add(new AverageDurationFunction());
        analysisFunctions.add(new BadQualitySessionsFunction());
        analysisFunctions.add(new SleeplessNightsFunction());
        analysisFunctions.add(new ChronoTypeFunction());
    }

    public static void main(String[] args) {
        String filePath = "src/main/resources/sleep_log.txt";

        if (args.length > 0) {
            filePath = args[0];
        }

        SleepTrackerApp app = new SleepTrackerApp();

        try {
            List<SleepingSession> sessions = app.loadSleepData(filePath);

            if (sessions.isEmpty()) {
                System.out.println("Файл не содержит данных о сне.");
                return;
            }

            System.out.println("=== Анализ сна ===\n");

            app.analysisFunctions.stream()
                    .map(function -> function.apply(sessions))
                    .forEach(System.out::println);

        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка в данных файла: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Неожиданная ошибка при обработке данных: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Загружает данные о сне из файла. Формат строки: dd.MM.yy HH:mm;dd.MM.yy HH:mm;QUALITY
    public List<SleepingSession> loadSleepData(String filePath) throws IOException {
        return Files.lines(Paths.get(filePath))
                .filter(line -> !line.trim().isEmpty())
                .map(this::parseSleepSession)
                .collect(Collectors.toList());
    }

    // Парсит одну строку из файла в объект SleepingSession
    private SleepingSession parseSleepSession(String line) throws IllegalArgumentException {
        if (line == null) {
            throw new IllegalArgumentException("Строка данных не может быть null");
        }

        String[] parts = line.split(";");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Неверный формат строки: " + line);
        }

        LocalDateTime sleepStart;
        LocalDateTime sleepEnd;
        try {
            sleepStart = LocalDateTime.parse(parts[0].trim(), DATE_TIME_FORMATTER);
            sleepEnd = LocalDateTime.parse(parts[1].trim(), DATE_TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Ошибка парсинга даты в строке: " + line, e);
        }

        if (!sleepEnd.isAfter(sleepStart)) {
            throw new IllegalArgumentException("Время окончания сна должно быть позже времени начала: " + line);
        }

        SleepQuality quality;
        quality = SleepQuality.valueOf(parts[2].trim());

        return new SleepingSession(sleepStart, sleepEnd, quality);
    }
}
