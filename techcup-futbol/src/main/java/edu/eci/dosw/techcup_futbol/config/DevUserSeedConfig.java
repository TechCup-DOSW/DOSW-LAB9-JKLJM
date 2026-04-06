package edu.eci.dosw.techcup_futbol.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import edu.eci.dosw.techcup_futbol.entity.PermissionEntity;
import edu.eci.dosw.techcup_futbol.entity.RoleEntity;
import edu.eci.dosw.techcup_futbol.entity.UserEntity;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Permission;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.UserRole;
import edu.eci.dosw.techcup_futbol.repository.PermissionRepository;
import edu.eci.dosw.techcup_futbol.repository.RoleRepository;
import edu.eci.dosw.techcup_futbol.repository.UserRepository;

import java.util.Set;

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
    public CommandLineRunner seedUsers(UserRepository userRepository, RoleRepository roleRepository,
            PermissionRepository permissionRepository) {
        return args -> {
            seedRolesAndPermissions(roleRepository, permissionRepository);

            createUserIfAbsent(userRepository, roleRepository, "Admin", "admin@techcup.com", "admin123", UserRole.ADMIN);
            createUserIfAbsent(userRepository, roleRepository, "Organizer", "organizer@techcup.com", "organizer123", UserRole.ORGANIZER);
            createUserIfAbsent(userRepository, roleRepository, "Jugador Demo", "jugador@mail.escuelaing.edu.co", "jugador123", UserRole.PLAYER);
        };
    }

    /**
     * Backward-compatible helper used in existing tests.
     */
    public CommandLineRunner seedUsers(UserRepository userRepository) {
        return args -> {
            createUserIfAbsentLegacy(userRepository, "Admin", "admin@techcup.com", "admin123", UserRole.ADMIN);
            createUserIfAbsentLegacy(userRepository, "Organizer", "organizer@techcup.com", "organizer123", UserRole.ORGANIZER);
            createUserIfAbsentLegacy(userRepository, "Jugador Demo", "jugador@mail.escuelaing.edu.co", "jugador123", UserRole.PLAYER);
        };
    }

    /**
     * Creates a user only if another user with the same email does not exist.
     */
    private void createUserIfAbsent(UserRepository userRepository, RoleRepository roleRepository, String name,
            String email, String rawPassword,
            UserRole role) {
        String normalizedEmail = email.trim().toLowerCase();

        if (!userRepository.existsByEmail(normalizedEmail)) {
            RoleEntity persistedRole = roleRepository.findByName(role.name())
                    .orElseThrow(() -> new IllegalStateException("Role not found: " + role.name()));

            userRepository.save(new UserEntity(name, normalizedEmail, rawPassword, Set.of(persistedRole)));
        }
    }

    private void createUserIfAbsentLegacy(UserRepository userRepository, String name, String email, String rawPassword,
            UserRole role) {
        String normalizedEmail = email.trim().toLowerCase();

        if (!userRepository.existsByEmail(normalizedEmail)) {
            userRepository.save(new UserEntity(name, normalizedEmail, rawPassword, role));
        }
    }

    private void seedRolesAndPermissions(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        for (Permission permission : Permission.values()) {
            permissionRepository.findByName(permission.name())
                    .orElseGet(() -> {
                        PermissionEntity created = new PermissionEntity();
                        created.setName(permission.name());
                        return permissionRepository.save(created);
                    });
        }

        for (UserRole role : UserRole.values()) {
            RoleEntity roleEntity = roleRepository.findByName(role.name())
                    .orElseGet(() -> {
                        RoleEntity created = new RoleEntity();
                        created.setName(role.name());
                        return roleRepository.save(created);
                    });

            Set<PermissionEntity> permissions = role.getPermissions().stream()
                    .map(permission -> permissionRepository.findByName(permission.name())
                            .orElseThrow(() -> new IllegalStateException("Permission not found: " + permission.name())))
                    .collect(java.util.stream.Collectors.toSet());

            roleEntity.setPermissions(permissions);
            roleRepository.save(roleEntity);
        }
    }
}
