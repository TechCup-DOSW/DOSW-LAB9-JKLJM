package edu.eci.dosw.techcup_futbol.UsersAndSecutiryTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.RecordAudit;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.User;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.UserRole;

class RecordAuditTest {

    private User actor;

    @BeforeEach
    void setUp() {
        actor = new User(1, "Audit Actor", "actor@mail.escuelaing.edu.co", "123456", UserRole.ADMIN);
    }

    // --- Tests for createAudit(String action, User user) ---

    @Test
    void shouldCreateAuditSuccessfully() {
        RecordAudit audit = RecordAudit.createAudit("TEST_ACTION", actor);
        assertNotNull(audit);
    }

    @Test
    void shouldCreateAuditWithExpectedData() throws Exception {
        RecordAudit audit = RecordAudit.createAudit("EXPECTED_ACTION", actor);

        Field actionField = RecordAudit.class.getDeclaredField("action");
        Field dateField = RecordAudit.class.getDeclaredField("date");
        actionField.setAccessible(true);
        dateField.setAccessible(true);

        String action = (String) actionField.get(audit);
        LocalDateTime date = (LocalDateTime) dateField.get(audit);

        assertEquals("EXPECTED_ACTION", action);
        assertNotNull(date);
    }
}
