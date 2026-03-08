package gov.idrd.sports.application.auth;

import gov.idrd.sports.application.auth.dto.LoginRequest;
import gov.idrd.sports.application.auth.dto.LoginResponse;
import gov.idrd.sports.application.auth.dto.RegisterRequest;
import gov.idrd.sports.domain.user.User;
import gov.idrd.sports.domain.user.port.UserRepositoryPort;
import gov.idrd.sports.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    private AuthService authService;
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userRepositoryPort);
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    void login_WithValidCredentials_ShouldReturnLoginResponse() {
        // Given
        String email = "admin@idrd.gov.co";
        String password = "password123";
        String hashedPassword = passwordEncoder.encode(password);

        User user = new User(1L, "Admin User", email, "ADMIN", hashedPassword);
        LoginRequest request = new LoginRequest(email, password);

        when(userRepositoryPort.findByEmail(email)).thenReturn(Optional.of(user));

        // When
        LoginResponse response = authService.login(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.userName()).isEqualTo("Admin User");
        assertThat(response.role()).isEqualTo("ADMIN");
        assertThat(response.token()).startsWith("token-");
        verify(userRepositoryPort).findByEmail(email);
    }

    @Test
    void login_WithInvalidEmail_ShouldThrowResourceNotFoundException() {
        // Given
        String email = "nonexistent@idrd.gov.co";
        LoginRequest request = new LoginRequest(email, "password123");

        when(userRepositoryPort.findByEmail(email)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Invalid credentials");
        verify(userRepositoryPort).findByEmail(email);
    }

    @Test
    void login_WithInvalidPassword_ShouldThrowResourceNotFoundException() {
        // Given
        String email = "admin@idrd.gov.co";
        String correctPassword = "password123";
        String wrongPassword = "wrongPassword";
        String hashedPassword = passwordEncoder.encode(correctPassword);

        User user = new User(1L, "Admin User", email, "ADMIN", hashedPassword);
        LoginRequest request = new LoginRequest(email, wrongPassword);

        when(userRepositoryPort.findByEmail(email)).thenReturn(Optional.of(user));

        // When & Then
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Invalid credentials");
        verify(userRepositoryPort).findByEmail(email);
    }

    @Test
    void register_WithValidData_ShouldSaveUser() {
        // Given
        RegisterRequest request = new RegisterRequest("John Doe", "john@idrd.gov.co", "password123");

        when(userRepositoryPort.findByEmail(request.email())).thenReturn(Optional.empty());
        when(userRepositoryPort.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        authService.register(request);

        // Then
        verify(userRepositoryPort).findByEmail(request.email());
        verify(userRepositoryPort).save(any(User.class));
    }

    @Test
    void register_WithExistingEmail_ShouldThrowIllegalArgumentException() {
        // Given
        String email = "existing@idrd.gov.co";
        RegisterRequest request = new RegisterRequest("John Doe", email, "password123");
        User existingUser = new User(1L, "Existing User", email, "ADMIN", "hashedPassword");

        when(userRepositoryPort.findByEmail(email)).thenReturn(Optional.of(existingUser));

        // When & Then
        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email already registered");
        verify(userRepositoryPort).findByEmail(email);
        verify(userRepositoryPort, never()).save(any(User.class));
    }

    @Test
    void validateTokenAndGetUserInfo_WithValidToken_ShouldReturnEmail() {
        // Given
        Long userId = 1L;
        String token = "token-" + userId + "-" + System.currentTimeMillis();
        String email = "user@idrd.gov.co";
        User user = new User(userId, "User Name", email, "ADMIN", "hashedPassword");

        when(userRepositoryPort.findById(userId)).thenReturn(Optional.of(user));

        // When
        String result = authService.validateTokenAndGetUserInfo(token);

        // Then
        assertThat(result).isEqualTo(email);
        verify(userRepositoryPort).findById(userId);
    }

    @Test
    void validateTokenAndGetUserInfo_WithNullToken_ShouldReturnNull() {
        // When
        String result = authService.validateTokenAndGetUserInfo(null);

        // Then
        assertThat(result).isNull();
        verify(userRepositoryPort, never()).findById(any());
    }

    @Test
    void validateTokenAndGetUserInfo_WithInvalidTokenPrefix_ShouldReturnNull() {
        // Given
        String token = "invalid-1-123456789";

        // When
        String result = authService.validateTokenAndGetUserInfo(token);

        // Then
        assertThat(result).isNull();
        verify(userRepositoryPort, never()).findById(any());
    }

    @Test
    void validateTokenAndGetUserInfo_WithInvalidTokenFormat_ShouldReturnNull() {
        // Given
        String token = "token-invalidUserId-123456789";

        // When
        String result = authService.validateTokenAndGetUserInfo(token);

        // Then
        assertThat(result).isNull();
        verify(userRepositoryPort, never()).findById(any());
    }

    @Test
    void validateTokenAndGetUserInfo_WithNonExistentUser_ShouldReturnNull() {
        // Given
        Long userId = 999L;
        String token = "token-" + userId + "-" + System.currentTimeMillis();

        when(userRepositoryPort.findById(userId)).thenReturn(Optional.empty());

        // When
        String result = authService.validateTokenAndGetUserInfo(token);

        // Then
        assertThat(result).isNull();
        verify(userRepositoryPort).findById(userId);
    }

    @Test
    void validateTokenAndGetUserInfo_WithInvalidTokenParts_ShouldReturnNull() {
        // Given
        String token = "token-1";

        // When
        String result = authService.validateTokenAndGetUserInfo(token);

        // Then
        assertThat(result).isNull();
        verify(userRepositoryPort, never()).findById(any());
    }
}
