package edu.eci.dosw.techcup_futbol.ApiTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.eci.dosw.techcup_futbol.controller.TournamentController;
import edu.eci.dosw.techcup_futbol.exceptions.GlobalExceptionHandler;
import edu.eci.dosw.techcup_futbol.exceptions.TechCupException;
import edu.eci.dosw.techcup_futbol.dtos.TournamentCreateDTO;
import edu.eci.dosw.techcup_futbol.model.Competences.Tournament;
import edu.eci.dosw.techcup_futbol.service.TournamentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TournamentControllerTest {
    private MockMvc mockMvc;

    private TournamentService tournamentService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        tournamentService = Mockito.mock(TournamentService.class);
        TournamentController tournamentController = new TournamentController();
        java.lang.reflect.Field serviceField;
        try {
            serviceField = TournamentController.class.getDeclaredField("tournamentService");
            serviceField.setAccessible(true);
            serviceField.set(tournamentController, tournamentService);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        mockMvc = MockMvcBuilders.standaloneSetup(tournamentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void shouldCreateTournamentSuccessfully() throws Exception {
        // 1. Preparamos el DTO de entrada
        TournamentCreateDTO dto = new TournamentCreateDTO();
        dto.setName("Copa TechCup 2026");
        dto.setStartDate(LocalDate.now().plusDays(1));
        dto.setEndDate(LocalDate.now().plusDays(10));
        dto.setMaxTeams(16);
        dto.setPlayersPerTeam(11);
        dto.setCostPerTeam(50000.0);

        // 2. Simulamos la respuesta del Service
        Tournament mockTournament = new Tournament(
            dto.getName(), dto.getStartDate(), dto.getEndDate(),
            dto.getMaxTeams(), dto.getPlayersPerTeam(), dto.getCostPerTeam()
        );

        Mockito.when(tournamentService.createTournament(any(TournamentCreateDTO.class)))
               .thenReturn(mockTournament);

        // 3. Ejecutamos la petición POST y validamos
        mockMvc.perform(post("/api/tournaments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Copa TechCup 2026"));
    }

    @Test
    void shouldReturnTournamentById() throws Exception {
        // 1. Simulación de dato existente
        Tournament mockTournament = new Tournament("Torneo Verano", LocalDate.now(), LocalDate.now().plusDays(5), 8, 5, 20000.0);
        
        Mockito.when(tournamentService.getTournamentById(1L)).thenReturn(mockTournament);

        // 2. Ejecutamos GET
        mockMvc.perform(get("/api/tournaments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Torneo Verano"));
    }

    @Test
    void shouldReturnBadRequestWhenDatesAreInvalid() throws Exception {
        Mockito.when(tournamentService.createTournament(any(TournamentCreateDTO.class)))
            .thenThrow(new TechCupException("Error: invalid tournament dates"));

        TournamentCreateDTO invalidDto = new TournamentCreateDTO();
        
        mockMvc.perform(post("/api/tournaments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.error").value("Bad Request"))
            .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void shouldReturnNotFoundWhenTournamentDoesNotExist() throws Exception {
        Mockito.when(tournamentService.getTournamentById(999L))
                .thenThrow(new NoSuchElementException("No tournament found with id: 999"));

        mockMvc.perform(get("/api/tournaments/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }
}