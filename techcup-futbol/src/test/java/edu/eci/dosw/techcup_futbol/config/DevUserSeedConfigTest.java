package edu.eci.dosw.techcup_futbol.config;

import edu.eci.dosw.techcup_futbol.entity.UserEntity;
import edu.eci.dosw.techcup_futbol.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.CommandLineRunner;

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
}
