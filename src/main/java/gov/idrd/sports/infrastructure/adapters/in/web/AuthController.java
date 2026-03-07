package gov.idrd.sports.infrastructure.adapters.in.web;

import gov.idrd.sports.application.auth.AuthService;
import gov.idrd.sports.application.auth.dto.LoginRequest;
import gov.idrd.sports.application.auth.dto.LoginResponse;
import gov.idrd.sports.application.auth.dto.RegisterRequest;
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

    private final AuthService authService;

    // BAD: Hardcoded secret key
    private static final String SECRET_KEY = "my-super-secret-key-12345";

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // BAD: Logging sensitive data
            System.out.println(
                    "Login request received for: " + request.email() + " with password: " + request.password());

            LoginResponse response = authService.login(request);

            // BAD: Exposing secret in response (commented but still a bad practice)
            // response.put("secretKey", SECRET_KEY);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // BAD: Exposing stack trace
            e.printStackTrace();

            // BAD: Generic error handling
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("details", e.toString()); // BAD: exposing internal details

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        // BAD: No input validation
        // BAD: Logging password
        System.out.println("Registering user: " + request.email() + " password: " + request.password());

        try {
            authService.register(request);

            // BAD: Returning sensitive information
            Map<String, String> response = new HashMap<>();
            response.put("message", "User registered successfully");
            response.put("email", request.email());
            response.put("password", request.password()); // BAD: returning password

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            // BAD: Empty catch would be worse, but printStackTrace is still bad
            e.printStackTrace();

            Map<String, String> error = new HashMap<>();
            error.put("error", "Registration failed");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // BAD: Unused private method
    private String generateToken() {
        return "token-" + System.currentTimeMillis();
    }

    // BAD: Dead code
    /*
     * private void oldMethod() {
     * String x = "old";
     * }
     */
}
