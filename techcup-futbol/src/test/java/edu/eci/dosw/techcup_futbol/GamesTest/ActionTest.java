package edu.eci.dosw.techcup_futbol.GamesTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.eci.dosw.techcup_futbol.exceptions.TechCupException;
import edu.eci.dosw.techcup_futbol.model.Games.Action;
import edu.eci.dosw.techcup_futbol.model.Games.TypeEvent;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Player;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Rol;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.TypeUser;

class ActionTest {

    private Player player;

    @BeforeEach
    void setUp() {
        // Arrange: Create a valid player to use in action-related tests
        player = new Player(1, "Test Player", "player@mail.escuelaing.edu.co", "123456", 9, Rol.FORWARD, TypeUser.STUDENT);
    }

    // --- Tests for constructor Action(int minute, TypeEvent type, Player player) ---

    @Test
    void shouldCreateActionSuccessfully() {
        // Test: Create an Action with valid minute, type and player; should not throw any exception
        Action action = new Action(45, TypeEvent.GOAL, player);

        assertNotNull(action);
        assertEquals(45, action.getMinute());
        assertEquals(TypeEvent.GOAL, action.getType());
        assertSame(player, action.getPlayer());
    }

    @Test
    void shouldThrowExceptionWhenPlayerIsNull() {
        // Test: Creating an Action with null player should throw TechCupException
        assertThrows(TechCupException.class, () -> new Action(10, TypeEvent.GOAL, null));
    }

    @Test
    void shouldThrowExceptionWhenTypeEventIsNull() {
        // Test: Creating an Action with null TypeEvent should throw TechCupException
        assertThrows(TechCupException.class, () -> new Action(10, null, player));
    }

    @Test
    void shouldThrowExceptionWhenMinuteIsNegative() {
        // Test: Creating an Action with a negative minute should throw TechCupException
        assertThrows(TechCupException.class, () -> new Action(-1, TypeEvent.GOAL, player));
    }

    @Test
    void shouldThrowExceptionWhenMinuteExceedsLimit() {
        // Test: Creating an Action with minute > 130 should throw TechCupException
        assertThrows(TechCupException.class, () -> new Action(131, TypeEvent.GOAL, player));
    }

    @Test
    void shouldAcceptMinuteAtLowerBoundary() {
        // Test: Creating an Action with minute = 0 should be valid (boundary case)
        Action action = new Action(0, TypeEvent.YELLOW_CARD, player);

        assertEquals(0, action.getMinute());
    }

    @Test
    void shouldAcceptMinuteAtUpperBoundary() {
        // Test: Creating an Action with minute = 130 should be valid (boundary case)
        Action action = new Action(130, TypeEvent.RED_CARD, player);

        assertEquals(130, action.getMinute());
    }

    // --- Tests for createAction(int minute, TypeEvent type, Player player) ---

    @Test
    void shouldCreateActionViaFactoryMethodSuccessfully() {
        // Test: Factory method createAction should return a valid Action instance with correct data
        Action action = Action.createAction(25, TypeEvent.GOAL, player);

        assertNotNull(action);
        assertEquals(25, action.getMinute());
        assertEquals(TypeEvent.GOAL, action.getType());
        assertSame(player, action.getPlayer());
    }

    @Test
    void shouldThrowExceptionInFactoryMethodWhenPlayerIsNull() {
        // Test: Factory method createAction with null player should throw TechCupException
        assertThrows(TechCupException.class, () -> Action.createAction(10, TypeEvent.GOAL, null));
    }

    // --- Tests for getters ---

    @Test
    void shouldReturnCorrectMinute() {
        // Test: getMinute() should return the minute provided in the constructor
        Action action = new Action(88, TypeEvent.YELLOW_CARD, player);

        assertEquals(88, action.getMinute());
    }

    @Test
    void shouldReturnCorrectTypeEvent() {
        // Test: getType() should return the TypeEvent provided in the constructor
        Action action = new Action(40, TypeEvent.RED_CARD, player);

        assertEquals(TypeEvent.RED_CARD, action.getType());
    }

    @Test
    void shouldReturnCorrectPlayer() {
        // Test: getPlayer() should return the Player provided in the constructor
        Action action = new Action(40, TypeEvent.GOAL, player);

        assertSame(player, action.getPlayer());
    }
}
