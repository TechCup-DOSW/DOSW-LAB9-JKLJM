package edu.eci.dosw.techcup_futbol.model.Games;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import edu.eci.dosw.techcup_futbol.exceptions.TechCupException;
import edu.eci.dosw.techcup_futbol.model.Teams.Team;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Player;

public class Match {
    private int id;
    private LocalDateTime date;
    private String place;
    private List<Team> teams; 
    private List<Action> actions; 
    private String status; // SCHEDULED, FINISHED
    
    // New delegation of the managament of statistics per game
    private Statistics statistics; 

    /**
     * Constructor for the Organizer to create a game.
     */
    public Match(int id, String place, LocalDateTime date, List<Team> teams) {
        validateTeams(teams); 
        
        this.id = id;
        this.place = place;
        this.date = date;
        this.teams = new ArrayList<>(teams);
        this.actions = new ArrayList<>();
        
        // Statistics start on 0-0 by default dudes
        this.statistics = new Statistics();
        this.status = "SCHEDULED"; 
    }

    // --- DOMAIN LOGIC METHODS ---

    /**
     * Records the goals. Updates the statistics object.
     */
    public void recordResult(int scoreLocal, int scoreAway) {
        // Validación de estado del partido
        if ("FINISHED".equals(this.status)) {
            throw new TechCupException("Error: Cannot modify results of a finalized game.");
        }

        // Validación de tiempo
        if (LocalDateTime.now().isBefore(this.date)) {
            throw new TechCupException("Error: Cannot register results for a game that hasn't happened yet.");
        }
        
        // Deleegates validations and updates to new methodd Statistics
        this.statistics.updateScore(scoreLocal, scoreAway);
    }

    /**
     * Adds an action to the game (e.g., a goal or a card)
     */
    public void addAction(int minute, TypeEvent type, Player player) {
        
        if ("FINISHED".equals(this.status)) {
            throw new TechCupException("Error: Cannot add actions to a finished game.");
        }
        Action action = new Action(minute, type, player);
        this.actions.add(action);
    }

    /**
     * Finalizes the game.
     */
    public boolean endGame() {
        if ("FINISHED".equals(this.status)) {
            throw new TechCupException("Error: The game is already finalized.");
        }

        this.status = "FINISHED";
        return true; 
    }

    // --- VALIDATION ---

    private void validateTeams(List<Team> teams) {
        if (teams == null || teams.size() != 2) {
            throw new TechCupException("Error: A game must have exactly 2 teams.");
        }
        if (teams.get(0).getId() == teams.get(1).getId()) {
            throw new TechCupException("Error: A team cannot play against itself.");
        }
    }

    // --- GETTERS ---

    public int getId() { return id; }
    
    public String getStatus() { return status; }

    public List<Team> getTeams() { return teams; }

    public List<Action> getActions() { return actions; }

    public LocalDateTime getDateTime() { return date; }

    public String getPlace() { return place; }

    /**
     * Permits access to the statistics
     */
    public Statistics getStatistics() {
        return statistics;
    }

    public String getMatchUp() {
        return teams.get(0).getName() + " " + statistics.toString() + " " + teams.get(1).getName();
    }
}