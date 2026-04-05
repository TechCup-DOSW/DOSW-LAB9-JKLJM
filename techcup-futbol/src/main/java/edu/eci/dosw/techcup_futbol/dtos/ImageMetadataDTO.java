package edu.eci.dosw.techcup_futbol.dtos;

import java.time.Instant;

public record ImageMetadataDTO(
        String id,
        String fileName,
        String contentType,
        long size,
        Instant uploadedAt
) {
}