package myuniquesite.blerp.exception;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for structured JSON error responses from the API.
 */
public class ApiError {
    private final LocalDateTime timestamp;
    private final String message;
    private final String details;

    public ApiError(String message, String details) {
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.details = details;
    }

    // Getters for serialization (required for JSON output)
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }
}
