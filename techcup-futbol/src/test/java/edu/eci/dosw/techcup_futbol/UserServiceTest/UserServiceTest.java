package edu.eci.dosw.techcup_futbol.UserServiceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.UserService;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Player;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.User;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Rol;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.TypeUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import edu.eci.dosw.techcup_futbol.exceptions.TechCupException;


class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    @Test
    void shouldRegisterStudentWithInstitutionalEmail() {
        Player player = new Player(
                1,
                "Santiago",
                "santiago@mail.escuelaing.edu.co",
                "123456",
                10,
                Rol.FORWARD,
                TypeUser.STUDENT
        );

        userService.registerPlayer(player);

        assertEquals(1, userService.getUsers().size());
        assertTrue(userService.emailExists("santiago@mail.escuelaing.edu.co"));
    }

    @Test
    void shouldRegisterGraduateWithInstitutionalEmail() {
        Player player = new Player(
                2,
                "Laura",
                "laura@mail.escuelaing.edu.co",
                "123456",
                8,
                Rol.MIDFIELDER,
                TypeUser.GRADUATE
        );

        userService.registerPlayer(player);

        assertEquals(1, userService.getUsers().size());
    }

    @Test
    void shouldRegisterFamilyMemberWithGmail() {
        Player player = new Player(
                3,
                "Carlos",
                "carlos@gmail.com",
                "123456",
                7,
                Rol.DEFENDER,
                TypeUser.FAMILY_MEMBER
        );

        userService.registerPlayer(player);

        assertEquals(1, userService.getUsers().size());
        assertTrue(userService.emailExists("carlos@gmail.com"));
    }

    @Test
    void shouldRejectStudentWithGmail() {
        Player player = new Player(
                4,
                "Mateo",
                "mateo@gmail.com",
                "123456",
                9,
                Rol.FORWARD,
                TypeUser.STUDENT
        );

        TechCupException exception = assertThrows(
                TechCupException.class,
                () -> userService.registerPlayer(player)
        );

        assertEquals(
                "Error: Students, graduates, professors, and administrative staff must use an institutional email.",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectFamilyMemberWithInstitutionalEmail() {
        Player player = new Player(
                5,
                "Ana",
                "ana@mail.escuelaing.edu.co",
                "123456",
                11,
                Rol.GOALKEEPER,
                TypeUser.FAMILY_MEMBER
        );

        TechCupException exception = assertThrows(
                TechCupException.class,
                () -> userService.registerPlayer(player)
        );

        assertEquals(
                "Error: Family members must use a Gmail account.",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectDuplicatedEmail() {
        Player player1 = new Player(
                6,
                "Juan",
                "juan@mail.escuelaing.edu.co",
                "123456",
                5,
                Rol.DEFENDER,
                TypeUser.STUDENT
        );

        Player player2 = new Player(
                7,
                "Pedro",
                "juan@mail.escuelaing.edu.co",
                "654321",
                6,
                Rol.MIDFIELDER,
                TypeUser.STUDENT
        );

        userService.registerPlayer(player1);

        TechCupException exception = assertThrows(
                TechCupException.class,
                () -> userService.registerPlayer(player2)
        );

        assertEquals("Error: Email is already registered.", exception.getMessage());
    }

    @Test
    void shouldRejectDuplicatedEmailIgnoringCase() {
        Player player1 = new Player(
                8,
                "Sofia",
                "Sofia@mail.escuelaing.edu.co",
                "123456",
                4,
                Rol.DEFENDER,
                TypeUser.STUDENT
        );

        Player player2 = new Player(
                9,
                "Valeria",
                "sofia@mail.escuelaing.edu.co",
                "654321",
                3,
                Rol.FORWARD,
                TypeUser.STUDENT
        );

        userService.registerPlayer(player1);

        assertThrows(
                TechCupException.class,
                () -> userService.registerPlayer(player2)
        );
    }

    @Test
    void shouldRejectInvalidEmailFormat() {
        Player player = new Player(
                10,
                "Nicolas",
                "nicolas-mail.escuelaing.edu.co",
                "123456",
                2,
                Rol.DEFENDER,
                TypeUser.STUDENT
        );

        TechCupException exception = assertThrows(
                TechCupException.class,
                () -> userService.registerPlayer(player)
        );

        assertEquals("Error: Invalid email format.", exception.getMessage());
    }

    @Test
    void shouldReturnFalseWhenEmailDoesNotExist() {
        assertFalse(userService.emailExists("unknown@mail.escuelaing.edu.co"));
    }

    @Test
    void shouldFindUserByEmail() {
        Player player = new Player(
                11,
                "Camilo",
                "camilo@mail.escuelaing.edu.co",
                "123456",
                8,
                Rol.MIDFIELDER,
                TypeUser.STUDENT
        );

        userService.registerPlayer(player);

        User foundUser = userService.findByEmail("camilo@mail.escuelaing.edu.co");

        assertNotNull(foundUser);
        assertEquals("Camilo", foundUser.getName());
        assertEquals("camilo@mail.escuelaing.edu.co", foundUser.getEmail());
    }

    @Test
    void shouldFindUserByEmailIgnoringCase() {
        Player player = new Player(
                12,
                "Daniela",
                "daniela@mail.escuelaing.edu.co",
                "123456",
                6,
                Rol.DEFENDER,
                TypeUser.STUDENT
        );

        userService.registerPlayer(player);

        User foundUser = userService.findByEmail("DANIELA@mail.escuelaing.edu.co");

        assertNotNull(foundUser);
        assertEquals("Daniela", foundUser.getName());
    }

    @Test
    void shouldReturnNullWhenFindByEmailDoesNotExist() {
        User foundUser = userService.findByEmail("ghost@mail.escuelaing.edu.co");

        assertNull(foundUser);
    }
}
