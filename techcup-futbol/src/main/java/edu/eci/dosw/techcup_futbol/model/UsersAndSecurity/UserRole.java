package edu.eci.dosw.techcup_futbol.model.UsersAndSecurity;

import java.util.EnumSet;
import java.util.Set;

/**
 * Represents account-level roles in the platform.
 */
public enum UserRole {
    ADMIN(EnumSet.of(
            Permission.USERS_READ,
            Permission.USERS_WRITE,
            Permission.TOURNAMENTS_READ,
            Permission.TOURNAMENTS_WRITE,
            Permission.PAYMENTS_REVIEW,
            Permission.MATCHES_REFEREE
    )),
    ORGANIZER(EnumSet.of(
            Permission.USERS_READ,
            Permission.TOURNAMENTS_READ,
            Permission.TOURNAMENTS_WRITE,
            Permission.PAYMENTS_REVIEW
    )),
    PLAYER(EnumSet.of(
            Permission.TOURNAMENTS_READ
    )),
    REFEREE(EnumSet.of(
            Permission.TOURNAMENTS_READ,
            Permission.MATCHES_REFEREE
    ));

    private final Set<Permission> permissions;

    UserRole(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }
}
