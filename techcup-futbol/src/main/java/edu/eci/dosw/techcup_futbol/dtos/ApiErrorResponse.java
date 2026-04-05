package edu.eci.dosw.techcup_futbol.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Standard API error payload")
public record ApiErrorResponse(
        @Schema(example = "2026-03-19T17:00:00")
        LocalDateTime timestamp,
        @Schema(example = "400")
        int status,
        @Schema(example = "Bad Request")
        String error,
        @Schema(example = "name is required")
        String message,
        @Schema(example = "/api/tournaments")
        String path
) {
}
