package edu.eci.dosw.techcup_futbol.model.UsersAndSecurity;

import java.util.List;

import edu.eci.dosw.techcup_futbol.exceptions.TechCupException;
import edu.eci.dosw.techcup_futbol.model.Competences.Tournament;
import edu.eci.dosw.techcup_futbol.model.Payments.Pay;
import edu.eci.dosw.techcup_futbol.model.Payments.StatePay;
/**
 * Represents a system administrator with global control privileges.
 */
public class Admin extends User {

    public Admin(Integer id, String name, String email, String password) {
        super(id, name, email, password, UserRole.ADMIN);
    }

    /**
     * Changes the role of a user.
     */
    public void changeUserRole(User user, UserRole role) {
        if (user == null || role == null) {
            throw new TechCupException("Error: User and Role are required for this operation.");
        }
        
        // Validation: Prevent an admin from revoking their own administrative privileges
        // (optional but recommended for system stability)
        if (user.getId() == this.getId() && role != UserRole.ADMIN) {
            throw new TechCupException("Security Error: An admin cannot revoke their own admin status.");
        }

        user.setRole(role);

        // Register audit --> max trazability
        RecordAudit.createAudit("ADMIN_ACTION: Changed role of user " + user.getEmail() + " to " + role, this);
        // implement logic to update user in DB -> IMPORTANT PLEASE CHECK THIS BROS
    }

    /**
     * Enables or disables a user account.
     */
    public void updateUserStatus(User user, boolean enabled) {
        if (user == null) {
            throw new TechCupException("Error: Target user cannot be null.");
        }

        String action = enabled ? "ENABLED" : "DISABLED";
        RecordAudit.createAudit("ADMIN_ACTION: User " + user.getEmail() + " set to " + action, this);
        // user.setEnabled(enabled); // implement
    }

    /**
     * Allows an administrator to override a payment status in exceptional cases.
     */
    public void overridePaymentStatus(Pay pay, StatePay status, String comment) {
        if (pay == null || status == null) {
            throw new TechCupException("Error: Payment record and new status are mandatory.");
        }

        // Los overrides son críticos, se debe auditar con detalle
        RecordAudit.createAudit("ADMIN_OVERRIDE: Payment ID " + pay.getId() + " forced to status " + status, this);
        // pay.setStatus(status); // implement
    }

    /**
     * Resets a user's password.
     */
    public void resetPassword(User user, String newPassword) {
        if (user == null) {
            throw new TechCupException("Error: User not found.");
        }
        
        // Validación de seguridad básica
        if (newPassword == null || newPassword.trim().length() < 6) {
            throw new TechCupException("Security Error: New password must be at least 6 characters long.");
        }

        RecordAudit.createAudit("ADMIN_ACTION: Password reset for user " + user.getEmail(), this);
        user.setPassword(newPassword); // implement
    }

    // --- QUERIES (Read-Only) ---

    public List<User> getUsers() {
        // SERVICE HANDLES ERROR IF DB DOESNT RESPOND --> PLEASE CHECK THIS
        return List.of(); 
    }

    public List<RecordAudit> getAuditRecords() {
        return List.of(); 
    }

    public List<Tournament> getTournaments() {
        return List.of(); 
    }
}