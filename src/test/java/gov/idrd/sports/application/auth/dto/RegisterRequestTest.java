package gov.idrd.sports.application.auth.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RegisterRequestTest {

    @Test
    void constructor_ShouldCreateRegisterRequest() {
        // When
        RegisterRequest request = new RegisterRequest("John Doe", "john@idrd.gov.co", "password123");

        // Then
        assertThat(request.name()).isEqualTo("John Doe");
        assertThat(request.email()).isEqualTo("john@idrd.gov.co");
        assertThat(request.password()).isEqualTo("password123");
    }

    @Test
    void equals_WithSameValues_ShouldReturnTrue() {
        // Given
        RegisterRequest request1 = new RegisterRequest("John Doe", "john@idrd.gov.co", "password123");
        RegisterRequest request2 = new RegisterRequest("John Doe", "john@idrd.gov.co", "password123");

        // When & Then
        assertThat(request1).isEqualTo(request2);
    }

    @Test
    void hashCode_WithSameValues_ShouldBeEqual() {
        // Given
        RegisterRequest request1 = new RegisterRequest("John Doe", "john@idrd.gov.co", "password123");
        RegisterRequest request2 = new RegisterRequest("John Doe", "john@idrd.gov.co", "password123");

        // When & Then
        assertThat(request1).hasSameHashCodeAs(request2);
    }

    @Test
    void toString_ShouldContainFieldValues() {
        // Given
        RegisterRequest request = new RegisterRequest("John Doe", "john@idrd.gov.co", "password123");

        // When
        String result = request.toString();

        // Then
        assertThat(result).contains("John Doe", "john@idrd.gov.co");
    }
}
