package edu.eci.dosw.techcup_futbol.GamesTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import edu.eci.dosw.techcup_futbol.model.Games.TypeEvent;

class TypeEventTest {

    // --- Tests for enum values ---

    @Test
    void shouldContainGoalValue() {
        // Test: TypeEvent enum should have a GOAL constant
        assertEquals(TypeEvent.GOAL, TypeEvent.valueOf("GOAL"));
    }

    @Test
    void shouldContainYellowCardValue() {
        // Test: TypeEvent enum should have a YELLOW_CARD constant
        assertEquals(TypeEvent.YELLOW_CARD, TypeEvent.valueOf("YELLOW_CARD"));
    }

    @Test
    void shouldContainRedCardValue() {
        // Test: TypeEvent enum should have a RED_CARD constant
        assertEquals(TypeEvent.RED_CARD, TypeEvent.valueOf("RED_CARD"));
    }

    @Test
    void shouldHaveExactlyThreeValues() {
        // Test: TypeEvent.values() should return exactly 3 constants
        assertEquals(3, TypeEvent.values().length);
    }

    // --- Tests for valueOf() ---

    @Test
    void shouldResolveGoalByName() {
        // Test: TypeEvent.valueOf("GOAL") should return TypeEvent.GOAL
        assertEquals(TypeEvent.GOAL, TypeEvent.valueOf("GOAL"));
    }

    @Test
    void shouldResolveYellowCardByName() {
        // Test: TypeEvent.valueOf("YELLOW_CARD") should return TypeEvent.YELLOW_CARD
        assertEquals(TypeEvent.YELLOW_CARD, TypeEvent.valueOf("YELLOW_CARD"));
    }

    @Test
    void shouldResolveRedCardByName() {
        // Test: TypeEvent.valueOf("RED_CARD") should return TypeEvent.RED_CARD
        assertEquals(TypeEvent.RED_CARD, TypeEvent.valueOf("RED_CARD"));
    }

    @Test
    void shouldThrowExceptionForUnknownEventName() {
        // Test: TypeEvent.valueOf("FOUL") should throw IllegalArgumentException as it is not a valid constant
        assertThrows(IllegalArgumentException.class, () -> TypeEvent.valueOf("FOUL"));
    }
}
