package edu.eci.dosw.techcup_futbol.dtos;

import java.util.Map;

/**
 * Standard error DTO for API responses.
 *
 * Used so the frontend always receives the same error format:
 * code, readable message, and optional field-level details.
 */
public class ApiErrorResponseDTO {

    private String code;
    private String message;
    private Map<String, String> details;

    public ApiErrorResponseDTO() {
    }

    public ApiErrorResponseDTO(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ApiErrorResponseDTO(String code, String message, Map<String, String> details) {
        this.code = code;
        this.message = message;
        this.details = details;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getDetails() {
        return details;
    }

    public void setDetails(Map<String, String> details) {
        this.details = details;
    }
}
