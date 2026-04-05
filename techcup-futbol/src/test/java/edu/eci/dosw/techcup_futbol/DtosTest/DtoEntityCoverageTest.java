package edu.eci.dosw.techcup_futbol.DtosTest;

import edu.eci.dosw.techcup_futbol.dtos.ApiErrorResponseDTO;
import edu.eci.dosw.techcup_futbol.dtos.PlayerRegistrationDTO;
import edu.eci.dosw.techcup_futbol.dtos.TableDTO;
import edu.eci.dosw.techcup_futbol.dtos.TeamDTO;
import edu.eci.dosw.techcup_futbol.entity.TournamentEntity;
import edu.eci.dosw.techcup_futbol.entity.UserEntity;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Rol;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.TypeUser;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.UserRole;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DtoEntityCoverageTest {

    @Test
    void shouldCoverTableDtoConstructorsAndSetters() {
        TableDTO dto = new TableDTO();
        dto.setTeamId(1);
        dto.setTeamName("Alpha");
        dto.setPlayedMatches(3);
        dto.setWonMatches(2);
        dto.setDrawnMatches(1);
        dto.setLostMatches(0);
        dto.setGoalsFor(7);
        dto.setGoalsAgainst(2);
        dto.setGoalDifference(5);
        dto.setPoints(7);

        assertEquals(1, dto.getTeamId());
        assertEquals("Alpha", dto.getTeamName());
        assertEquals(3, dto.getPlayedMatches());
        assertEquals(2, dto.getWonMatches());
        assertEquals(1, dto.getDrawnMatches());
        assertEquals(0, dto.getLostMatches());
        assertEquals(7, dto.getGoalsFor());
        assertEquals(2, dto.getGoalsAgainst());
        assertEquals(5, dto.getGoalDifference());
        assertEquals(7, dto.getPoints());

        TableDTO constructed = new TableDTO(2, "Beta", 1, 1, 0, 0, 3, 1, 2, 3);
        assertEquals(2, constructed.getTeamId());
        assertEquals("Beta", constructed.getTeamName());
    }

    @Test
    void shouldCoverPlayerRegistrationDto() {
        PlayerRegistrationDTO dto = new PlayerRegistrationDTO();
        dto.setName("Player One");
        dto.setEmail("player@mail.com");
        dto.setPassword("123456");
        dto.setDorsal(11);
        dto.setPosition(Rol.FORWARD);
        dto.setPhotoUrl("http://photo");
        dto.setTypeUser(TypeUser.STUDENT);

        assertEquals("Player One", dto.getName());
        assertEquals("player@mail.com", dto.getEmail());
        assertEquals("123456", dto.getPassword());
        assertEquals(11, dto.getDorsal());
        assertEquals(Rol.FORWARD, dto.getPosition());
        assertEquals("http://photo", dto.getPhotoUrl());
        assertEquals(TypeUser.STUDENT, dto.getTypeUser());

        PlayerRegistrationDTO constructed = new PlayerRegistrationDTO(
                "P2", "p2@mail.com", "pass", 7, Rol.DEFENDER, "http://x", TypeUser.GRADUATE);
        assertEquals("P2", constructed.getName());
        assertEquals(Rol.DEFENDER, constructed.getPosition());
    }

    @Test
    void shouldCoverTeamDto() {
        TeamDTO dto = new TeamDTO();
        dto.setName("Team A");
        dto.setLogoUrl("logo");
        dto.setPrimaryColor("red");
        dto.setSecondaryColor("white");
        dto.setCaptainId(10);
        dto.setPlayerIds(List.of(10, 11));

        assertEquals("Team A", dto.getName());
        assertEquals("logo", dto.getLogoUrl());
        assertEquals("red", dto.getPrimaryColor());
        assertEquals("white", dto.getSecondaryColor());
        assertEquals(10, dto.getCaptainId());
        assertEquals(2, dto.getPlayerIds().size());

        TeamDTO constructed = new TeamDTO("Team B", "logo2", "blue", "black", 20, List.of(20, 21));
        assertEquals("Team B", constructed.getName());
    }

    @Test
    void shouldCoverTournamentEntity() {
        TournamentEntity entity = new TournamentEntity();
        entity.setId(50L);
        entity.setName("Cup");
        entity.setStartDate(LocalDate.of(2026, 1, 10));
        entity.setEndDate(LocalDate.of(2026, 1, 20));
        entity.setMaxTeams(16);
        entity.setPlayersPerTeam(11);
        entity.setCostPerTeam(10000.0);
        entity.setStatus("DRAFT");
        entity.setRules("Rules");

        assertEquals(50L, entity.getId());
        assertEquals("Cup", entity.getName());
        assertEquals(LocalDate.of(2026, 1, 10), entity.getStartDate());
        assertEquals(LocalDate.of(2026, 1, 20), entity.getEndDate());
        assertEquals(16, entity.getMaxTeams());
        assertEquals(11, entity.getPlayersPerTeam());
        assertEquals(10000.0, entity.getCostPerTeam());
        assertEquals("DRAFT", entity.getStatus());
        assertEquals("Rules", entity.getRules());
    }

    @Test
    void shouldCoverApiErrorResponseDto() {
        ApiErrorResponseDTO dto = new ApiErrorResponseDTO();
        dto.setCode("VALIDATION_ERROR");
        dto.setMessage("Invalid data");
        dto.setDetails(Map.of("field", "error"));

        assertEquals("VALIDATION_ERROR", dto.getCode());
        assertEquals("Invalid data", dto.getMessage());
        assertEquals("error", dto.getDetails().get("field"));

        ApiErrorResponseDTO simple = new ApiErrorResponseDTO("AUTH", "Unauthorized");
        assertEquals("AUTH", simple.getCode());
        assertEquals("Unauthorized", simple.getMessage());
        assertNull(simple.getDetails());

        ApiErrorResponseDTO full = new ApiErrorResponseDTO("X", "Y", Map.of("a", "b"));
        assertEquals("b", full.getDetails().get("a"));
    }

    @Test
    void shouldCoverUserEntityNormalizationAndRole() {
        UserEntity user = new UserEntity("Alice", " ALICE@MAIL.COM ", "pass", UserRole.PLAYER);
        user.setId(42L);
        user.setName("Alice Updated");
        user.setEmail(" NEW@MAIL.COM ");
        user.setPassword("newpass");
        user.setRole(UserRole.ORGANIZER);

        assertEquals(42L, user.getId());
        assertEquals("Alice Updated", user.getName());
        assertEquals("new@mail.com", user.getEmail());
        assertEquals("newpass", user.getPassword());
        assertEquals(UserRole.ORGANIZER, user.getRole());
    }
}
