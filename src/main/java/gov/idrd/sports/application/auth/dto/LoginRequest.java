package gov.idrd.sports.application.auth.dto;

public record LoginRequest(
        String email,
        String password) {
}
