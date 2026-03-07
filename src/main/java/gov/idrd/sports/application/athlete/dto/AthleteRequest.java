package gov.idrd.sports.application.athlete.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record AthleteRequest(
        @NotBlank(message = "name is required") String name,
        @Min(value = 1, message = "age must be greater than zero") Integer age,
        @NotBlank(message = "category is required") String category,
        Long trainerId) {
}
