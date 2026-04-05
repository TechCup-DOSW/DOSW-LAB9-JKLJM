package edu.eci.dosw.techcup_futbol.UserServiceTest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Player;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Rol;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.TypeUser;

public class UserCreationTest {

    @Test
    @DisplayName("Should create a Player and verify domain integrity and audit")
    public void shouldCreatePlayerWithCorrectData() {
        // 1. Arrange: DEFINING PARAMETERS
        int id = 101;
        String name = "Juan Perez";
        String email = "juan.perez@mail.escuelaing.edu.co";
        String password = "securePassword123";
        int dorsal = 10;
        Rol initialRol = Rol.MIDFIELDER;
        TypeUser type = TypeUser.STUDENT;

        // 2. Act: CREATING PLAYER OBJECT
        // THIS WILL TRIGGER THE AUDIT :=)
        Player newPlayer = new Player(id, name, email, password, dorsal, initialRol, type);

        // 3. Assert: Verificaciones
        assertAll("Player basic attributes",
            () -> assertNotNull(newPlayer, "Player object should not be null"),
            () -> assertEquals(id, newPlayer.getId(), "ID mismatch"),
            () -> assertEquals(name, newPlayer.getName(), "Name mismatch"),
            () -> assertEquals(email, newPlayer.getEmail(), "Email mismatch"),
            () -> assertEquals(dorsal, newPlayer.getDorsal(), "Dorsal mismatch"),
            () -> assertEquals(initialRol, newPlayer.getRol(), "Role mismatch"),
            () -> assertTrue(newPlayer.isAvailable(), "New player should be available by default")
        );
    }
}