package edu.eci.dosw.techcup_futbol.model.UsersAndSecurity;

import java.util.List;

import edu.eci.dosw.techcup_futbol.exceptions.TechCupException;
import edu.eci.dosw.techcup_futbol.model.Games.Match;
import edu.eci.dosw.techcup_futbol.model.Teams.Team;

/**
 * Represents a referee.
 * Their primary role is to consult assigned game details.
 */
public class Referee extends User {

    private Match assignedGame;

    public Referee(Integer id, String name, String email, String password, Match assignedGame) {
        super(id, name, email, password, UserRole.REFEREE);
        this.assignedGame = assignedGame;
    }

    /**
     * Consults the official matchup.
     * Uses the internal logic of the Game class.
     */
    public String getMatchDetails() {
        validateAssignedGame();
        // Returns "Team A vs Team B" + Place + Date
        return String.format("Match: %s | Location: %s | Schedule: %s", 
                assignedGame.getMatchUp(), 
                assignedGame.getPlace(), 
                assignedGame.getDateTime());
    }

    /**
     * Checks if the game is already officially closed by the organizer.
     */
    public boolean isGameFinalized() {
        validateAssignedGame();
        return "FINISHED".equals(assignedGame.getStatus());
    }

    /**
     * Consults the teams to verify player rosters before the match starts.
     */
    public List<Team> getTeamsToVerify() {
        validateAssignedGame();
        return assignedGame.getTeams();
    }

    // --- ASSIGNMENT LOGIC ---

    public void setAssignedGame(Match newGame) {
        if (newGame == null) {
            throw new TechCupException("Error: Cannot assign a null game.");
        }
        this.assignedGame = newGame;
        RecordAudit.createAudit("REFEREE_ASSIGNMENT: Assigned to match " + newGame.getId(), this);
    }

    // --- PRIVATE UTILS ---

    private void validateAssignedGame() {
        if (this.assignedGame == null) {
            throw new TechCupException("Error: You do not have an assigned game at this moment.");
        }
    }

    public Match getAssignedGame() {
        return assignedGame;
    }
}