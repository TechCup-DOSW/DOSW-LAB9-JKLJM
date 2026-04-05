package edu.eci.dosw.techcup_futbol.UsersAndSecutiryTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.eci.dosw.techcup_futbol.exceptions.TechCupException;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Player;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Rol;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.TypeUser;

class PlayerTest {

    private Player player;

    @BeforeEach
    void setUp() {
        // Arrange: Create a valid default player
        player = new Player(1, "Sample Player", "player@mail.escuelaing.edu.co", "123456", 10, Rol.MIDFIELDER, TypeUser.STUDENT);
    }

    // --- Tests for constructor ---

    @Test
    void shouldCreatePlayerSuccessfully() {
        assertEquals(1, player.getId());
        assertEquals("Sample Player", player.getName());
        assertEquals("player@mail.escuelaing.edu.co", player.getEmail());
        assertEquals(10, player.getDorsal());
        assertEquals(Rol.MIDFIELDER, player.getRol());
        assertEquals(TypeUser.STUDENT, player.getTypeUser());
        assertTrue(player.isAvailable());
    }

    @Test
    void shouldThrowExceptionWhenCreatingPlayerWithInvalidData() {
        assertThrows(TechCupException.class, () -> new Player(2, "A", "a@mail.escuelaing.edu.co", "123456", 0, Rol.DEFENDER, TypeUser.STUDENT));
        assertThrows(TechCupException.class, () -> new Player(3, "B", "b@mail.escuelaing.edu.co", "123456", 7, null, TypeUser.STUDENT));
    }

    // --- Tests for updatePlayer(String name, int dorsal, boolean available, Rol newRol) ---

    @Test
    void shouldUpdatePlayerSuccessfully() {
        player.updatePlayer("Updated Player", 8, false, Rol.FORWARD);

        assertEquals("Updated Player", player.getName());
        assertEquals(8, player.getDorsal());
        assertEquals(Rol.FORWARD, player.getRol());
        assertFalse(player.isAvailable());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingPlayerWithInvalidData() {
        assertThrows(TechCupException.class, () -> player.updatePlayer("X", 0, true, Rol.DEFENDER));
        assertThrows(TechCupException.class, () -> player.updatePlayer("X", 11, true, null));
    }

    // --- Tests for changeRol(Rol newRol) ---

    @Test
    void shouldChangeRolSuccessfully() {
        player.changeRol(Rol.DEFENDER);
        assertEquals(Rol.DEFENDER, player.getRol());
    }

    @Test
    void shouldThrowExceptionWhenChangingRolToNull() {
        assertThrows(TechCupException.class, () -> player.changeRol(null));
    }

    // --- Tests for setRol(Rol rol) ---

    @Test
    void shouldSetRolSuccessfully() {
        player.setRol(Rol.GOALKEEPER);
        assertEquals(Rol.GOALKEEPER, player.getRol());
    }

    @Test
    void shouldThrowExceptionWhenSettingRolToNull() {
        assertThrows(TechCupException.class, () -> player.setRol(null));
    }

    // --- Tests for setDorsal(int dorsal) ---

    @Test
    void shouldSetDorsalSuccessfully() {
        player.setDorsal(5);
        assertEquals(5, player.getDorsal());
    }

    @Test
    void shouldThrowExceptionWhenSettingInvalidDorsal() {
        assertThrows(TechCupException.class, () -> player.setDorsal(0));
        assertThrows(TechCupException.class, () -> player.setDorsal(-3));
    }

    // --- Tests for simple getters/setters ---

    @Test
    void shouldHandleAvailabilityFlag() {
        player.setAvailable(false);
        assertFalse(player.isAvailable());

        player.setAvailable(true);
        assertTrue(player.isAvailable());
    }

    @Test
    void shouldReturnTypeUserSuccessfully() {
        assertEquals(TypeUser.STUDENT, player.getTypeUser());
    }
}
