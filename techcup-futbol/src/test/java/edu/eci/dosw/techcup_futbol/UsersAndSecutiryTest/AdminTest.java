package edu.eci.dosw.techcup_futbol.UsersAndSecutiryTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.eci.dosw.techcup_futbol.exceptions.TechCupException;
import edu.eci.dosw.techcup_futbol.model.Payments.Pay;
import edu.eci.dosw.techcup_futbol.model.Payments.StatePay;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Admin;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Player;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Rol;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.TypeUser;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.UserRole;

class AdminTest {

    private Admin admin;
    private Player targetUser;

    @BeforeEach
    void setUp() {
        // Arrange: Create a valid admin and a target user
        admin = new Admin(1, "Main Admin", "admin@mail.escuelaing.edu.co", "123456");
        targetUser = new Player(2, "Target User", "target@mail.escuelaing.edu.co", "123456", 10, Rol.DEFENDER, TypeUser.STUDENT);
    }

    // --- Tests for changeUserRole(User user, UserRole role) ---

    @Test
    void shouldChangeUserRoleSuccessfully() {
        assertDoesNotThrow(() -> admin.changeUserRole(targetUser, UserRole.ORGANIZER));
    }

    @Test
    void shouldThrowExceptionWhenUserOrRoleIsNull() {
        assertThrows(TechCupException.class, () -> admin.changeUserRole(null, UserRole.ADMIN));
        assertThrows(TechCupException.class, () -> admin.changeUserRole(targetUser, null));
    }

    @Test
    void shouldThrowExceptionWhenAdminTriesToRevokeOwnRole() {
        TechCupException exception = assertThrows(TechCupException.class, () -> admin.changeUserRole(admin, UserRole.PLAYER));
        assertEquals("Security Error: An admin cannot revoke their own admin status.", exception.getMessage());
    }

    // --- Tests for updateUserStatus(User user, boolean enabled) ---

    @Test
    void shouldUpdateUserStatusSuccessfully() {
        assertDoesNotThrow(() -> admin.updateUserStatus(targetUser, false));
        assertDoesNotThrow(() -> admin.updateUserStatus(targetUser, true));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingStatusWithNullUser() {
        assertThrows(TechCupException.class, () -> admin.updateUserStatus(null, true));
    }

    // --- Tests for overridePaymentStatus(Pay pay, StatePay status, String comment) ---

    @Test
    void shouldOverridePaymentStatusSuccessfully() {
        Pay pay = new Pay(1, "https://receipt.local/1");
        assertDoesNotThrow(() -> admin.overridePaymentStatus(pay, StatePay.APPROVED, "Manual override"));
    }

    @Test
    void shouldThrowExceptionWhenPaymentOrStatusIsNull() {
        Pay pay = new Pay(1, "https://receipt.local/1");
        assertThrows(TechCupException.class, () -> admin.overridePaymentStatus(null, StatePay.APPROVED, "x"));
        assertThrows(TechCupException.class, () -> admin.overridePaymentStatus(pay, null, "x"));
    }

    // --- Tests for resetPassword(User user, String newPassword) ---

    @Test
    void shouldResetPasswordSuccessfully() {
        admin.resetPassword(targetUser, "newPassword123");
        assertEquals("newPassword123", targetUser.getPassword());
    }

    @Test
    void shouldThrowExceptionWhenResettingPasswordWithInvalidData() {
        assertThrows(TechCupException.class, () -> admin.resetPassword(null, "newPassword123"));
        assertThrows(TechCupException.class, () -> admin.resetPassword(targetUser, "123"));
    }

    // --- Tests for query methods ---

    @Test
    void shouldReturnUsersList() {
        assertNotNull(admin.getUsers());
        assertTrue(admin.getUsers().isEmpty());
    }

    @Test
    void shouldReturnAuditRecordsList() {
        assertNotNull(admin.getAuditRecords());
        assertTrue(admin.getAuditRecords().isEmpty());
    }

    @Test
    void shouldReturnTournamentsList() {
        assertNotNull(admin.getTournaments());
        assertTrue(admin.getTournaments().isEmpty());
    }
}