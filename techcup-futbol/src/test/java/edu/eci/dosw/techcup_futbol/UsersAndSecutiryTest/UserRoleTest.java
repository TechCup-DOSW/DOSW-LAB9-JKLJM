package edu.eci.dosw.techcup_futbol.UsersAndSecutiryTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EnumSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Permission;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.UserRole;

/**
 * Unit tests for {@link UserRole}.
 */
class UserRoleTest {

    // --- Tests for ADMIN role ---

    @Test
    void shouldReturnAdminPermissionsSuccessfully() {
        Set<Permission> expectedPermissions = EnumSet.of(
                Permission.USERS_READ,
                Permission.USERS_WRITE,
                Permission.TOURNAMENTS_READ,
                Permission.TOURNAMENTS_WRITE,
                Permission.PAYMENTS_REVIEW,
                Permission.MATCHES_REFEREE
        );

        assertEquals(expectedPermissions, UserRole.ADMIN.getPermissions());
        assertEquals(6, UserRole.ADMIN.getPermissions().size());
    }

    // --- Tests for ORGANIZER role ---

    @Test
    void shouldReturnOrganizerPermissionsSuccessfully() {
        Set<Permission> expectedPermissions = EnumSet.of(
                Permission.USERS_READ,
                Permission.TOURNAMENTS_READ,
                Permission.TOURNAMENTS_WRITE,
                Permission.PAYMENTS_REVIEW
        );

        assertEquals(expectedPermissions, UserRole.ORGANIZER.getPermissions());
        assertEquals(4, UserRole.ORGANIZER.getPermissions().size());
    }

    // --- Tests for PLAYER role ---

    @Test
    void shouldReturnPlayerPermissionsSuccessfully() {
        Set<Permission> expectedPermissions = EnumSet.of(
                Permission.TOURNAMENTS_READ
        );

        assertEquals(expectedPermissions, UserRole.PLAYER.getPermissions());
        assertEquals(1, UserRole.PLAYER.getPermissions().size());
    }

    @Test
    void shouldNotGrantAdministrativePermissionsToPlayer() {
        assertTrue(UserRole.PLAYER.getPermissions().contains(Permission.TOURNAMENTS_READ));
        assertFalse(UserRole.PLAYER.getPermissions().contains(Permission.USERS_WRITE));
        assertFalse(UserRole.PLAYER.getPermissions().contains(Permission.PAYMENTS_REVIEW));
        assertFalse(UserRole.PLAYER.getPermissions().contains(Permission.MATCHES_REFEREE));
    }

    // --- Tests for REFEREE role ---

    @Test
    void shouldReturnRefereePermissionsSuccessfully() {
        Set<Permission> expectedPermissions = EnumSet.of(
                Permission.TOURNAMENTS_READ,
                Permission.MATCHES_REFEREE
        );

        assertEquals(expectedPermissions, UserRole.REFEREE.getPermissions());
        assertEquals(2, UserRole.REFEREE.getPermissions().size());
    }

    // --- Tests for enum resolution ---

    @Test
    void shouldResolveRolesByNameSuccessfully() {
        assertEquals(UserRole.ADMIN, UserRole.valueOf("ADMIN"));
        assertEquals(UserRole.ORGANIZER, UserRole.valueOf("ORGANIZER"));
        assertEquals(UserRole.PLAYER, UserRole.valueOf("PLAYER"));
        assertEquals(UserRole.REFEREE, UserRole.valueOf("REFEREE"));
    }
}
