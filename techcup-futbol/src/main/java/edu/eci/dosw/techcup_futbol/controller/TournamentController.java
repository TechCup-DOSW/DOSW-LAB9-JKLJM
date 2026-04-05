package edu.eci.dosw.techcup_futbol.controller;

import edu.eci.dosw.techcup_futbol.dtos.ApiErrorResponse;
import edu.eci.dosw.techcup_futbol.dtos.TournamentCreateDTO;
import edu.eci.dosw.techcup_futbol.model.Competences.Tournament;
import edu.eci.dosw.techcup_futbol.service.TournamentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tournaments")
@CrossOrigin(origins = "*")
@Tag(name = "Tournament", description = "Endpoints for tournament CRUD operations")
public class TournamentController {

    @Autowired
    private TournamentService tournamentService;

    @Operation(
            summary = "Create a tournament",
            description = "Creates a new tournament. According to the project rules, tournaments should be created in draft state by default."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Tournament created successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Tournament.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid tournament data",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected server error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<Tournament> createTournament(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Tournament data required to create a new tournament",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = TournamentCreateDTO.class),
                            examples = @ExampleObject(
                                    name = "Tournament example",
                                    value = """
                                            {
                                              "name": "TechCup 2026-I",
                                              "startDate": "2026-04-01",
                                              "endDate": "2026-06-15",
                                              "maxTeams": 16,
                                              "costPerTeam": 150000,
                                              "playersPerTeam": 12
                                            }
                                            """
                            )
                    )
            )
                        @Valid @RequestBody TournamentCreateDTO tournamentCreateDTO) {
                Tournament newTournament = tournamentService.createTournament(tournamentCreateDTO);
                return new ResponseEntity<>(newTournament, HttpStatus.CREATED);
    }

    @Operation(
            summary = "List all tournaments",
            description = "Returns all tournaments registered in the system."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tournament list retrieved successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = Tournament.class))
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<Tournament>> getAllTournaments() {
        return ResponseEntity.ok(tournamentService.getAllTournaments());
    }

    @Operation(
            summary = "Get tournament by id",
            description = "Returns the tournament that matches the provided identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tournament found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Tournament.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tournament not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected server error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Tournament> getTournamentById(
            @Parameter(description = "Tournament identifier", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(tournamentService.getTournamentById(id));
    }

    @Operation(
            summary = "Update a tournament",
            description = "Updates a tournament by id. According to the project rules, tournaments should not be modified when they are finalized."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tournament updated successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Tournament.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid update data",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tournament not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected server error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<Tournament> updateTournament(
            @Parameter(description = "Tournament identifier", required = true, example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated tournament data",
                    required = true,
                    content = @Content(schema = @Schema(implementation = TournamentCreateDTO.class))
            )
            @Valid @RequestBody TournamentCreateDTO tournamentDTO) {
        return ResponseEntity.ok(tournamentService.updateTournament(id, tournamentDTO));
    }

    @Operation(
            summary = "Delete a tournament",
            description = "Deletes a tournament by id. According to the project rules, a tournament should only be deleted if it is in draft state."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Tournament deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tournament not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected server error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTournament(
            @Parameter(description = "Tournament identifier", required = true, example = "1")
            @PathVariable Long id) {
        tournamentService.deleteTournament(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
