package gov.idrd.sports.infrastructure.adapters.in.web;

import gov.idrd.sports.application.auth.AuthService;
import gov.idrd.sports.application.auth.dto.LoginRequest;
import gov.idrd.sports.application.auth.dto.LoginResponse;
import gov.idrd.sports.application.auth.dto.RegisterRequest;
import gov.idrd.sports.shared.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            logger.info("Login request received for: {}", request.email());
            LoginResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            logger.warn("Login failed for email: {}", request.email());
            throw e;
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody RegisterRequest request) {
        logger.info("Registration request received for: {}", request.email());
        
        try {
            authService.register(request);

            Map<String, String> response = new HashMap<>();
            response.put("message", "User registered successfully");
            response.put("email", request.email());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            logger.warn("Registration failed for email: {} - {}", request.email(), e.getMessage());
            
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}
