package edu.eci.dosw.techcup_futbol.GamesTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.eci.dosw.techcup_futbol.exceptions.TechCupException;
import edu.eci.dosw.techcup_futbol.model.Games.Statistics;

class StatisticsTest {

    private Statistics statistics;

    @BeforeEach
    void setUp() {
        // Arrange: Create a fresh Statistics instance for each test
        statistics = new Statistics();
    }

    // --- Tests for constructor Statistics() ---

    @Test
    void shouldInitializeLocalScoreAtZero() {
        // Test: A newly created Statistics object should have localScoreboard = 0
        assertEquals(0, statistics.getLocalScoreboard());
    }

    @Test
    void shouldInitializeAwayScoreAtZero() {
        // Test: A newly created Statistics object should have awayScoreboard = 0
        assertEquals(0, statistics.getAwayScoreboard());
    }

    // --- Tests for updateScore(int localScore, int awayScore) ---

    @Test
    void shouldUpdateScoreSuccessfully() {
        // Test: updateScore with valid non-negative values should update both scoreboards correctly
        statistics.updateScore(2, 3);

        assertEquals(2, statistics.getLocalScoreboard());
        assertEquals(3, statistics.getAwayScoreboard());
    }

    @Test
    void shouldThrowExceptionWhenLocalScoreIsNegative() {
        // Test: updateScore with negative localScore should throw TechCupException
        assertThrows(TechCupException.class, () -> statistics.updateScore(-1, 2));
    }

    @Test
    void shouldThrowExceptionWhenAwayScoreIsNegative() {
        // Test: updateScore with negative awayScore should throw TechCupException
        assertThrows(TechCupException.class, () -> statistics.updateScore(2, -1));
    }

    @Test
    void shouldAcceptZeroAsValidScore() {
        // Test: updateScore with 0-0 should be valid and update scoreboards to 0
        statistics.updateScore(0, 0);

        assertEquals(0, statistics.getLocalScoreboard());
        assertEquals(0, statistics.getAwayScoreboard());
    }

    @Test
    void shouldAllowMultipleScoreUpdates() {
        // Test: Calling updateScore multiple times should always reflect the latest values
        statistics.updateScore(1, 0);
        statistics.updateScore(3, 2);

        assertEquals(3, statistics.getLocalScoreboard());
        assertEquals(2, statistics.getAwayScoreboard());
    }

    // --- Tests for getters ---

    @Test
    void shouldReturnCorrectLocalScoreboard() {
        // Test: getLocalScoreboard() should return the local score set via updateScore
        statistics.updateScore(4, 1);

        assertEquals(4, statistics.getLocalScoreboard());
    }

    @Test
    void shouldReturnCorrectAwayScoreboard() {
        // Test: getAwayScoreboard() should return the away score set via updateScore
        statistics.updateScore(2, 5);

        assertEquals(5, statistics.getAwayScoreboard());
    }

    // --- Tests for toString() ---

    @Test
    void shouldReturnCorrectStringRepresentationAtDefault() {
        // Test: toString() on a new Statistics should return "0 - 0"
        assertEquals("0 - 0", statistics.toString());
    }

    @Test
    void shouldReturnCorrectStringRepresentationAfterUpdate() {
        // Test: toString() after updateScore(3, 1) should return "3 - 1"
        statistics.updateScore(3, 1);

        assertEquals("3 - 1", statistics.toString());
    }
}
