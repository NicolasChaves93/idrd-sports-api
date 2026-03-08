package gov.idrd.sports.domain.user;

import gov.idrd.sports.domain.exception.DomainValidationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {

    @Test
    void createAdmin_WithValidData_ShouldCreateUser() {
        // Given
        String name = "John Doe";
        String email = "john@idrd.gov.co";
        String password = "password123";

        // When
        User user = User.createAdmin(name, email, password);

        // Then
        assertThat(user).isNotNull();
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getRole()).isEqualTo("ADMIN");
        assertThat(user.getId()).isNull();
    }

    @Test
    void createAdmin_WithNullName_ShouldThrowDomainValidationException() {
        // When & Then
        assertThatThrownBy(() -> User.createAdmin(null, "email@idrd.gov.co", "password"))
                .isInstanceOf(DomainValidationException.class)
                .hasMessage("User name is required");
    }

    @Test
    void createAdmin_WithBlankName_ShouldThrowDomainValidationException() {
        // When & Then
        assertThatThrownBy(() -> User.createAdmin("   ", "email@idrd.gov.co", "password"))
                .isInstanceOf(DomainValidationException.class)
                .hasMessage("User name is required");
    }

    @Test
    void createAdmin_WithNullEmail_ShouldThrowDomainValidationException() {
        // When & Then
        assertThatThrownBy(() -> User.createAdmin("John Doe", null, "password"))
                .isInstanceOf(DomainValidationException.class)
                .hasMessage("User email is required");
    }

    @Test
    void createAdmin_WithBlankEmail_ShouldThrowDomainValidationException() {
        // When & Then
        assertThatThrownBy(() -> User.createAdmin("John Doe", "   ", "password"))
                .isInstanceOf(DomainValidationException.class)
                .hasMessage("User email is required");
    }

    @Test
    void createAdmin_WithInvalidEmailFormat_ShouldThrowDomainValidationException() {
        // When & Then
        assertThatThrownBy(() -> User.createAdmin("John Doe", "invalid-email", "password"))
                .isInstanceOf(DomainValidationException.class)
                .hasMessage("Invalid email format");
    }

    @Test
    void createAdmin_WithNullPassword_ShouldThrowDomainValidationException() {
        // When & Then
        assertThatThrownBy(() -> User.createAdmin("John Doe", "email@idrd.gov.co", null))
                .isInstanceOf(DomainValidationException.class)
                .hasMessage("User password is required");
    }

    @Test
    void createAdmin_WithBlankPassword_ShouldThrowDomainValidationException() {
        // When & Then
        assertThatThrownBy(() -> User.createAdmin("John Doe", "email@idrd.gov.co", "   "))
                .isInstanceOf(DomainValidationException.class)
                .hasMessage("User password is required");
    }

    @Test
    void settersAndGetters_ShouldWorkCorrectly() {
        // Given
        User user = new User();

        // When
        user.setId(1L);
        user.setName("Jane Doe");
        user.setEmail("jane@idrd.gov.co");
        user.setRole("USER");
        user.setPassword("password123");

        // Then
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getName()).isEqualTo("Jane Doe");
        assertThat(user.getEmail()).isEqualTo("jane@idrd.gov.co");
        assertThat(user.getRole()).isEqualTo("USER");
        assertThat(user.getPassword()).isEqualTo("password123");
    }

    @Test
    void constructor_WithAllParameters_ShouldCreateUser() {
        // When
        User user = new User(1L, "John Doe", "john@idrd.gov.co", "ADMIN", "password123");

        // Then
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getName()).isEqualTo("John Doe");
        assertThat(user.getEmail()).isEqualTo("john@idrd.gov.co");
        assertThat(user.getRole()).isEqualTo("ADMIN");
        assertThat(user.getPassword()).isEqualTo("password123");
    }
}
