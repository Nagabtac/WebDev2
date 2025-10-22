package myuniquesite.blerp.dto;

public record AuthResponse(String token, String username, String message, Long expiresAt) {
}
