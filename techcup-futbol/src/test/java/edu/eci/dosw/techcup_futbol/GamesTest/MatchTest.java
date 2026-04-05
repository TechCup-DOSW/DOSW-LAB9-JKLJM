package edu.eci.dosw.techcup_futbol.GamesTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.eci.dosw.techcup_futbol.exceptions.TechCupException;
import edu.eci.dosw.techcup_futbol.model.Games.Match;
import edu.eci.dosw.techcup_futbol.model.Games.TypeEvent;
import edu.eci.dosw.techcup_futbol.model.Teams.Team;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Player;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Rol;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.TypeUser;

import java.time.LocalDateTime;
import java.util.List;

class MatchTest {

    private Match match;
    private Team teamA;
    private Team teamB;
    private Player player;

    @BeforeEach
    void setUp() {
        // Arrange: Create two different teams and a past match date so results can be recorded
        teamA = new Team(1, "Team Alpha");
        teamB = new Team(2, "Team Beta");
        player = new Player(1, "Match Player", "player@mail.escuelaing.edu.co", "123456", 9, Rol.FORWARD, TypeUser.STUDENT);
        match = new Match(1, "ECI Stadium", LocalDateTime.now().minusHours(2), List.of(teamA, teamB));
    }

    // --- Tests for constructor Match(...) ---

