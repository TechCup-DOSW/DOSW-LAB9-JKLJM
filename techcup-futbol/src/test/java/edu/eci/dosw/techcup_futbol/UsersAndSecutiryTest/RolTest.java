package edu.eci.dosw.techcup_futbol.UsersAndSecutiryTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Rol;

class RolTest {

    // --- Tests for enum Rol ---

    @Test
    void shouldContainExpectedRolValues() {
        Rol[] values = Rol.values();
        assertEquals(5, values.length);
        assertNotNull(Rol.valueOf("GOALKEEPER"));
        assertNotNull(Rol.valueOf("DEFENDER"));
        assertNotNull(Rol.valueOf("MIDFIELDER"));
        assertNotNull(Rol.valueOf("FORWARD"));
        assertNotNull(Rol.valueOf("CAPTAIN"));
        assertTrue(Arrays.asList(values).contains(Rol.CAPTAIN));
    }
}
