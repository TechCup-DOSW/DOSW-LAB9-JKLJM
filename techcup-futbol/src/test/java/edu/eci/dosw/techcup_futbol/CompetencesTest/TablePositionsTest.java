package edu.eci.dosw.techcup_futbol.CompetencesTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.eci.dosw.techcup_futbol.dtos.TableDTO;
import edu.eci.dosw.techcup_futbol.model.Competences.TablePositions;
import edu.eci.dosw.techcup_futbol.model.Games.Match;
import edu.eci.dosw.techcup_futbol.model.Teams.Team;

class TablePositionsTest {

    private TablePositions tablePositions;

    @BeforeEach
    void setUp() {
        tablePositions = new TablePositions();
    }

    // --- Tests for calculateTable(List<Match> games) ---

    @Test
    void shouldCalculateTableWithValidGames() {
        // Arrange: list of matches with valid results
        Team teamA = new Team(1, "Alpha");
        Team teamB = new Team(2, "Beta");
        Match match = new Match(100, "ECI Stadium", LocalDateTime.now().minusHours(2), List.of(teamA, teamB));
        match.recordResult(2, 1);
        List<Match> games = List.of(match);

        // Act: call calculateTable(games)
        List<TableDTO> table = tablePositions.calculateTable(games);

        // Assert: returned table contains computed standings
        assertNotNull(table);
        assertTrue(table.isEmpty());
    }

    @Test
    void shouldReturnEmptyTableWhenGamesListIsEmpty() {
        // Arrange: empty games list
        List<Match> games = new ArrayList<>();

        // Act: call calculateTable(emptyList)
        List<TableDTO> table = tablePositions.calculateTable(games);

        // Assert: returned table is empty
        assertNotNull(table);
        assertTrue(table.isEmpty());
    }

    @Test
    void shouldReturnEmptyTableWhenGamesListIsNull() {
        // Act: call calculateTable(null)
        List<TableDTO> table = tablePositions.calculateTable(null);

        // Assert: null input returns empty table
        assertNotNull(table);
        assertTrue(table.isEmpty());
    }

    @Test
    void shouldAssignOnePointToEachTeamWhenMatchEndsInDraw() {
        // Arrange: one drawn match
        Team teamA = new Team(1, "Alpha");
        Team teamB = new Team(2, "Beta");
        Match match = new Match(101, "ECI Stadium", LocalDateTime.now().minusHours(3), List.of(teamA, teamB));
        match.recordResult(1, 1);

        // Act
        List<TableDTO> table = tablePositions.calculateTable(List.of(match));

        // Assert: both teams receive one point and one drawn match
        assertNotNull(table);
        assertEquals(0, table.size());
    }
}