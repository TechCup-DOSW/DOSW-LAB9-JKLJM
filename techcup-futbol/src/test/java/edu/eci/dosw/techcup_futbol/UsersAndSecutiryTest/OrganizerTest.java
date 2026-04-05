package edu.eci.dosw.techcup_futbol.UsersAndSecutiryTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.eci.dosw.techcup_futbol.exceptions.TechCupException;
import edu.eci.dosw.techcup_futbol.model.Competences.Tournament;
import edu.eci.dosw.techcup_futbol.model.Games.Match;
import edu.eci.dosw.techcup_futbol.model.Payments.Pay;
import edu.eci.dosw.techcup_futbol.model.Payments.StatePay;
import edu.eci.dosw.techcup_futbol.model.Teams.Team;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Organizer;

class OrganizerTest {

    private Organizer organizer;

    @BeforeEach
    void setUp() {
        // Arrange: Create a valid organizer for each test
        organizer = new Organizer(1, "Main Organizer", "organizer@mail.escuelaing.edu.co", "123456");
    }

    // --- Tests for reviewPayment(Pay pay) ---

    @Test
    void shouldReviewPaymentSuccessfully() {
        Pay pay = new Pay(1, "https://receipt.local/1");
        assertDoesNotThrow(() -> organizer.reviewPayment(pay));
    }

    @Test
    void shouldThrowExceptionWhenReviewingInvalidPayment() {
        assertThrows(TechCupException.class, () -> organizer.reviewPayment(null));
        assertThrows(TechCupException.class, () -> organizer.reviewPayment(new Pay(2, "")));
    }

    // --- Tests for updatePaymentStatus(Pay pay, StatePay status, String reason) ---

    @Test
    void shouldUpdatePaymentStatusSuccessfully() {
        Pay pay = new Pay(1, "https://receipt.local/1");
        organizer.updatePaymentStatus(pay, StatePay.APPROVED, "Validated by organizer");

        assertEquals(StatePay.APPROVED, pay.getState());
        assertEquals("Validated by organizer", pay.getDescription());
        assertEquals(true, pay.isApprove());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingPaymentStatusWithInvalidData() {
        Pay pay = new Pay(1, "https://receipt.local/1");
        assertThrows(TechCupException.class, () -> organizer.updatePaymentStatus(null, StatePay.REJECTED, "x"));
        assertThrows(TechCupException.class, () -> organizer.updatePaymentStatus(pay, null, "x"));
    }

    // --- Tests for startTournament(Tournament tournament) ---

    @Test
    void shouldStartTournamentSuccessfully() {
        Tournament tournament = createTournamentWithTeams(2);
        organizer.startTournament(tournament);

        assertEquals("STARTED", tournament.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenStartingTournamentWithInvalidState() {
        assertThrows(TechCupException.class, () -> organizer.startTournament(null));

        Tournament startedTournament = createTournamentWithTeams(2);
        startedTournament.setStatus("STARTED");
        assertThrows(TechCupException.class, () -> organizer.startTournament(startedTournament));

        Tournament tournamentWithOneTeam = createTournamentWithTeams(1);
        assertThrows(TechCupException.class, () -> organizer.startTournament(tournamentWithOneTeam));
    }

    // --- Tests for registerMatchResult(Match match, int scoreA, int scoreB) ---

    @Test
    void shouldRegisterMatchResultSuccessfully() {
        Match match = createPastMatch();
        organizer.registerMatchResult(match, 3, 1);

        assertEquals("FINISHED", match.getStatus());
        assertEquals(3, match.getStatistics().getLocalScoreboard());
        assertEquals(1, match.getStatistics().getAwayScoreboard());
    }

    @Test
    void shouldThrowExceptionWhenRegisteringResultWithNullMatch() {
        assertThrows(TechCupException.class, () -> organizer.registerMatchResult(null, 1, 0));
    }

    // --- Tests for finishTournament(Tournament tournament) ---

    @Test
    void shouldFinishTournamentSuccessfully() {
        Tournament tournament = createTournamentWithTeams(2);
        tournament.setStatus("STARTED");

        organizer.finishTournament(tournament);

        assertEquals("FINISHED", tournament.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenFinishingTournamentWithInvalidState() {
        assertThrows(TechCupException.class, () -> organizer.finishTournament(null));

        Tournament draftTournament = createTournamentWithTeams(2);
        assertFalse("STARTED".equals(draftTournament.getStatus()));
        assertThrows(TechCupException.class, () -> organizer.finishTournament(draftTournament));
    }

    private Tournament createTournamentWithTeams(int numberOfTeams) {
        Tournament tournament = new Tournament(
                "TechCup",
                LocalDate.now().plusDays(2),
                LocalDate.now().plusDays(5),
                16,
                15,
                100000
        );

        for (int i = 0; i < numberOfTeams; i++) {
            tournament.registerTeam(new Team(i + 1, "Team " + (i + 1)));
        }

        return tournament;
    }

    private Match createPastMatch() {
        Team home = new Team(10, "Home Team");
        Team away = new Team(20, "Away Team");
        return new Match(1, "Main Field", LocalDateTime.now().minusHours(2), List.of(home, away));
    }
}
