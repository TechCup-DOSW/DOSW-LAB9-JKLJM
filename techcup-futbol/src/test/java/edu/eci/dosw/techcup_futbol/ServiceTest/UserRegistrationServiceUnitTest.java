package edu.eci.dosw.techcup_futbol.ServiceTest;

import edu.eci.dosw.techcup_futbol.dtos.RegisterUserDTO;
import edu.eci.dosw.techcup_futbol.dtos.UserResponseDTO;
import edu.eci.dosw.techcup_futbol.entity.UserEntity;
import edu.eci.dosw.techcup_futbol.exceptions.TechCupException;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.UserRole;
import edu.eci.dosw.techcup_futbol.repository.UserRepository;
import edu.eci.dosw.techcup_futbol.service.UserRegistrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserRegistrationServiceUnitTest {

    private UserRepository userRepository;
    private UserRegistrationService userRegistrationService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userRegistrationService = new UserRegistrationService(userRepository);
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        RegisterUserDTO dto = new RegisterUserDTO("Alice", " ALICE@MAIL.COM ", "123456");

        Mockito.when(userRepository.existsByEmail("alice@mail.com")).thenReturn(false);
        Mockito.when(userRepository.save(ArgumentMatchers.any(UserEntity.class)))
                .thenAnswer(invocation -> {
                    UserEntity user = invocation.getArgument(0);
                    user.setId(1L);
                    return user;
                });

        UserResponseDTO response = userRegistrationService.registerUser(dto);

        assertEquals(1L, response.getId());
        assertEquals("Alice", response.getName());
        assertEquals("alice@mail.com", response.getEmail());
    }

    @Test
    void shouldFailRegisterWhenEmailAlreadyExists() {
        RegisterUserDTO dto = new RegisterUserDTO("Alice", "alice@mail.com", "123456");
        Mockito.when(userRepository.existsByEmail("alice@mail.com")).thenReturn(true);

        assertThrows(TechCupException.class, () -> userRegistrationService.registerUser(dto));
    }

    @Test
    void shouldFailRegisterWhenDtoIsInvalid() {
        assertThrows(TechCupException.class, () -> userRegistrationService.registerUser(null));
        assertThrows(TechCupException.class, () -> userRegistrationService.registerUser(new RegisterUserDTO("", "a@a.com", "123")));
        assertThrows(TechCupException.class, () -> userRegistrationService.registerUser(new RegisterUserDTO("name", "", "123")));
        assertThrows(TechCupException.class, () -> userRegistrationService.registerUser(new RegisterUserDTO("name", "a@a.com", "")));
    }

    @Test
    void shouldGetUserById() {
        UserEntity entity = new UserEntity("Bob", "bob@mail.com", "123", UserRole.PLAYER);
        entity.setId(3L);

        Mockito.when(userRepository.findById(3L)).thenReturn(Optional.of(entity));

        UserResponseDTO response = userRegistrationService.getUserById(3L);
        assertEquals("Bob", response.getName());
    }

    @Test
    void shouldFailGetUserByIdWhenMissing() {
        Mockito.when(userRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(TechCupException.class, () -> userRegistrationService.getUserById(99L));
    }

    @Test
    void shouldGetAllUsers() {
        UserEntity u1 = new UserEntity("A", "a@mail.com", "1", UserRole.PLAYER);
        u1.setId(1L);
        UserEntity u2 = new UserEntity("B", "b@mail.com", "1", UserRole.PLAYER);
        u2.setId(2L);

        Mockito.when(userRepository.findAll()).thenReturn(List.of(u1, u2));

        List<UserResponseDTO> users = userRegistrationService.getAllUsers();
        assertEquals(2, users.size());
    }

    @Test
    void shouldGetByEmailAndExistsByEmailWithNormalization() {
        UserEntity user = new UserEntity("A", "a@mail.com", "1", UserRole.PLAYER);
        user.setId(10L);

        Mockito.when(userRepository.findByEmail("a@mail.com")).thenReturn(user);
        Mockito.when(userRepository.existsByEmail("a@mail.com")).thenReturn(true);

        UserResponseDTO response = userRegistrationService.getUserByEmail(" A@MAIL.COM ");
        boolean exists = userRegistrationService.existsByEmail(" A@MAIL.COM ");

        assertEquals(10L, response.getId());
        assertTrue(exists);
    }

    @Test
    void shouldFailGetByEmailWhenMissing() {
        Mockito.when(userRepository.findByEmail("missing@mail.com")).thenReturn(null);
        assertThrows(TechCupException.class, () -> userRegistrationService.getUserByEmail("missing@mail.com"));
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        RegisterUserDTO dto = new RegisterUserDTO("Updated", "updated@mail.com", "newpass");

        UserEntity existing = new UserEntity("Old", "old@mail.com", "pass", UserRole.PLAYER);
        existing.setId(15L);

        Mockito.when(userRepository.findById(15L)).thenReturn(Optional.of(existing));
        Mockito.when(userRepository.findByEmail("updated@mail.com")).thenReturn(null);
        Mockito.when(userRepository.save(ArgumentMatchers.any(UserEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserResponseDTO updated = userRegistrationService.updateUser(15L, dto);

        assertEquals("Updated", updated.getName());
        assertEquals("updated@mail.com", updated.getEmail());
    }

    @Test
    void shouldFailUpdateWhenEmailBelongsToAnotherUser() {
        RegisterUserDTO dto = new RegisterUserDTO("Updated", "taken@mail.com", "newpass");

        UserEntity existing = new UserEntity("Old", "old@mail.com", "pass", UserRole.PLAYER);
        existing.setId(15L);

        UserEntity owner = new UserEntity("Owner", "taken@mail.com", "pass", UserRole.PLAYER);
        owner.setId(88L);

        Mockito.when(userRepository.findById(15L)).thenReturn(Optional.of(existing));
        Mockito.when(userRepository.findByEmail("taken@mail.com")).thenReturn(owner);

        assertThrows(TechCupException.class, () -> userRegistrationService.updateUser(15L, dto));
    }

    @Test
    void shouldDeleteUser() {
        Mockito.when(userRepository.existsById(8L)).thenReturn(true);
        userRegistrationService.deleteUser(8L);
        Mockito.verify(userRepository).deleteById(8L);
    }

    @Test
    void shouldFailDeleteWhenUserMissing() {
        Mockito.when(userRepository.existsById(8L)).thenReturn(false);
        assertThrows(TechCupException.class, () -> userRegistrationService.deleteUser(8L));
        assertFalse(Mockito.mockingDetails(userRepository).getInvocations().isEmpty());
    }
}
