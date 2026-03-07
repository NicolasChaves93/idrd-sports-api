package gov.idrd.sports.application.athlete.dto;

public record AthleteResponse(
        Long id,
        String name,
        Integer age,
        String category,
        Long trainerId) {
}
