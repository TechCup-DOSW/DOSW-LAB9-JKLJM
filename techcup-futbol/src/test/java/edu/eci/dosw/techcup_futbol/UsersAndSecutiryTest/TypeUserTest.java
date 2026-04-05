package edu.eci.dosw.techcup_futbol.UsersAndSecutiryTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.TypeUser;

class TypeUserTest {

    // --- Tests for enum TypeUser ---

    @Test
    void shouldContainExpectedTypeUserValues() {
        TypeUser[] values = TypeUser.values();
        assertEquals(5, values.length);
        assertNotNull(TypeUser.valueOf("STUDENT"));
        assertNotNull(TypeUser.valueOf("GRADUATE"));
        assertNotNull(TypeUser.valueOf("PROFESSOR"));
        assertNotNull(TypeUser.valueOf("ADMINISTRATIVE"));
        assertNotNull(TypeUser.valueOf("FAMILY_MEMBER"));
        assertTrue(Arrays.asList(values).contains(TypeUser.FAMILY_MEMBER));
    }
}