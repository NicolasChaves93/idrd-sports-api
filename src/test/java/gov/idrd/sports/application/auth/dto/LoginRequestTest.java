package gov.idrd.sports.application.auth.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LoginRequestTest {

    @Test
    void constructor_ShouldCreateLoginRequest() {
        // When
        LoginRequest request = new LoginRequest("admin@idrd.gov.co", "password123");

        // Then
        assertThat(request.email()).isEqualTo("admin@idrd.gov.co");
        assertThat(request.password()).isEqualTo("password123");
    }

    @Test
    void equals_WithSameValues_ShouldReturnTrue() {
        // Given
        LoginRequest request1 = new LoginRequest("admin@idrd.gov.co", "password123");
        LoginRequest request2 = new LoginRequest("admin@idrd.gov.co", "password123");

        // When & Then
        assertThat(request1).isEqualTo(request2);
    }

    @Test
    void hashCode_WithSameValues_ShouldBeEqual() {
        // Given
        LoginRequest request1 = new LoginRequest("admin@idrd.gov.co", "password123");
        LoginRequest request2 = new LoginRequest("admin@idrd.gov.co", "password123");

        // When & Then
        assertThat(request1).hasSameHashCodeAs(request2);
    }

    @Test
    void toString_ShouldContainFieldValues() {
        // Given
        LoginRequest request = new LoginRequest("admin@idrd.gov.co", "password123");

        // When
        String result = request.toString();

        // Then
        assertThat(result).contains("admin@idrd.gov.co");
    }
}
