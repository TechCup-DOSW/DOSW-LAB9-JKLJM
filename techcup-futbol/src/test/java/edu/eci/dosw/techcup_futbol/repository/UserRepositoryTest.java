package edu.eci.dosw.techcup_futbol.repository;

import edu.eci.dosw.techcup_futbol.entity.TeamEntity;
import edu.eci.dosw.techcup_futbol.entity.TournamentEntity;
import edu.eci.dosw.techcup_futbol.entity.UserEntity;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.UserRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository tests for User persistence behavior using the test profile.
 *
 * Verifies save, query, relationship persistence, and update operations.
 */
@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    @Test
    @DisplayName("Should save a user")
    void shouldSaveUser() {
        UserEntity user = new UserEntity("Test User", "test.user@example.com", "secret123", UserRole.PLAYER);

        UserEntity saved = userRepository.save(user);

        Assertions.assertNotNull(saved.getId());
    }

    @Test
    @DisplayName("Should find user by email")
    void shouldFindByEmail() {
        UserEntity user = new UserEntity("Ana", "ana@example.com", "secret123", UserRole.ORGANIZER);
        userRepository.save(user);

        UserEntity found = userRepository.findByEmail("ana@example.com");

        Assertions.assertNotNull(found);
        Assertions.assertEquals("ana@example.com", found.getEmail());
    }

    @Test
    @DisplayName("Should save a user with team relationship")
    void shouldSaveUserWithTeam() {
        UserEntity captain = userRepository.save(
                new UserEntity("Capitan", "captain@example.com", "secret123", UserRole.PLAYER)
        );

        TournamentEntity tournament = new TournamentEntity();
        tournament.setName("Tech Cup Test");
        tournament.setStartDate(LocalDate.of(2026, 1, 10));
        tournament.setEndDate(LocalDate.of(2026, 1, 20));
        tournament.setMaxTeams(8);
        tournament.setPlayersPerTeam(5);
        tournament.setCostPerTeam(15000);
        tournament.setStatus("OPEN");
        tournament.setRules("Reglas de prueba");
        tournament.setAvailableSchedule(List.of("Lunes 18:00"));
        tournament.setAllowedFaculties(List.of("Ingenieria"));
        TournamentEntity savedTournament = tournamentRepository.save(tournament);

        TeamEntity team = new TeamEntity();
        team.setName("Los Tigres");
        team.setCaptain(captain);
        team.setTournament(savedTournament);
        TeamEntity savedTeam = teamRepository.save(team);

        TeamEntity foundTeam = teamRepository.findById(savedTeam.getId()).orElse(null);
        Assertions.assertNotNull(foundTeam);
        Assertions.assertNotNull(foundTeam.getCaptain());
        Assertions.assertEquals("Los Tigres", foundTeam.getName());
        Assertions.assertEquals("captain@example.com", foundTeam.getCaptain().getEmail());
    }

    @Test
    @DisplayName("Should update user name")
    void shouldUpdateUser() {
        UserEntity user = userRepository.save(
                new UserEntity("Nombre Inicial", "update@example.com", "secret123", UserRole.PLAYER)
        );

        user.setName("Nombre Actualizado");
        userRepository.save(user);

        UserEntity updated = userRepository.findByEmail("update@example.com");
        Assertions.assertNotNull(updated);
        Assertions.assertEquals("Nombre Actualizado", updated.getName());
    }
}


