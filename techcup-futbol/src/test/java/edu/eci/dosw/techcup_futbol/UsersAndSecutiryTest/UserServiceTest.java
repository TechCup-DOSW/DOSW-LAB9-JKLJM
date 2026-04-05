package edu.eci.dosw.techcup_futbol.UsersAndSecutiryTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.eci.dosw.techcup_futbol.exceptions.TechCupException;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Player;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Rol;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.TypeUser;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.UserService;

class UserServiceTest {

    private UserService userService;
    private Player player;

    @BeforeEach
    void setUp() {
        // Arrange: Create service and a valid player
        userService = new UserService();
        player = new Player(1, "Service Player", "service@mail.escuelaing.edu.co", "123456", 7, Rol.FORWARD, TypeUser.STUDENT);
    }

    // --- Tests for registerPlayer(Player player) ---

    @Test
    void shouldRegisterPlayerSuccessfully() {
        userService.registerPlayer(player);

        assertEquals(1, userService.getUsers().size());
        assertTrue(userService.emailExists("service@mail.escuelaing.edu.co"));
    }

    @Test
    void shouldThrowExceptionWhenRegisteringNullPlayer() {
        assertThrows(TechCupException.class, () -> userService.registerPlayer(null));
    }

    // --- Tests for validateUniqueEmail(String email) ---

    @Test
    void shouldValidateUniqueEmailSuccessfully() {
        userService.validateUniqueEmail("new@mail.escuelaing.edu.co");
    }

    @Test
    void shouldThrowExceptionWhenEmailIsNotUniqueOrInvalid() {
        userService.registerPlayer(player);

        assertThrows(TechCupException.class, () -> userService.validateUniqueEmail(""));
        assertThrows(TechCupException.class, () -> userService.validateUniqueEmail("service@mail.escuelaing.edu.co"));
    }

    // --- Tests for validateEmailFormat(String email) ---

    @Test
    void shouldValidateEmailFormatSuccessfully() {
        userService.validateEmailFormat("valid.user@mail.escuelaing.edu.co");
    }

    @Test
    void shouldThrowExceptionWhenEmailFormatIsInvalid() {
        assertThrows(TechCupException.class, () -> userService.validateEmailFormat(null));
        assertThrows(TechCupException.class, () -> userService.validateEmailFormat(""));
        assertThrows(TechCupException.class, () -> userService.validateEmailFormat("invalid-format"));
    }

    // --- Tests for validateAllowedEmailByType(String email, TypeUser typeUser) ---

    @Test
    void shouldValidateAllowedEmailByTypeSuccessfully() {
        userService.validateAllowedEmailByType("student@mail.escuelaing.edu.co", TypeUser.STUDENT);
        userService.validateAllowedEmailByType("graduate@mail.escuelaing.edu.co", TypeUser.GRADUATE);
        userService.validateAllowedEmailByType("professor@mail.escuelaing.edu.co", TypeUser.PROFESSOR);
        userService.validateAllowedEmailByType("admin@mail.escuelaing.edu.co", TypeUser.ADMINISTRATIVE);
        userService.validateAllowedEmailByType("family@gmail.com", TypeUser.FAMILY_MEMBER);
    }

    @Test
    void shouldThrowExceptionWhenEmailDomainIsNotAllowedByType() {
        assertThrows(TechCupException.class, () -> userService.validateAllowedEmailByType("student@gmail.com", TypeUser.STUDENT));
        assertThrows(TechCupException.class, () -> userService.validateAllowedEmailByType("family@mail.escuelaing.edu.co", TypeUser.FAMILY_MEMBER));
        assertThrows(TechCupException.class, () -> userService.validateAllowedEmailByType("", TypeUser.STUDENT));
        assertThrows(TechCupException.class, () -> userService.validateAllowedEmailByType("ok@mail.escuelaing.edu.co", null));
    }

    // --- Tests for findByEmail(String email) ---

    @Test
    void shouldFindUserByEmailSuccessfully() {
        userService.registerPlayer(player);

        assertNotNull(userService.findByEmail("service@mail.escuelaing.edu.co"));
        assertNotNull(userService.findByEmail("SERVICE@mail.escuelaing.edu.co"));
    }

    @Test
    void shouldReturnNullWhenFindByEmailReceivesInvalidInput() {
        assertNull(userService.findByEmail(null));
        assertNull(userService.findByEmail(""));
        assertNull(userService.findByEmail("unknown@mail.escuelaing.edu.co"));
    }

    // --- Tests for emailExists(String email) ---

    @Test
    void shouldReturnEmailExistsStatusSuccessfully() {
        assertEquals(false, userService.emailExists("service@mail.escuelaing.edu.co"));
        userService.registerPlayer(player);
        assertTrue(userService.emailExists("service@mail.escuelaing.edu.co"));
    }

    // --- Tests for getUsers() ---

    @Test
    void shouldReturnRegisteredUsersSuccessfully() {
        userService.registerPlayer(player);
        assertEquals(1, userService.getUsers().size());
    }
}
