package edu.eci.dosw.techcup_futbol.model.Teams;

import java.util.ArrayList;
import java.util.List;

import edu.eci.dosw.techcup_futbol.exceptions.TechCupException;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Player;

public class Alignment {
    private int id;
    private String alignment;
    private Team team;
    private List<Player> starters;
    private List<Player> substitutes;

    public Alignment(int id, String alignment, Team team) {
        this.id = id;
        this.alignment = alignment;
        this.team = team;
        this.starters = new ArrayList<>();
        this.substitutes = new ArrayList<>();
    }

    /**
     * + defineStarters(starters : List<Player>) : void
     */
    public void defineStarters(List<Player> starters) {
        if (starters == null || starters.isEmpty()) {
            throw new TechCupException("Error: Starters list cannot be empty.");
        }
        
        for (Player p : starters) {
            validatePlayerInTeam(p);
        }
        
        this.starters = new ArrayList<>(starters);
    }

    /**
     * + defineSubstitute(substitutes : List<Player>) : void
     */
    public void defineSubstitute(List<Player> substitutes) {
        if (substitutes == null) {
            throw new TechCupException("Error: Substitutes list cannot be null.");
        }
        
        for (Player p : substitutes) {
            validatePlayerInTeam(p);
            if (this.starters.contains(p)) {
                throw new TechCupException("Error: Player " + p.getName() + " cannot be starter and substitute.");
            }
        }
        
        this.substitutes = new ArrayList<>(substitutes);
    }

    private void validatePlayerInTeam(Player player) {
        if (!team.getPlayers().contains(player)) {
            throw new TechCupException("Security Error: Player " + player.getName() + " is not in team " + team.getName());
        }
    }

    // Getters for queries
    public List<Player> getStarters() { return starters; }
    public String getAlignmentName() { return alignment; }
}