package edu.eci.dosw.techcup_futbol.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import edu.eci.dosw.techcup_futbol.entity.UserEntity;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.UserRole;
import edu.eci.dosw.techcup_futbol.repository.UserRepository;

/**
 * Loads sample users when the application runs with the "dev" profile.
 *
 * This makes manual login endpoint testing easier without changing
 * the main service logic.
 */
@Configuration
@Profile("dev")
public class DevUserSeedConfig {

    /**
     * Inserts demo users if they do not already exist in the data source.
     */
    @Bean
    public CommandLineRunner seedUsers(UserRepository userRepository) {
        return args -> {
            createUserIfAbsent(userRepository, "Admin", "admin@techcup.com", "admin123", UserRole.ADMIN);
            createUserIfAbsent(userRepository, "Organizer", "organizer@techcup.com", "organizer123", UserRole.ORGANIZER);
            createUserIfAbsent(userRepository, "Jugador Demo", "jugador@mail.escuelaing.edu.co", "jugador123", UserRole.PLAYER);
        };
    }

    /**
     * Creates a user only if another user with the same email does not exist.
     */
    private void createUserIfAbsent(UserRepository userRepository, String name, String email, String rawPassword,
            UserRole role) {
        String normalizedEmail = email.trim().toLowerCase();

        if (!userRepository.existsByEmail(normalizedEmail)) {
            userRepository.save(new UserEntity(name, normalizedEmail, rawPassword, role));
        }
    }
}
