package edu.eci.dosw.techcup_futbol.UsersAndSecutiryTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Permission;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.User;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.UserRole;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1, "Base User", "BASE@MAIL.COM", "123456", UserRole.PLAYER);
    }

    // --- Tests for constructor ---

    @Test
    void shouldCreateUserSuccessfully() {
        assertEquals(1, user.getId());
        assertEquals("Base User", user.getName());
        assertEquals("base@mail.com", user.getEmail());
        assertEquals("123456", user.getPassword());
    }

    // --- Tests for getName()/setName(String name) ---

    @Test
    void shouldSetAndGetNameSuccessfully() {
        user.setName("Updated Name");
        assertEquals("Updated Name", user.getName());
    }

    // --- Tests for getEmail()/setEmail(String email) ---

    @Test
    void shouldNormalizeEmailWhenSettingAndGettingEmail() {
        user.setEmail("  NEW.USER@MAIL.COM  ");
        assertEquals("new.user@mail.com", user.getEmail());
    }

    // --- Tests for getPassword()/setPassword(String password) ---

    @Test
    void shouldSetAndGetPasswordSuccessfully() {
        user.setPassword("newSecurePass");
        assertEquals("newSecurePass", user.getPassword());
    }

    // --- Tests for getId() ---

    @Test
    void shouldReturnIdSuccessfully() {
        assertEquals(1, user.getId());
    }

    @Test
    void shouldResolveRoleAndPermissionChecksFromCentralizedUser() {
        assertTrue(user.hasRole(UserRole.PLAYER));
        assertTrue(user.hasPermission(Permission.TOURNAMENTS_READ));
        assertFalse(user.hasPermission(Permission.USERS_WRITE));
    }

    @Test
    void shouldAddAndRemoveRolesThroughUnifiedApi() {
        user.addRole(UserRole.ORGANIZER);

        assertTrue(user.hasRole(UserRole.ORGANIZER));
        assertTrue(user.hasPermission(Permission.PAYMENTS_REVIEW));

        user.removeRole(UserRole.ORGANIZER);

        assertFalse(user.hasRole(UserRole.ORGANIZER));
        assertFalse(user.hasPermission(Permission.PAYMENTS_REVIEW));
    }
}
