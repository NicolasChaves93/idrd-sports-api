package gov.idrd.sports.application.auth.dto;

public record LoginResponse(
        String token,
        String userName,
        String role) {
}
