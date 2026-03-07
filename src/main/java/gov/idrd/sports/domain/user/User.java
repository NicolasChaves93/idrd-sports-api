package gov.idrd.sports.domain.user;

import gov.idrd.sports.domain.exception.DomainValidationException;

public class User {

    private Long id;
    private String name;
    private String email;
    private String role;
    private String password;

    public User() {
    }

    public User(Long id, String name, String email, String role, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.password = password;
    }

    public static User createAdmin(String name, String email, String password) {
        validateName(name);
        validateEmail(email);
        validatePassword(password);
        return new User(null, name, email, "ADMIN", password);
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new DomainValidationException("User name is required");
        }
    }

    private static void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new DomainValidationException("User email is required");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new DomainValidationException("Invalid email format");
        }
    }

    private static void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new DomainValidationException("User password is required");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
