package edu.eci.dosw.techcup_futbol.UsersAndSecutiryTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Permission;

/**
 * Unit tests for {@link Permission}.
 */
class PermissionTest {

    // --- Tests for enum values ---

    @Test
    void shouldContainAllExpectedPermissions() {
        Permission[] permissions = Permission.values();

        assertEquals(6, permissions.length);
        assertEquals(Permission.USERS_READ, permissions[0]);
        assertEquals(Permission.USERS_WRITE, permissions[1]);
        assertEquals(Permission.TOURNAMENTS_READ, permissions[2]);
        assertEquals(Permission.TOURNAMENTS_WRITE, permissions[3]);
        assertEquals(Permission.PAYMENTS_REVIEW, permissions[4]);
        assertEquals(Permission.MATCHES_REFEREE, permissions[5]);
    }

    @Test
    void shouldResolvePermissionByNameSuccessfully() {
        assertEquals(Permission.USERS_READ, Permission.valueOf("USERS_READ"));
        assertEquals(Permission.USERS_WRITE, Permission.valueOf("USERS_WRITE"));
        assertEquals(Permission.TOURNAMENTS_READ, Permission.valueOf("TOURNAMENTS_READ"));
        assertEquals(Permission.TOURNAMENTS_WRITE, Permission.valueOf("TOURNAMENTS_WRITE"));
        assertEquals(Permission.PAYMENTS_REVIEW, Permission.valueOf("PAYMENTS_REVIEW"));
        assertEquals(Permission.MATCHES_REFEREE, Permission.valueOf("MATCHES_REFEREE"));
    }
}