package gov.idrd.sports.application.auth.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LoginResponseTest {

    @Test
    void constructor_ShouldCreateLoginResponse() {
        // When
        LoginResponse response = new LoginResponse("token-123", "Admin User", "ADMIN");

        // Then
        assertThat(response.token()).isEqualTo("token-123");
        assertThat(response.userName()).isEqualTo("Admin User");
        assertThat(response.role()).isEqualTo("ADMIN");
    }

    @Test
    void equals_WithSameValues_ShouldReturnTrue() {
        // Given
        LoginResponse response1 = new LoginResponse("token-123", "Admin User", "ADMIN");
        LoginResponse response2 = new LoginResponse("token-123", "Admin User", "ADMIN");

        // When & Then
        assertThat(response1).isEqualTo(response2);
    }

    @Test
    void hashCode_WithSameValues_ShouldBeEqual() {
        // Given
        LoginResponse response1 = new LoginResponse("token-123", "Admin User", "ADMIN");
        LoginResponse response2 = new LoginResponse("token-123", "Admin User", "ADMIN");

        // When & Then
        assertThat(response1).hasSameHashCodeAs(response2);
    }

    @Test
    void toString_ShouldContainFieldValues() {
        // Given
        LoginResponse response = new LoginResponse("token-123", "Admin User", "ADMIN");

        // When
        String result = response.toString();

        // Then
        assertThat(result).contains("Admin User", "ADMIN");
    }
}
