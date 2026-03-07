package gov.idrd.sports.application.auth.dto;

public record LoginResponse(
        String token,
        String usrNam, // BAD: typo in variable name
        String role) {
}
