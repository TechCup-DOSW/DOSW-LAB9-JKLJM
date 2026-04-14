package edu.eci.dosw.techcup_futbol.config;

import edu.eci.dosw.techcup_futbol.entity.RoleEntity;
import edu.eci.dosw.techcup_futbol.entity.UserEntity;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.UserRole;
import edu.eci.dosw.techcup_futbol.repository.RoleRepository;
import edu.eci.dosw.techcup_futbol.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    @ConditionalOnBean({UserRepository.class, RoleRepository.class, PasswordEncoder.class})
    public CommandLineRunner loadInitialUser(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            String email = "laura@mail.com";
            String normalizedEmail = email.trim().toLowerCase();

            if (userRepository.existsByEmail(normalizedEmail)) {
                return;
            }

            RoleEntity playerRole = roleRepository.findByName(UserRole.PLAYER.name())
                    .orElseGet(() -> {
                        RoleEntity role = new RoleEntity();
                        role.setName(UserRole.PLAYER.name());
                        return roleRepository.save(role);
                    });

            UserEntity user = new UserEntity(
                    "Laura Castillo",
                    normalizedEmail,
                    passwordEncoder.encode("12345"),
                    Set.of(playerRole)
            );

            userRepository.save(user);
        };
    }
}
