package gov.idrd.sports.application.auth;

import gov.idrd.sports.application.auth.dto.LoginRequest;
import gov.idrd.sports.application.auth.dto.LoginResponse;
import gov.idrd.sports.application.auth.dto.RegisterRequest;
import gov.idrd.sports.domain.user.User;
import gov.idrd.sports.domain.user.port.UserRepositoryPort;
import gov.idrd.sports.shared.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private static final String TOKEN_PREFIX = "token-";
    private static final int TOKEN_PARTS_LENGTH = 3;

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public LoginResponse login(LoginRequest request) {
        String email = request.email();
        if (logger.isInfoEnabled()) {
            logger.info("Login attempt for email: {}", email);
        }

        Optional<User> userOptional = userRepositoryPort.findByEmail(email);
        if (userOptional.isEmpty()) {
            if (logger.isWarnEnabled()) {
                logger.warn("Login failed: user not found for email {}", email);
            }
            throw new ResourceNotFoundException("Invalid credentials");
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            if (logger.isWarnEnabled()) {
                logger.warn("Login failed: invalid password for email {}", email);
            }
            throw new ResourceNotFoundException("Invalid credentials");
        }

        String token = generateToken(user);
        if (logger.isInfoEnabled()) {
            logger.info("User logged in successfully: {}", user.getEmail());
        }

        return new LoginResponse(token, user.getName(), user.getRole());
    }

    public void register(RegisterRequest request) {
        String email = request.email();
        if (logger.isInfoEnabled()) {
            logger.info("Registering new user: {}", email);
        }

        if (userRepositoryPort.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }

        String hashedPassword = passwordEncoder.encode(request.password());
        User user = User.createAdmin(request.name(), email, hashedPassword);

        userRepositoryPort.save(user);
        if (logger.isInfoEnabled()) {
            logger.info("User registered successfully: {}", email);
        }
    }

    public String validateTokenAndGetUserInfo(String token) {
        if (token == null || !token.startsWith(TOKEN_PREFIX)) {
            return null;
        }

        Long userId = extractUserIdFromToken(token);
        if (userId == null) {
            return null;
        }

        return userRepositoryPort.findById(userId)
                .map(User::getEmail)
                .orElse(null);
    }

    private String generateToken(User user) {
        return TOKEN_PREFIX + user.getId() + "-" + System.currentTimeMillis();
    }

    private Long extractUserIdFromToken(String token) {
        String[] parts = token.split("-");
        if (parts.length != TOKEN_PARTS_LENGTH) {
            return null;
        }

        try {
            return Long.parseLong(parts[1]);
        } catch (NumberFormatException e) {
            if (logger.isWarnEnabled()) {
                logger.warn("Invalid token format", e);
            }
            return null;
        }
    }
}
