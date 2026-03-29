package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.enums.ChronoType;
import ru.yandex.practicum.sleeptracker.enums.SleepQuality;
import ru.yandex.practicum.sleeptracker.functions.*;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import ru.yandex.practicum.sleeptracker.result.SleepAnalysisResult;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SleepTrackerAppTest {

    // Тесты для TotalSessionsFunction

    @Test
    public void testTotalSessions_EmptyList() {
        List<SleepingSession> sessions = new ArrayList<>();
        TotalSessionsFunction function = new TotalSessionsFunction();
        SleepAnalysisResult result = function.apply(sessions);

        assertEquals(0L, result.getValue(), "Пустой список должен вернуть 0");
    }

    @Test
    public void testTotalSessions_MultipleElements() {
        List<SleepingSession> sessions = createTestSessions();
        TotalSessionsFunction function = new TotalSessionsFunction();
        SleepAnalysisResult result = function.apply(sessions);

        assertEquals(3L, result.getValue(), "Должно быть 3 сессии");
    }

    // Тесты для MinDurationFunction

    @Test
    public void testMinDuration_SingleSession() {
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 22, 0),
                LocalDateTime.of(2025, 10, 2, 8, 0),
                SleepQuality.GOOD
        ));

        MinDurationFunction function = new MinDurationFunction();
        SleepAnalysisResult result = function.apply(sessions);

        assertEquals(600L, result.getValue(), "10 часов = 600 минут");
    }

    @Test
    public void testMinDuration_MultipleSessions() {
        List<SleepingSession> sessions = createTestSessions();
        MinDurationFunction function = new MinDurationFunction();
        SleepAnalysisResult result = function.apply(sessions);

        assertEquals(50L, result.getValue(), "Минимум должен быть 50 минут");
    }

    // Тесты для MaxDurationFunction

    @Test
    public void testMaxDuration_SingleSession() {
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 23, 0),
                LocalDateTime.of(2025, 10, 2, 9, 0),
                SleepQuality.NORMAL
        ));

        MaxDurationFunction function = new MaxDurationFunction();
        SleepAnalysisResult result = function.apply(sessions);

        assertEquals(600L, result.getValue(), "10 часов = 600 минут");
    }

    @Test
    public void testMaxDuration_MultipleSessions() {
        List<SleepingSession> sessions = createTestSessions();
        MaxDurationFunction function = new MaxDurationFunction();
        SleepAnalysisResult result = function.apply(sessions);

        assertEquals(540L, result.getValue(), "Максимум должен быть 540 минут");
    }

    // Тесты для AverageDurationFunction

    @Test
    public void testAverageDuration_TwoSessions() {
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 22, 0),
                LocalDateTime.of(2025, 10, 2, 6, 0),
                SleepQuality.GOOD
        ));
        sessions.add(new SleepingSession(
                LocalDateTime.of(2025, 10, 2, 23, 0),
                LocalDateTime.of(2025, 10, 3, 7, 0),
                SleepQuality.NORMAL
        ));

        AverageDurationFunction function = new AverageDurationFunction();
        SleepAnalysisResult result = function.apply(sessions);

        assertEquals("480,00", result.getValue(), "Среднее должно быть 480 минут");
    }

    @Test
    public void testAverageDuration_SingleSession() {
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 22, 0),
                LocalDateTime.of(2025, 10, 2, 4, 0),
                SleepQuality.GOOD
        ));

        AverageDurationFunction function = new AverageDurationFunction();
        SleepAnalysisResult result = function.apply(sessions);

        assertEquals("360,00", result.getValue(), "Среднее должно быть 360 минут");
    }

    // Тесты для BadQualitySessionsFunction

    @Test
    public void testBadQuality_NoBadSessions() {
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 22, 0),
                LocalDateTime.of(2025, 10, 2, 8, 0),
                SleepQuality.GOOD
        ));
        sessions.add(new SleepingSession(
                LocalDateTime.of(2025, 10, 2, 23, 0),
                LocalDateTime.of(2025, 10, 3, 7, 0),
                SleepQuality.NORMAL
        ));

        BadQualitySessionsFunction function = new BadQualitySessionsFunction();
        SleepAnalysisResult result = function.apply(sessions);

        assertEquals(0L, result.getValue(), "Не должно быть плохих сессий");
    }

    @Test
    public void testBadQuality_SomeBadSessions() {
        List<SleepingSession> sessions = createTestSessions();
        BadQualitySessionsFunction function = new BadQualitySessionsFunction();
        SleepAnalysisResult result = function.apply(sessions);

        assertEquals(1L, result.getValue(), "Должна быть 1 плохая сессия");
    }

    // Тесты для SleeplessNightsFunction

    @Test
    public void testSleeplessNights_NoSleeplessNights() {
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 22, 0),
                LocalDateTime.of(2025, 10, 2, 8, 0),
                SleepQuality.GOOD
        ));
        sessions.add(new SleepingSession(
                LocalDateTime.of(2025, 10, 2, 23, 0),
                LocalDateTime.of(2025, 10, 3, 7, 0),
                SleepQuality.NORMAL
        ));

        SleeplessNightsFunction function = new SleeplessNightsFunction();
        SleepAnalysisResult result = function.apply(sessions);

        assertEquals(0L, result.getValue(), "Не должно быть бессонных ночей");
    }

    @Test
    public void testSleeplessNights_EdgeCase_AfterNoon() {
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 14, 0),
                LocalDateTime.of(2025, 10, 1, 15, 0),
                SleepQuality.NORMAL
        ));
        sessions.add(new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 23, 0),
                LocalDateTime.of(2025, 10, 2, 7, 0),
                SleepQuality.GOOD
        ));

        SleeplessNightsFunction function = new SleeplessNightsFunction();
        SleepAnalysisResult result = function.apply(sessions);

        assertEquals(0L, result.getValue(), "Не должно быть бессонных ночей");
    }

    @Test
    public void testSleeplessNights_OneSleeplessNight() {
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 22, 0),
                LocalDateTime.of(2025, 10, 2, 8, 0),
                SleepQuality.GOOD
        ));
        sessions.add(new SleepingSession(
                LocalDateTime.of(2025, 10, 3, 23, 0),
                LocalDateTime.of(2025, 10, 4, 7, 0),
                SleepQuality.NORMAL
        ));

        SleeplessNightsFunction function = new SleeplessNightsFunction();
        SleepAnalysisResult result = function.apply(sessions);

        assertEquals(1L, result.getValue(), "Должна быть 1 бессонная ночь");
    }


    @Test
    public void testSleeplessNights_EdgeCase_MultiDay() {
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 22, 0),
                LocalDateTime.of(2025, 10, 4, 10, 0),
                SleepQuality.GOOD
        ));

        SleeplessNightsFunction function = new SleeplessNightsFunction();
        SleepAnalysisResult result = function.apply(sessions);

        assertEquals(0L, result.getValue(), "Не должно быть бессонных ночей при многодневном сне");
    }

    @Test
    public void testSleeplessNights_MultiDaySession_ThreeNights() {
        List<SleepingSession> sessions = new ArrayList<>();

        // Трёхдневная сессия
        sessions.add(new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 23, 0),
                LocalDateTime.of(2025, 10, 3, 7, 0),
                SleepQuality.NORMAL
        ));

        SleeplessNightsFunction function = new SleeplessNightsFunction();
        SleepAnalysisResult result = function.apply(sessions);

        assertEquals(0L, result.getValue(),
                "Трёхдневная сессия должна покрывать 3 ночи");
    }

    // Тест проверяет правильный подсчёт дней при переходе через месяц
    @Test
    public void testSleeplessNights_CrossMonthBoundary() {
        List<SleepingSession> sessions = new ArrayList<>();

        sessions.add(new SleepingSession(
                LocalDateTime.of(2025, 10, 30, 23, 0),
                LocalDateTime.of(2025, 10, 31, 7, 0),
                SleepQuality.GOOD
        ));

        sessions.add(new SleepingSession(
                LocalDateTime.of(2025, 11, 1, 23, 0),
                LocalDateTime.of(2025, 11, 2, 7, 0),
                SleepQuality.GOOD
        ));

        SleeplessNightsFunction function = new SleeplessNightsFunction();
        SleepAnalysisResult result = function.apply(sessions);

        assertEquals(1L, result.getValue(),
                "При переходе через месяц должен правильно считать дни (ChronoUnit, не Period)");
    }

    @Test
    public void testSleeplessNights_AfterMidnightSession_ShouldBeNightSleep() {
        List<SleepingSession> sessions = new ArrayList<>();

        sessions.add(new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 2, 0),
                LocalDateTime.of(2025, 10, 1, 7, 0),
                SleepQuality.GOOD
        ));

        sessions.add(new SleepingSession(
                LocalDateTime.of(2025, 10, 2, 23, 0),
                LocalDateTime.of(2025, 10, 3, 7, 0),
                SleepQuality.GOOD
        ));

        SleeplessNightsFunction function = new SleeplessNightsFunction();
        SleepAnalysisResult result = function.apply(sessions);

        assertEquals(1L, result.getValue());
    }

    @Test
    public void testSleeplessNights_MidnightStart_ShouldBeNightSleep() {
        List<SleepingSession> sessions = new ArrayList<>();

        sessions.add(new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 0, 0),
                LocalDateTime.of(2025, 10, 1, 6, 0),
                SleepQuality.GOOD
        ));

        sessions.add(new SleepingSession(
                LocalDateTime.of(2025, 10, 2, 23, 0),
                LocalDateTime.of(2025, 10, 3, 7, 0),
                SleepQuality.GOOD
        ));

        SleeplessNightsFunction function = new SleeplessNightsFunction();
        SleepAnalysisResult result = function.apply(sessions);

        assertEquals(1L, result.getValue());
    }

    @Test
    public void testSleeplessNights_WakeAt6AM_ShouldBeNightSleep() {
        List<SleepingSession> sessions = new ArrayList<>();

        sessions.add(new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 23, 0),
                LocalDateTime.of(2025, 10, 2, 6, 0),
                SleepQuality.GOOD
        ));

        sessions.add(new SleepingSession(
                LocalDateTime.of(2025, 10, 3, 23, 0),
                LocalDateTime.of(2025, 10, 4, 7, 0),
                SleepQuality.GOOD
        ));

        SleeplessNightsFunction function = new SleeplessNightsFunction();
        SleepAnalysisResult result = function.apply(sessions);

        assertEquals(1L, result.getValue());
    }

    @Test
    public void testSleeplessNights_DaytimeSleep_ShouldNotBeNightSleep() {
        List<SleepingSession> sessions = new ArrayList<>();

        sessions.add(new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 23, 0),
                LocalDateTime.of(2025, 10, 2, 7, 0),
                SleepQuality.GOOD
        ));

        sessions.add(new SleepingSession(
                LocalDateTime.of(2025, 10, 2, 14, 0),
                LocalDateTime.of(2025, 10, 2, 15, 0),
                SleepQuality.GOOD
        ));

        sessions.add(new SleepingSession(
                LocalDateTime.of(2025, 10, 4, 23, 0),
                LocalDateTime.of(2025, 10, 5, 7, 0),
                SleepQuality.GOOD
        ));

        SleeplessNightsFunction function = new SleeplessNightsFunction();
        SleepAnalysisResult result = function.apply(sessions);

        assertEquals(2L, result.getValue());
    }


    // Тесты для ChronoTypeFunction

    @Test
    public void testChronoType_Owl() {
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 23, 30),
                LocalDateTime.of(2025, 10, 2, 10, 0),
                SleepQuality.GOOD
        ));
        sessions.add(new SleepingSession(
                LocalDateTime.of(2025, 10, 2, 23, 0),
                LocalDateTime.of(2025, 10, 3, 9, 30),
                SleepQuality.NORMAL
        ));

        ChronoTypeFunction function = new ChronoTypeFunction();
        SleepAnalysisResult result = function.apply(sessions);

        assertEquals(ChronoType.OWL, result.getValue(), "Должен быть хронотип Сова");
    }

    @Test
    public void testChronoType_Lark() {
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 21, 0),
                LocalDateTime.of(2025, 10, 2, 6, 0),
                SleepQuality.GOOD
        ));
        sessions.add(new SleepingSession(
                LocalDateTime.of(2025, 10, 2, 21, 30),
                LocalDateTime.of(2025, 10, 3, 6, 30),
                SleepQuality.NORMAL
        ));

        ChronoTypeFunction function = new ChronoTypeFunction();
        SleepAnalysisResult result = function.apply(sessions);

        assertEquals(ChronoType.LARK, result.getValue(), "Должен быть хронотип Жаворонок");
    }

    @Test
    public void testChronoType_Dove() {
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 22, 30),
                LocalDateTime.of(2025, 10, 2, 7, 30),
                SleepQuality.GOOD
        ));
        sessions.add(new SleepingSession(
                LocalDateTime.of(2025, 10, 2, 22, 0),
                LocalDateTime.of(2025, 10, 3, 8, 0),
                SleepQuality.NORMAL
        ));

        ChronoTypeFunction function = new ChronoTypeFunction();
        SleepAnalysisResult result = function.apply(sessions);

        assertEquals(ChronoType.DOVE, result.getValue(), "Должен быть хронотип Голубь");
    }

    @Test
    public void testChronoType_Mixed() {
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 23, 30),
                LocalDateTime.of(2025, 10, 2, 10, 0),
                SleepQuality.GOOD
        ));
        sessions.add(new SleepingSession(
                LocalDateTime.of(2025, 10, 2, 21, 0),
                LocalDateTime.of(2025, 10, 3, 6, 0),
                SleepQuality.NORMAL
        ));

        ChronoTypeFunction function = new ChronoTypeFunction();
        SleepAnalysisResult result = function.apply(sessions);

        assertEquals(ChronoType.DOVE, result.getValue(), "При равенстве должен быть Голубь");
    }

    @Test
    public void testChronoType_AfterMidnightSession_ShouldBeIncluded() {
        List<SleepingSession> sessions = new ArrayList<>();

        // Несколько сессий 2:00-7:00 (засыпает после полуночи, просыпается рано)
        sessions.add(new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 2, 0),
                LocalDateTime.of(2025, 10, 1, 7, 0),
                SleepQuality.GOOD
        ));
        sessions.add(new SleepingSession(
                LocalDateTime.of(2025, 10, 2, 2, 30),
                LocalDateTime.of(2025, 10, 2, 6, 30),
                SleepQuality.GOOD
        ));
        sessions.add(new SleepingSession(
                LocalDateTime.of(2025, 10, 3, 1, 0),
                LocalDateTime.of(2025, 10, 3, 6, 0),
                SleepQuality.GOOD
        ));

        ChronoTypeFunction function = new ChronoTypeFunction();
        SleepAnalysisResult result = function.apply(sessions);

        assertNotNull(result.getValue(),
                "Сессии 2:00-7:00 должны учитываться для хронотипа");
    }

    // Вспомогательные методы
    private List<SleepingSession> createTestSessions() {
        List<SleepingSession> sessions = new ArrayList<>();

        sessions.add(new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 22, 0),
                LocalDateTime.of(2025, 10, 2, 7, 0),
                SleepQuality.GOOD
        ));

        sessions.add(new SleepingSession(
                LocalDateTime.of(2025, 10, 2, 23, 0),
                LocalDateTime.of(2025, 10, 3, 7, 0),
                SleepQuality.NORMAL
        ));

        sessions.add(new SleepingSession(
                LocalDateTime.of(2025, 10, 3, 14, 30),
                LocalDateTime.of(2025, 10, 3, 15, 20),
                SleepQuality.BAD
        ));

        return sessions;
    }
}