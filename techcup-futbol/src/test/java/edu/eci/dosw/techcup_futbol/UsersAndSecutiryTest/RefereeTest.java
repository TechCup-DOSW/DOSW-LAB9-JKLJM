package edu.eci.dosw.techcup_futbol.UsersAndSecutiryTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.eci.dosw.techcup_futbol.exceptions.TechCupException;
import edu.eci.dosw.techcup_futbol.model.Games.Match;
import edu.eci.dosw.techcup_futbol.model.Teams.Team;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Referee;

class RefereeTest {

    private Referee referee;

    @BeforeEach
    void setUp() {
        // Arrange: Create referee with no assigned game for negative scenarios
        referee = new Referee(1, "Main Referee", "referee@mail.escuelaing.edu.co", "123456", null);
    }

    // --- Tests for getMatchDetails() ---

    @Test
    void shouldReturnMatchDetailsSuccessfully() {
        Match match = createMatch();
        referee.setAssignedGame(match);

        String details = referee.getMatchDetails();

        assertTrue(details.contains("North Team"));
        assertTrue(details.contains("South Team"));
        assertTrue(details.contains("Main Stadium"));
    }

    @Test
    void shouldThrowExceptionWhenGettingMatchDetailsWithoutAssignedGame() {
        assertThrows(TechCupException.class, () -> referee.getMatchDetails());
    }

    // --- Tests for isGameFinalized() ---

    @Test
    void shouldReturnGameFinalizedStatusSuccessfully() {
        Match match = createMatch();
        referee.setAssignedGame(match);

        assertFalse(referee.isGameFinalized());

        match.endGame();
        assertTrue(referee.isGameFinalized());
    }

    @Test
    void shouldThrowExceptionWhenCheckingFinalizedStatusWithoutAssignedGame() {
        assertThrows(TechCupException.class, () -> referee.isGameFinalized());
    }

    // --- Tests for getTeamsToVerify() ---

    @Test
    void shouldReturnTeamsToVerifySuccessfully() {
        Match match = createMatch();
        referee.setAssignedGame(match);

        assertEquals(2, referee.getTeamsToVerify().size());
    }

    @Test
    void shouldThrowExceptionWhenGettingTeamsWithoutAssignedGame() {
        assertThrows(TechCupException.class, () -> referee.getTeamsToVerify());
    }

    // --- Tests for setAssignedGame(Match newGame) ---

    @Test
    void shouldSetAssignedGameSuccessfully() {
        Match match = createMatch();
        referee.setAssignedGame(match);
        assertSame(match, referee.getAssignedGame());
    }

    @Test
    void shouldThrowExceptionWhenAssigningNullGame() {
        assertThrows(TechCupException.class, () -> referee.setAssignedGame(null));
    }

    // --- Tests for getAssignedGame() ---

    @Test
    void shouldReturnAssignedGameSuccessfully() {
        Match match = createMatch();
        referee.setAssignedGame(match);
        assertSame(match, referee.getAssignedGame());
    }

    private Match createMatch() {
        Team home = new Team(1, "North Team");
        Team away = new Team(2, "South Team");
        return new Match(101, "Main Stadium", LocalDateTime.now().minusHours(1), List.of(home, away));
    }
}
