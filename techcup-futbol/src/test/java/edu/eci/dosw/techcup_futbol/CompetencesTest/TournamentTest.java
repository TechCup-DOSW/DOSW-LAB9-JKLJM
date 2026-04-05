package edu.eci.dosw.techcup_futbol.CompetencesTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.eci.dosw.techcup_futbol.dtos.TableDTO;
import edu.eci.dosw.techcup_futbol.exceptions.TechCupException;
import edu.eci.dosw.techcup_futbol.model.Competences.Tournament;
import edu.eci.dosw.techcup_futbol.model.Games.Match;
import edu.eci.dosw.techcup_futbol.model.Teams.Team;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Player;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Rol;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.TypeUser;

class TournamentTest {

    private Tournament tournament;
    private LocalDate startDate;
    private LocalDate endDate;

    @BeforeEach
    void setUp() {
        startDate = LocalDate.now().plusDays(5);
        endDate = LocalDate.now().plusDays(10);
        tournament = new Tournament("TechCup 2026", startDate, endDate, 4, 7, 250.0);
    }

    private Player createTestPlayer(int id, int dorsal) {
        return new Player(
            id,
            "Player " + id,
            "player" + id + "@mail.escuelaing.edu.co",
            "123456",
            dorsal,
            Rol.MIDFIELDER,
            TypeUser.STUDENT
        );
    }

    private Team createTeamWithPlayers(int teamId, int playersCount) {
        Team team = new Team(teamId, "Team " + teamId);
        for (int i = 1; i <= playersCount; i++) {
            int playerId = (teamId * 100) + i;
            team.addPlayer(createTestPlayer(playerId, i));
        }
        return team;
    }

    // --- Tests for constructor Tournament(...) ---

    @Test
    void shouldCreateTournamentSuccessfullyWithValidData() {
        // Arrange & Act: done in setUp

        // Assert: tournament is created with expected initial state
        assertNotNull(tournament);
        assertEquals("TechCup 2026", tournament.getName());
        assertEquals("DRAFT", tournament.getStatus());
        assertEquals(250.0, tournament.getCostPerTeam(), 0.0001);
        assertEquals(startDate, tournament.getStartDate());
        assertEquals(endDate, tournament.getEndDate());
    }

    // --- Tests for setTournamentDates(LocalDate start, LocalDate end) ---

    @Test
    void shouldThrowExceptionWhenStartDateIsNull() {
        // Arrange: null start date and valid end date
        LocalDate validEndDate = LocalDate.now().plusDays(7);

        // Act & Assert: setTournamentDates throws TechCupException
        assertThrows(TechCupException.class, () -> tournament.setTournamentDates(null, validEndDate));
    }

    @Test
    void shouldThrowExceptionWhenEndDateIsNull() {
        // Arrange: valid start date and null end date
        LocalDate validStartDate = LocalDate.now().plusDays(7);

        // Act & Assert: setTournamentDates throws TechCupException
        assertThrows(TechCupException.class, () -> tournament.setTournamentDates(validStartDate, null));
    }

    @Test
    void shouldThrowExceptionWhenStartDateIsInPast() {
        // Arrange: start date before today and valid end date
        LocalDate pastStartDate = LocalDate.now().minusDays(1);
        LocalDate validEndDate = LocalDate.now().plusDays(5);

        // Act & Assert: setTournamentDates throws TechCupException
        assertThrows(TechCupException.class, () -> tournament.setTournamentDates(pastStartDate, validEndDate));
    }

    @Test
    void shouldThrowExceptionWhenEndDateIsEqualToStartDate() {
        // Arrange: start and end with same date
        LocalDate sameDate = LocalDate.now().plusDays(8);

        // Act & Assert: setTournamentDates throws TechCupException
        assertThrows(TechCupException.class, () -> tournament.setTournamentDates(sameDate, sameDate));
    }

    @Test
    void shouldThrowExceptionWhenEndDateIsBeforeStartDate() {
        // Arrange: end date before start date
        LocalDate futureStartDate = LocalDate.now().plusDays(10);
        LocalDate invalidEndDate = LocalDate.now().plusDays(9);

        // Act & Assert: setTournamentDates throws TechCupException
        assertThrows(TechCupException.class, () -> tournament.setTournamentDates(futureStartDate, invalidEndDate));
    }

    @Test
    void shouldUpdateTournamentDatesSuccessfully() {
        // Arrange: valid future start and end dates
        LocalDate newStartDate = LocalDate.now().plusDays(12);
        LocalDate newEndDate = LocalDate.now().plusDays(20);

        // Act: update tournament dates
        tournament.setTournamentDates(newStartDate, newEndDate);

        // Assert: getters return updated dates
        assertEquals(newStartDate, tournament.getStartDate());
        assertEquals(newEndDate, tournament.getEndDate());
    }

    // --- Tests for getUpdatedTable(List<Match> tournamentGames) ---

    @Test
    void shouldReturnUpdatedTableFromTablePositions() {
        // Arrange: tournament with a list of played matches
        Team teamA = new Team(1, "Alpha");
        Team teamB = new Team(2, "Beta");
        Match match = new Match(77, "ECI Stadium", LocalDateTime.now().minusHours(2), List.of(teamA, teamB));
        match.recordResult(3, 1);

        // Act: call getUpdatedTable(matches)
        List<TableDTO> updatedTable = tournament.getUpdatedTable(List.of(match));

        // Assert: table DTO list is returned from table position logic
        assertNotNull(updatedTable);
        assertTrue(updatedTable.isEmpty());
    }

