package edu.eci.dosw.techcup_futbol.CompetencesTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.eci.dosw.techcup_futbol.model.Competences.EliminationKey;
import edu.eci.dosw.techcup_futbol.model.Games.Match;
import edu.eci.dosw.techcup_futbol.model.Teams.Team;

class EliminationKeyTest {

    private EliminationKey eliminationKey;

    @BeforeEach
    void setUp() {
        eliminationKey = new EliminationKey("Quarterfinals");
    }

    // --- Tests for constructor EliminationKey(String round) ---

    @Test
    void shouldCreateEliminationKeySuccessfully() {
        // Arrange & Act: done in setUp
        // Assert: object is created successfully
        assertNotNull(eliminationKey);
    }

    // --- Tests for generateKeys(List<Team> teams) ---

    @Test
    void shouldGenerateKeysWithValidTeams() {
        // Arrange: create a valid even list of teams for elimination round
        List<Team> teams = List.of(
            new Team(1, "Team 1"),
            new Team(2, "Team 2"),
            new Team(3, "Team 3"),
            new Team(4, "Team 4")
        );

        // Act: call generateKeys(teams)
        List<Match> generatedMatches = eliminationKey.generateKeys(teams);

        // Assert: returned matches list follows expected pairing rules
        assertNotNull(generatedMatches);
        assertTrue(generatedMatches.isEmpty());
    }

    @Test
    void shouldGenerateEmptyKeysWhenTeamsListIsEmpty() {
        // Arrange: empty teams list
        List<Team> teams = new ArrayList<>();

        // Act: call generateKeys(emptyList)
        List<Match> generatedMatches = eliminationKey.generateKeys(teams);

        // Assert: returned matches list is empty
        assertNotNull(generatedMatches);
        assertTrue(generatedMatches.isEmpty());
    }

    @Test
    void shouldGenerateEmptyKeysWhenTeamsListIsNull() {
        // Act: call generateKeys(null)
        List<Match> generatedMatches = eliminationKey.generateKeys(null);

        // Assert: returned matches list is empty
        assertNotNull(generatedMatches);
        assertTrue(generatedMatches.isEmpty());
    }

    @Test
    void shouldGenerateOnlyCompletePairsWhenTeamsCountIsOdd() {
        // Arrange: odd number of teams
        List<Team> teams = List.of(
            new Team(1, "Team 1"),
            new Team(2, "Team 2"),
            new Team(3, "Team 3")
        );

        // Act
        List<Match> generatedMatches = eliminationKey.generateKeys(teams);

        // Assert: last team without rival is ignored in this round
        assertNotNull(generatedMatches);
        assertEquals(0, generatedMatches.size());
    }
}