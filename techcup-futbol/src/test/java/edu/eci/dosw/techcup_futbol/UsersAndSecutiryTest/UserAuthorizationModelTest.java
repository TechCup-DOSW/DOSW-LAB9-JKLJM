package edu.eci.dosw.techcup_futbol.UsersAndSecutiryTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Permission;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.User;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.UserRole;

class UserAuthorizationModelTest {

    @Test
    void shouldAggregatePermissionsFromMultipleRoles() {
        User user = new User(7, "Multi Role", "multi@mail.com", "123456", UserRole.PLAYER);
        user.setRoles(Set.of(UserRole.PLAYER, UserRole.ORGANIZER));

        assertTrue(user.hasPermission(Permission.TOURNAMENTS_READ));
        assertTrue(user.hasPermission(Permission.TOURNAMENTS_WRITE));
        assertTrue(user.hasPermission(Permission.PAYMENTS_REVIEW));
    }

    @Test
    void shouldKeepBackwardCompatiblePrimaryRoleAfterSetRoles() {
        User user = new User(8, "Compat", "compat@mail.com", "123456", UserRole.PLAYER);
        user.setRoles(Set.of(UserRole.ADMIN, UserRole.PLAYER));

        assertTrue(user.getRoles().contains(UserRole.ADMIN));
        assertTrue(user.getRoles().contains(UserRole.PLAYER));
        assertEquals(2, user.getRoles().size());
    }
}