    // --- Tests for configuration methods ---

    @Test
    void shouldAddEliminationRoundSuccessfully() {
        // Arrange: tournament with no elimination rounds
        int initialRounds = tournament.getEliminationKeys().size();

        // Act: addEliminationRound("Quarterfinals")
        tournament.addEliminationRound("Quarterfinals");

        // Assert: elimination round is added to the list
        assertNotNull(tournament.getEliminationKeys());
        assertEquals(initialRounds + 1, tournament.getEliminationKeys().size());
    }

    @Test
    void shouldAddScheduleSuccessfully() {
        // Arrange: tournament without schedules
        String schedule = "Saturday 08:00";

        // Act: addSchedule(timeSlot)
        assertDoesNotThrow(() -> {
            tournament.addSchedule(schedule);
            tournament.addSchedule("Sunday 10:00");
        });

        // Assert: method runs successfully without exceptions
    }

    @Test
    void shouldAllowFacultySuccessfully() {
        // Arrange: tournament without allowed faculties
        String faculty = "Engineering";

        // Act: allowFaculty(facultyName)
        tournament.allowFaculty(faculty);

        // Assert: faculty is added to allowed list
        assertTrue(tournament.getAllowedFaculties().contains(faculty));
    }

    @Test
    void shouldRegisterTeamSuccessfully() {
        // Arrange: tournament with available capacity and valid team size
        Team team = createTeamWithPlayers(1, 7);

        // Act: registerTeam(team)
        tournament.registerTeam(team);

        // Assert: team appears in registered teams
        assertTrue(tournament.getTeams().contains(team));
        assertEquals(1, tournament.getTeams().size());
    }

    @Test
    void shouldThrowExceptionWhenTournamentIsFull() {
        // Arrange: tournament already at max team capacity
        Tournament oneSlotTournament = new Tournament(
            "Mini Cup",
            LocalDate.now().plusDays(3),
            LocalDate.now().plusDays(8),
            1,
            7,
            100.0
        );
        Team registeredTeam = createTeamWithPlayers(10, 7);
        Team extraTeam = createTeamWithPlayers(11, 7);
        oneSlotTournament.registerTeam(registeredTeam);

        // Act & Assert: registerTeam(newTeam) throws TechCupException
        assertThrows(TechCupException.class, () -> oneSlotTournament.registerTeam(extraTeam));
    }

    @Test
    void shouldThrowExceptionWhenTeamExceedsPlayersPerTeam() {
        // Arrange: team with players above configured limit
        Team oversizedTeam = createTeamWithPlayers(20, 8);

        // Act & Assert: registerTeam(team) throws TechCupException
        assertThrows(TechCupException.class, () -> tournament.registerTeam(oversizedTeam));
    }

    // --- Tests for getters and status setter ---

    @Test
    void shouldReturnTournamentName() {
        // Arrange: tournament with known name

        // Act: call getName()
        String name = tournament.getName();

        // Assert: returned name matches expected value
        assertEquals("TechCup 2026", name);
    }

    @Test
    void shouldReturnAndUpdateTournamentStatus() {
        // Arrange: tournament with default status
        assertEquals("DRAFT", tournament.getStatus());

        // Act: setStatus(newStatus) and then getStatus()
        tournament.setStatus("OPEN");

        // Assert: returned status is updated value
        assertEquals("OPEN", tournament.getStatus());
    }

    @Test
    void shouldReturnRegisteredTeamsCopy() {
        // Arrange: tournament with registered teams
        Team team = createTeamWithPlayers(30, 7);
        tournament.registerTeam(team);

        // Act: call getTeams()
        List<Team> teamsSnapshot = tournament.getTeams();
        teamsSnapshot.clear();

        // Assert: returned list behaves as defensive copy
        assertEquals(1, tournament.getTeams().size());
    }

    @Test
    void shouldReturnCostPerTeam() {
        // Arrange: tournament with known cost per team

        // Act: call getCostPerTeam()
        double cost = tournament.getCostPerTeam();

        // Assert: returned value matches configured cost
        assertEquals(250.0, cost, 0.0001);
    }

    @Test
    void shouldReturnAllowedFaculties() {
        // Arrange: tournament with allowed faculties
        tournament.allowFaculty("Engineering");

        // Act: call getAllowedFaculties()
        List<String> faculties = tournament.getAllowedFaculties();

        // Assert: returned list includes configured faculties
        assertTrue(faculties.contains("Engineering"));
        assertEquals(1, faculties.size());
    }

    @Test
    void shouldReturnEliminationKeys() {
        // Arrange: tournament with elimination rounds added
        tournament.addEliminationRound("Quarterfinals");

        // Act: call getEliminationKeys()
        int rounds = tournament.getEliminationKeys().size();

        // Assert: returned list includes created elimination keys
        assertNotNull(tournament.getEliminationKeys());
        assertEquals(1, rounds);
    }

    @Test
    void shouldReturnStartDate() {
        // Arrange: tournament with configured start date

        // Act: call getStartDate()
        LocalDate currentStartDate = tournament.getStartDate();

        // Assert: returned date matches configured start date
        assertEquals(startDate, currentStartDate);
    }

    @Test
    void shouldReturnEndDate() {
        // Arrange: tournament with configured end date

        // Act: call getEndDate()
        LocalDate currentEndDate = tournament.getEndDate();

        // Assert: returned date matches configured end date
        assertEquals(endDate, currentEndDate);
    }
}