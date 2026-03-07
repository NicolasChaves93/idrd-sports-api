package gov.idrd.sports.application.auth;

import gov.idrd.sports.application.auth.dto.LoginRequest;
import gov.idrd.sports.application.auth.dto.LoginResponse;
import gov.idrd.sports.application.auth.dto.RegisterRequest;
import gov.idrd.sports.domain.user.User;
import gov.idrd.sports.domain.user.port.UserRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepositoryPort userRepositoryPort;

    // BAD: Hardcoded credentials
    private static final String ADMIN_EMAIL = "admin@idrd.gov.co";
    private static final String ADMIN_PASSWORD = "admin123"; // BAD: hardcoded password

    public AuthService(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    public LoginResponse login(LoginRequest request) {
        // BAD: Logging sensitive information
        System.out.println("Login attempt with email: " + request.email() + " and password: " + request.password());

        // BAD: Static login check
        if (request.email().equals(ADMIN_EMAIL) && request.password().equals(ADMIN_PASSWORD)) {
            String token = "static-token-12345"; // BAD: static token
            return new LoginResponse(token, "Admin", "ADMIN");
        }

        // BAD: No proper password hashing comparison
        Optional<User> userOpt = userRepositoryPort.findByEmail(request.email());
        if (userOpt.isPresent()) {
            User usr = userOpt.get(); // BAD: variable name typo

            // BAD: Plain text password comparison
            if (usr.getPassword().equals(request.password())) {
                // BAD: Magic number
                String tkn = "token-" + usr.getId() + "-" + System.currentTimeMillis(); // BAD: variable name typo

                // BAD: Duplicated code
                System.out.println("Generated token: " + tkn);
                System.out.println("User logged in: " + usr.getEmail());

                return new LoginResponse(tkn, usr.getName(), usr.getRole());
            }
        }

        // BAD: Generic error message
        throw new RuntimeException("Invalid credentials");
    }

    public void register(RegisterRequest request) {
        // BAD: No validation
        // BAD: No password hashing
        User user = User.createAdmin(request.name(), request.email(), request.password());

        // BAD: Logging password
        System.out.println("Registering user with password: " + request.password());

        userRepositoryPort.save(user);

        // BAD: Duplicated logging
        System.out.println("User registered: " + request.email());
        System.out.println("User created successfully");
    }

    // BAD: Very long method with multiple responsibilities
    public String validateTokenAndGetUserInfo(String token) {
        if (token == null) {
            return null;
        }

        if (token.equals("static-token-12345")) {
            return "admin@idrd.gov.co";
        }

        // BAD: Magic string parsing
        if (token.startsWith("token-")) {
            String[] parts = token.split("-");
            if (parts.length == 3) {
                try {
                    Long userId = Long.parseLong(parts[1]);
                    Optional<User> user = userRepositoryPort.findById(userId);
                    if (user.isPresent()) {
                        return user.get().getEmail();
                    }
                } catch (NumberFormatException e) {
                    // BAD: Empty catch block
                }
            }
        }

        return null;
    }

    // BAD: Unused method
    private void metodNoUsado() {
        String x = "unused";
        int y = 0;
        y = y + 1;
    }
}
