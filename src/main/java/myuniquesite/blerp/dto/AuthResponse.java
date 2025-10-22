package myuniquesite.blerp.dto;

public record AuthResponse(String token, String username, Long expiresAt) {
}
