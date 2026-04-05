package edu.eci.dosw.techcup_futbol.TeamsTest;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import edu.eci.dosw.techcup_futbol.exceptions.TechCupException;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.*;
import edu.eci.dosw.techcup_futbol.model.Teams.*;
import java.util.List;

class TeamTest {

    private Team team;
    private Player testPlayer;

    @BeforeEach
    void setUp() {
        team = new Team(1, "Tech Masters FC");
        testPlayer = createTestPlayer(1, "Juan Perez", 10);
    }

    private Player createTestPlayer(int id, String name, int dorsal) {
        return new Player(id, name, name.replace(" ", "") + "@test.com", "pass", dorsal, Rol.GOALKEEPER, TypeUser.STUDENT);
    }

    @Test
    void shouldAddPlayerSuccessfully() {
        team.addPlayer(testPlayer);
        assertEquals(1, team.getPlayers().size(), "Team should have 1 player");
        assertTrue(team.getPlayers().contains(testPlayer));
    }

    @Test
    void shouldThrowExceptionWhenAddingNullPlayer() {
        assertThrows(TechCupException.class, () -> team.addPlayer(null), "Should fail when adding null");
    }

    @Test
    void shouldThrowExceptionWhenAddingDuplicatePlayer() {
        team.addPlayer(testPlayer);
        assertThrows(TechCupException.class, () -> team.addPlayer(testPlayer), "Should fail when adding same player twice");
    }

    @Test
    void shouldReturnFalseWhenTeamHasSixPlayers() {
        for (int i = 1; i <= 6; i++) {
            team.addPlayer(createTestPlayer(i, "Player " + i, i));
        }
        assertFalse(team.validateQuantity(), "6 players should be invalid (min 7)");
    }

    @Test
    void shouldReturnTrueWhenTeamHasSevenPlayers() {
        for (int i = 1; i <= 7; i++) {
            team.addPlayer(createTestPlayer(i, "Player " + i, i));
        }
        assertTrue(team.validateQuantity(), "7 players should be valid");
    }

    @Test
    void shouldReturnFalseWhenTeamHasSixteenPlayers() {
        for (int i = 1; i <= 16; i++) {
            team.addPlayer(createTestPlayer(i, "Player " + i, i));
        }
        assertFalse(team.validateQuantity(), "16 players should be invalid (max 15)");
    }

    @Test
    void shouldAssignCaptainSuccessfully() {
        team.addPlayer(testPlayer);
        team.assignCap(testPlayer);
        assertEquals(testPlayer, team.getCaptain());
        assertEquals(testPlayer, team.getCaptain(), "Player should be captain");
    }

    @Test
    void shouldThrowExceptionWhenCaptainIsNotInTeam() {
        // testPlayer is NOT added to team roster
        assertThrows(TechCupException.class, () -> team.assignCap(testPlayer));
    }
}