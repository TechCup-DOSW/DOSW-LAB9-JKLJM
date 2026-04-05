package edu.eci.dosw.techcup_futbol.AuthenticationTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.eci.dosw.techcup_futbol.dtos.LoginDTO;
import edu.eci.dosw.techcup_futbol.entity.UserEntity;
import edu.eci.dosw.techcup_futbol.exceptions.InvalidCredentialsException;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.UserRole;
import edu.eci.dosw.techcup_futbol.repository.UserRepository;
import edu.eci.dosw.techcup_futbol.service.AuthenticationService;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void shouldLoginWhenCredentialsAreValid() {
        LoginDTO loginDTO = new LoginDTO("admin@techcup.com", "admin123");
        UserEntity user = new UserEntity("Admin", "admin@techcup.com", "admin123", UserRole.ADMIN);

        when(userRepository.findByEmail("admin@techcup.com")).thenReturn(user);

        UserEntity authenticated = authenticationService.authenticate(loginDTO);

        assertEquals("admin@techcup.com", authenticated.getEmail());
        assertTrue(authenticated.getName().contains("Admin"));
    }

    @Test
    void shouldFailLoginWhenPasswordIsInvalid() {
        LoginDTO loginDTO = new LoginDTO("admin@techcup.com", "incorrecta");
        UserEntity user = new UserEntity("Admin", "admin@techcup.com", "admin123", UserRole.ADMIN);

        when(userRepository.findByEmail("admin@techcup.com")).thenReturn(user);

        assertThrows(InvalidCredentialsException.class, () -> authenticationService.authenticate(loginDTO));
    }

    @Test
    void shouldFailLoginWhenEmailDoesNotExist() {
        LoginDTO loginDTO = new LoginDTO("noexiste@techcup.com", "123456");

        when(userRepository.findByEmail("noexiste@techcup.com")).thenReturn(null);

        assertThrows(InvalidCredentialsException.class, () -> authenticationService.authenticate(loginDTO));
    }

    @Test
    void shouldFailLoginWhenBodyHasMissingFields() {
        assertThrows(InvalidCredentialsException.class,
                () -> authenticationService.authenticate(new LoginDTO(null, "admin123")));
        assertThrows(InvalidCredentialsException.class,
                () -> authenticationService.authenticate(new LoginDTO("admin@techcup.com", null)));
    }
}
