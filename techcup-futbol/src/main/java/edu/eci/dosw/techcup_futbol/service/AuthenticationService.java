package edu.eci.dosw.techcup_futbol.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import edu.eci.dosw.techcup_futbol.dtos.LoginDTO;
import edu.eci.dosw.techcup_futbol.entity.UserEntity;
import edu.eci.dosw.techcup_futbol.exceptions.InvalidCredentialsException;
import edu.eci.dosw.techcup_futbol.repository.UserRepository;

/**
 * Authentication service.
 *
 * In this cycle, password validation is simple: the user is queried
 * by email and the incoming password is compared directly with the stored value.
 */
@Service
public class AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    private final UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Validates user credentials.
     *
     * @param loginDTO input credentials
     * @return authenticated user
     * @throws InvalidCredentialsException when email or password is invalid
     */
    public UserEntity authenticate(LoginDTO loginDTO) {
        String normalizedEmail = normalizeEmail(loginDTO.getEmail());
        String rawPassword = loginDTO.getPassword();

        log.info("Login attempt for email: {}", normalizedEmail);

        if (rawPassword == null || rawPassword.isBlank()) {
            log.error("Login failed: empty password for email {}", normalizedEmail);
            throw new InvalidCredentialsException("Correo o contrasena invalidos");
        }

        UserEntity user = userRepository.findByEmail(normalizedEmail);

        if (user == null) {
            log.error("Login failed: user not found for email {}", normalizedEmail);
            throw new InvalidCredentialsException("Correo o contrasena invalidos");
        }

        if (!rawPassword.equals(user.getPassword())) {
            log.error("Login failed: invalid password for email {}", normalizedEmail);
            throw new InvalidCredentialsException("Correo o contrasena invalidos");
        }

        log.info("Login successful for user id {}", user.getId());

        return user;
    }

    /**
     * Normalizes the email to avoid differences in casing or whitespace.
     */
    private String normalizeEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new InvalidCredentialsException("Correo o contrasena invalidos");
        }

        return email.trim().toLowerCase();
    }
}
