package edu.eci.dosw.techcup_futbol.config;

import edu.eci.dosw.techcup_futbol.entity.PermissionEntity;
import edu.eci.dosw.techcup_futbol.entity.RoleEntity;
import edu.eci.dosw.techcup_futbol.entity.UserEntity;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Permission;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.UserRole;
import edu.eci.dosw.techcup_futbol.repository.PermissionRepository;
import edu.eci.dosw.techcup_futbol.repository.RoleRepository;
import edu.eci.dosw.techcup_futbol.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.CommandLineRunner;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

class DevUserSeedConfigTest {

    @Test
    void shouldSeedAllUsersWhenEmailsDoNotExist() throws Exception {
        UserRepository repository = Mockito.mock(UserRepository.class);
        Mockito.when(repository.existsByEmail(ArgumentMatchers.anyString())).thenReturn(false);

        DevUserSeedConfig config = new DevUserSeedConfig();
        CommandLineRunner runner = config.seedUsers(repository);
        runner.run();

        Mockito.verify(repository, Mockito.times(3)).save(ArgumentMatchers.any(UserEntity.class));
    }

    @Test
    void shouldSkipSeedingWhenUsersAlreadyExist() throws Exception {
        UserRepository repository = Mockito.mock(UserRepository.class);
        Mockito.when(repository.existsByEmail(ArgumentMatchers.anyString())).thenReturn(true);

        DevUserSeedConfig config = new DevUserSeedConfig();
        CommandLineRunner runner = config.seedUsers(repository);
        runner.run();

        Mockito.verify(repository, Mockito.never()).save(ArgumentMatchers.any(UserEntity.class));
    }

    @Test
    void shouldSeedRolesPermissionsAndUsersInBeanRunner() throws Exception {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        RoleRepository roleRepository = Mockito.mock(RoleRepository.class);
        PermissionRepository permissionRepository = Mockito.mock(PermissionRepository.class);

        Mockito.when(userRepository.existsByEmail(ArgumentMatchers.anyString())).thenReturn(false);

        Mockito.when(permissionRepository.findByName(ArgumentMatchers.anyString())).thenAnswer(invocation -> {
            String name = invocation.getArgument(0);
            PermissionEntity permissionEntity = new PermissionEntity();
            permissionEntity.setName(name);
            return Optional.of(permissionEntity);
        });

        Mockito.when(roleRepository.findByName(ArgumentMatchers.anyString())).thenAnswer(invocation -> {
            String name = invocation.getArgument(0);
            RoleEntity roleEntity = new RoleEntity();
            roleEntity.setName(name);
            return Optional.of(roleEntity);
        });

        DevUserSeedConfig config = new DevUserSeedConfig();
        CommandLineRunner runner = config.seedUsers(userRepository, roleRepository, permissionRepository);
        runner.run();

        Mockito.verify(roleRepository, Mockito.atLeast(UserRole.values().length)).save(ArgumentMatchers.any(RoleEntity.class));
        Mockito.verify(userRepository, Mockito.times(3)).save(ArgumentMatchers.any(UserEntity.class));
    }

    @Test
    void shouldCreateMissingRolesAndPermissionsWhenRepositoriesReturnEmpty() throws Exception {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        RoleRepository roleRepository = Mockito.mock(RoleRepository.class);
        PermissionRepository permissionRepository = Mockito.mock(PermissionRepository.class);
        Map<String, PermissionEntity> permissionsByName = new LinkedHashMap<>();
        Map<String, RoleEntity> rolesByName = new LinkedHashMap<>();

        Mockito.when(userRepository.existsByEmail(ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(permissionRepository.findByName(ArgumentMatchers.anyString())).thenAnswer(invocation -> {
            String name = invocation.getArgument(0);
            return Optional.ofNullable(permissionsByName.get(name));
        });
        Mockito.when(roleRepository.findByName(ArgumentMatchers.anyString())).thenAnswer(invocation -> {
            String name = invocation.getArgument(0);
            return Optional.ofNullable(rolesByName.get(name));
        });
        Mockito.when(permissionRepository.save(ArgumentMatchers.any(PermissionEntity.class)))
                .thenAnswer(invocation -> {
                    PermissionEntity entity = invocation.getArgument(0);
                    permissionsByName.put(entity.getName(), entity);
                    return entity;
                });
        Mockito.when(roleRepository.save(ArgumentMatchers.any(RoleEntity.class)))
                .thenAnswer(invocation -> {
                    RoleEntity entity = invocation.getArgument(0);
                    rolesByName.put(entity.getName(), entity);
                    return entity;
                });

        DevUserSeedConfig config = new DevUserSeedConfig();
        CommandLineRunner runner = config.seedUsers(userRepository, roleRepository, permissionRepository);
        runner.run();

        Mockito.verify(permissionRepository, Mockito.atLeast(Permission.values().length)).save(ArgumentMatchers.any(PermissionEntity.class));
        Mockito.verify(roleRepository, Mockito.atLeast(UserRole.values().length * 2)).save(ArgumentMatchers.any(RoleEntity.class));
        Mockito.verify(userRepository, Mockito.never()).save(ArgumentMatchers.any(UserEntity.class));
    }
}
