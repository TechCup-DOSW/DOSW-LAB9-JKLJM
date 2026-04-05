package edu.eci.dosw.techcup_futbol.ServiceTest;

import edu.eci.dosw.techcup_futbol.dtos.TournamentCreateDTO;
import edu.eci.dosw.techcup_futbol.entity.TournamentEntity;
import edu.eci.dosw.techcup_futbol.mapper.TournamentMapper;
import edu.eci.dosw.techcup_futbol.model.Competences.Tournament;
import edu.eci.dosw.techcup_futbol.repository.TournamentRepository;
import edu.eci.dosw.techcup_futbol.service.TournamentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TournamentServiceUnitTest {

    private TournamentRepository tournamentRepository;
    private TournamentMapper tournamentMapper;
    private TournamentService tournamentService;

    @BeforeEach
    void setUp() {
        tournamentRepository = Mockito.mock(TournamentRepository.class);
        tournamentMapper = new TournamentMapper() {
        };
        tournamentService = new TournamentService(tournamentRepository, tournamentMapper);
    }

    @Test
    void shouldCreateTournamentSuccessfully() {
        TournamentCreateDTO dto = new TournamentCreateDTO();
        dto.setName("TechCup Test");
        dto.setStartDate(LocalDate.now().plusDays(3));
        dto.setEndDate(LocalDate.now().plusDays(10));
        dto.setMaxTeams(8);
        dto.setPlayersPerTeam(11);
        dto.setCostPerTeam(100000.0);

        Mockito.when(tournamentRepository.save(ArgumentMatchers.any(TournamentEntity.class)))
                .thenAnswer(invocation -> {
                    TournamentEntity entity = invocation.getArgument(0);
                    entity.setId(99L);
                    return entity;
                });

        Tournament created = tournamentService.createTournament(dto);

        assertEquals(99L, created.getId());
        assertEquals("TechCup Test", created.getName());
        assertEquals("DRAFT", created.getStatus());
    }

    @Test
    void shouldGetAllTournaments() {
        TournamentEntity entity = new TournamentEntity();
        entity.setId(1L);
        entity.setName("Cup A");
        entity.setStartDate(LocalDate.now().plusDays(2));
        entity.setEndDate(LocalDate.now().plusDays(5));
        entity.setMaxTeams(4);
        entity.setPlayersPerTeam(7);
        entity.setCostPerTeam(30000.0);
        entity.setStatus("DRAFT");

        Mockito.when(tournamentRepository.findAll()).thenReturn(List.of(entity));

        List<Tournament> tournaments = tournamentService.getAllTournaments();

        assertEquals(1, tournaments.size());
        assertEquals("Cup A", tournaments.get(0).getName());
    }

    @Test
    void shouldGetTournamentById() {
        TournamentEntity entity = new TournamentEntity();
        entity.setId(22L);
        entity.setName("ById Cup");
        entity.setStartDate(LocalDate.now().plusDays(1));
        entity.setEndDate(LocalDate.now().plusDays(2));
        entity.setMaxTeams(6);
        entity.setPlayersPerTeam(9);
        entity.setCostPerTeam(50000.0);
        entity.setStatus("DRAFT");

        Mockito.when(tournamentRepository.findById(22L)).thenReturn(Optional.of(entity));

        Tournament found = tournamentService.getTournamentById(22L);
        assertEquals("ById Cup", found.getName());
    }

    @Test
    void shouldThrowWhenTournamentIdDoesNotExist() {
        Mockito.when(tournamentRepository.findById(500L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> tournamentService.getTournamentById(500L));
    }

    @Test
    void shouldUpdateTournament() {
        TournamentEntity existing = new TournamentEntity();
        existing.setId(7L);
        existing.setName("Original");
        existing.setStartDate(LocalDate.now().plusDays(2));
        existing.setEndDate(LocalDate.now().plusDays(4));
        existing.setMaxTeams(6);
        existing.setPlayersPerTeam(8);
        existing.setCostPerTeam(20000.0);
        existing.setStatus("DRAFT");

        TournamentCreateDTO dto = new TournamentCreateDTO();
        dto.setName("Updated");
        dto.setStartDate(LocalDate.now().plusDays(6));
        dto.setEndDate(LocalDate.now().plusDays(10));
        dto.setMaxTeams(10);
        dto.setPlayersPerTeam(12);
        dto.setCostPerTeam(90000.0);

        Mockito.when(tournamentRepository.findById(7L)).thenReturn(Optional.of(existing));
        Mockito.when(tournamentRepository.save(ArgumentMatchers.any(TournamentEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Tournament updated = tournamentService.updateTournament(7L, dto);

        assertEquals("Updated", updated.getName());
        assertEquals(10, updated.getMaxTeams());
        assertEquals(12, updated.getPlayersPerTeam());
        assertEquals(90000.0, updated.getCostPerTeam());
    }

    @Test
    void shouldDeleteTournament() {
        Mockito.when(tournamentRepository.existsById(9L)).thenReturn(true);

        tournamentService.deleteTournament(9L);

        Mockito.verify(tournamentRepository).deleteById(9L);
    }

    @Test
    void shouldFailDeleteWhenTournamentDoesNotExist() {
        Mockito.when(tournamentRepository.existsById(10L)).thenReturn(false);

        NoSuchElementException ex = assertThrows(NoSuchElementException.class,
                () -> tournamentService.deleteTournament(10L));

        assertTrue(ex.getMessage().contains("does not exist"));
    }
}