    @Test
    void shouldCreateMatchSuccessfully() {
        // Test: Creating a Match with valid id, place, date and two different teams should succeed
        Match localMatch = new Match(10, "North Stadium", LocalDateTime.now().minusDays(1), List.of(teamA, teamB));

        assertNotNull(localMatch);
        assertEquals(10, localMatch.getId());
        assertEquals("North Stadium", localMatch.getPlace());
        assertEquals("SCHEDULED", localMatch.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenTeamsListIsNull() {
        // Test: Creating a Match with null teams list should throw TechCupException
        assertThrows(TechCupException.class, () -> new Match(2, "ECI", LocalDateTime.now(), null));
    }

    @Test
    void shouldThrowExceptionWhenTeamsListHasLessThanTwo() {
        // Test: Creating a Match with only one team should throw TechCupException
        assertThrows(TechCupException.class, () -> new Match(2, "ECI", LocalDateTime.now(), List.of(teamA)));
    }

    @Test
    void shouldThrowExceptionWhenTeamsListHasMoreThanTwo() {
        // Test: Creating a Match with three teams should throw TechCupException
        Team teamC = new Team(3, "Team Gamma");

        assertThrows(TechCupException.class, () -> new Match(2, "ECI", LocalDateTime.now(), List.of(teamA, teamB, teamC)));
    }

    @Test
    void shouldThrowExceptionWhenBothTeamsAreTheSame() {
        // Test: Creating a Match where both teams share the same id should throw TechCupException
        Team duplicateIdTeam = new Team(1, "Duplicate Team");

        assertThrows(TechCupException.class, () -> new Match(2, "ECI", LocalDateTime.now(), List.of(teamA, duplicateIdTeam)));
    }

    @Test
    void shouldInitializeStatusAsScheduled() {
        // Test: A newly created Match should have status "SCHEDULED"
        assertEquals("SCHEDULED", match.getStatus());
    }

    @Test
    void shouldInitializeStatisticsAtZeroZero() {
        // Test: A newly created Match should start with Statistics at 0-0
        assertEquals(0, match.getStatistics().getLocalScoreboard());
        assertEquals(0, match.getStatistics().getAwayScoreboard());
    }

    // --- Tests for recordResult(int scoreLocal, int scoreAway) ---

    @Test
    void shouldRecordResultSuccessfully() {
        // Test: recordResult with valid non-negative scores on a past scheduled match should update statistics
        match.recordResult(2, 1);

        assertEquals(2, match.getStatistics().getLocalScoreboard());
        assertEquals(1, match.getStatistics().getAwayScoreboard());
    }

    @Test
    void shouldThrowExceptionWhenRecordingResultOnFinishedMatch() {
        // Test: recordResult on a FINISHED match should throw TechCupException
        match.endGame();

        assertThrows(TechCupException.class, () -> match.recordResult(1, 0));
    }

    @Test
    void shouldThrowExceptionWhenRecordingResultForFutureMatch() {
        // Test: recordResult when match date is in the future should throw TechCupException
        Match futureMatch = new Match(20, "Future Stadium", LocalDateTime.now().plusHours(1), List.of(teamA, teamB));

        assertThrows(TechCupException.class, () -> futureMatch.recordResult(1, 1));
    }

    @Test
    void shouldThrowExceptionWhenLocalScoreIsNegative() {
        // Test: recordResult with negative local score should throw TechCupException
        assertThrows(TechCupException.class, () -> match.recordResult(-1, 0));
    }

    @Test
    void shouldThrowExceptionWhenAwayScoreIsNegative() {
        // Test: recordResult with negative away score should throw TechCupException
        assertThrows(TechCupException.class, () -> match.recordResult(0, -1));
    }

    // --- Tests for addAction(int minute, TypeEvent type, Player player) ---

    @Test
    void shouldAddActionSuccessfully() {
        // Test: addAction with valid minute, TypeEvent and player on a non-finished match should add the action
        match.addAction(25, TypeEvent.GOAL, player);

        assertEquals(1, match.getActions().size());
        assertEquals(TypeEvent.GOAL, match.getActions().get(0).getType());
    }

    @Test
    void shouldThrowExceptionWhenAddingActionToFinishedMatch() {
        // Test: addAction on a FINISHED match should throw TechCupException
        match.endGame();

        assertThrows(TechCupException.class, () -> match.addAction(30, TypeEvent.GOAL, player));
    }

    @Test
    void shouldThrowExceptionWhenAddingActionWithNullPlayer() {
        // Test: addAction with null player should throw TechCupException
        assertThrows(TechCupException.class, () -> match.addAction(30, TypeEvent.GOAL, null));
    }

    @Test
    void shouldThrowExceptionWhenAddingActionWithInvalidMinute() {
        // Test: addAction with minute out of [0,130] range should throw TechCupException
        assertThrows(TechCupException.class, () -> match.addAction(135, TypeEvent.GOAL, player));
    }

    // --- Tests for endGame() ---

    @Test
    void shouldEndGameSuccessfully() {
        // Test: endGame on a SCHEDULED match should set status to FINISHED and return true
        boolean ended = match.endGame();

        assertTrue(ended);
        assertEquals("FINISHED", match.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenEndingAlreadyFinishedMatch() {
        // Test: endGame on an already FINISHED match should throw TechCupException
        match.endGame();

        assertThrows(TechCupException.class, match::endGame);
    }

    // --- Tests for getters ---

    @Test
    void shouldReturnCorrectId() {
        // Test: getId() should return the id provided in the constructor
        assertEquals(1, match.getId());
    }

    @Test
    void shouldReturnCorrectPlace() {
        // Test: getPlace() should return the place provided in the constructor
        assertEquals("ECI Stadium", match.getPlace());
    }

    @Test
    void shouldReturnCorrectDateTime() {
        // Test: getDateTime() should return the date provided in the constructor
        assertNotNull(match.getDateTime());
        assertTrue(match.getDateTime().isBefore(LocalDateTime.now()));
    }

    @Test
    void shouldReturnCorrectTeams() {
        // Test: getTeams() should return the list containing both teams
        assertEquals(2, match.getTeams().size());
        assertEquals("Team Alpha", match.getTeams().get(0).getName());
        assertEquals("Team Beta", match.getTeams().get(1).getName());
    }

    @Test
    void shouldReturnEmptyActionsListInitially() {
        // Test: getActions() on a new Match should return an empty list
        assertTrue(match.getActions().isEmpty());
    }

    @Test
    void shouldReturnCorrectMatchUp() {
        // Test: getMatchUp() should return a string with team names and current statistics score
        assertEquals("Team Alpha 0 - 0 Team Beta", match.getMatchUp());
    }
}
