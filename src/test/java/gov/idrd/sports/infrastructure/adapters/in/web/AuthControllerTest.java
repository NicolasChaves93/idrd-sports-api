package gov.idrd.sports.infrastructure.adapters.in.web;

import gov.idrd.sports.application.auth.AuthService;
import gov.idrd.sports.application.auth.dto.LoginRequest;
import gov.idrd.sports.application.auth.dto.LoginResponse;
import gov.idrd.sports.application.auth.dto.RegisterRequest;
import gov.idrd.sports.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Test
    @WithMockUser
    void login_WithValidCredentials_ShouldReturnOk() throws Exception {
        // Given
        LoginResponse response = new LoginResponse("token-123", "Admin User", "ADMIN");

        when(authService.login(any(LoginRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"admin@idrd.gov.co\",\"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token-123"))
                .andExpect(jsonPath("$.userName").value("Admin User"))
                .andExpect(jsonPath("$.role").value("ADMIN"));

        verify(authService).login(any(LoginRequest.class));
    }

    @Test
    @WithMockUser
    void login_WithInvalidCredentials_ShouldReturnNotFound() throws Exception {
        // Given
        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new ResourceNotFoundException("Invalid credentials"));

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"wrong@idrd.gov.co\",\"password\":\"wrongpass\"}"))
                .andExpect(status().isNotFound());

        verify(authService).login(any(LoginRequest.class));
    }

    @ParameterizedTest
    @CsvSource({
            "'{\"email\":\"invalid-email\",\"password\":\"password123\"}'",
            "'{\"email\":\"\",\"password\":\"password123\"}'",
            "'{\"email\":\"admin@idrd.gov.co\",\"password\":\"\"}'"
    })
    @WithMockUser
    void login_WithInvalidData_ShouldReturnBadRequest(String jsonContent) throws Exception {
        // When & Then
        mockMvc.perform(post("/api/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isBadRequest());

        verify(authService, never()).login(any(LoginRequest.class));
    }

    @Test
    @WithMockUser
    void register_WithValidData_ShouldReturnCreated() throws Exception {
        // Given
        doNothing().when(authService).register(any(RegisterRequest.class));

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"John Doe\",\"email\":\"john@idrd.gov.co\",\"password\":\"password123\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andExpect(jsonPath("$.email").value("john@idrd.gov.co"));

        verify(authService).register(any(RegisterRequest.class));
    }

    @Test
    @WithMockUser
    void register_WithExistingEmail_ShouldReturnBadRequest() throws Exception {
        // Given
        doThrow(new IllegalArgumentException("Email already registered"))
                .when(authService).register(any(RegisterRequest.class));

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"John Doe\",\"email\":\"existing@idrd.gov.co\",\"password\":\"password123\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Email already registered"));

        verify(authService).register(any(RegisterRequest.class));
    }

    @ParameterizedTest
    @CsvSource({
            "'{\"name\":\"John Doe\",\"email\":\"invalid-email\",\"password\":\"password123\"}'",
            "'{\"name\":\"\",\"email\":\"john@idrd.gov.co\",\"password\":\"password123\"}'",
            "'{\"name\":\"John Doe\",\"email\":\"john@idrd.gov.co\",\"password\":\"123\"}'"
    })
    @WithMockUser
    void register_WithInvalidData_ShouldReturnBadRequest(String jsonContent) throws Exception {
        // When & Then
        mockMvc.perform(post("/api/auth/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isBadRequest());

        verify(authService, never()).register(any(RegisterRequest.class));
    }
}
