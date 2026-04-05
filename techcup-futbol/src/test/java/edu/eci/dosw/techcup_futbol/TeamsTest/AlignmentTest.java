package edu.eci.dosw.techcup_futbol.TeamsTest;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.ArrayList;
import edu.eci.dosw.techcup_futbol.exceptions.TechCupException;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.*;
import edu.eci.dosw.techcup_futbol.model.Teams.*;
class AlignmentTest {

    private Team team;
    private Alignment alignment;
    private Player p1, p2, p3;

    @BeforeEach
    void setUp() {
        team = new Team(1, "Dream Team");
        p1 = new Player(1, "P1", "p1@t.com", "123", 1, Rol.GOALKEEPER, TypeUser.STUDENT);
        p2 = new Player(2, "P2", "p2@t.com", "123", 2, Rol.CAPTAIN, TypeUser.STUDENT);
        p3 = new Player(3, "P3", "p3@t.com", "123", 3, Rol.DEFENDER, TypeUser.FAMILY_MEMBER);
        
        team.addPlayer(p1);
        team.addPlayer(p2);
        // Note: p3 is NOT added to the team roster
        
        alignment = new Alignment(1, "4-4-2", team);
    }

    @Test
    void shouldDefineStartersSuccessfully() {
        alignment.defineStarters(Arrays.asList(p1, p2));
        assertEquals(2, alignment.getStarters().size());
    }

    @Test
    void shouldThrowExceptionWhenStartersListIsEmpty() {
        assertThrows(TechCupException.class, () -> alignment.defineStarters(new ArrayList<>()));
    }

    @Test
    void shouldThrowExceptionWhenStarterIsNotInTeam() {
        // p3 belongs to the object world but not to 'team' roster
        assertThrows(TechCupException.class, () -> alignment.defineStarters(Arrays.asList(p3)));
    }

    @Test
    void shouldDefineSubstitutesSuccessfully() {
        alignment.defineStarters(Arrays.asList(p1));
        alignment.defineSubstitute(Arrays.asList(p2));
        
        assertTrue(alignment.getStarters().contains(p1));
        // Note: substitutes doesn't have a getter in your code, but you could add it
    }

    @Test
    void shouldThrowExceptionWhenPlayerIsBothStarterAndSubstitute() {
        alignment.defineStarters(Arrays.asList(p1));
        
        // p1 is already a starter, trying to make him a sub should fail
        assertThrows(TechCupException.class, () -> alignment.defineSubstitute(Arrays.asList(p1)));
    }

    @Test
    void shouldThrowExceptionWhenSubstituteIsNotInTeam() {
        assertThrows(TechCupException.class, () -> alignment.defineSubstitute(Arrays.asList(p3)));
    }
}