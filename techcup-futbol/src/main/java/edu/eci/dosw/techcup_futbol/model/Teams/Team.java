package edu.eci.dosw.techcup_futbol.model.Teams;
import java.util.ArrayList;
import java.util.List;

import edu.eci.dosw.techcup_futbol.exceptions.TechCupException;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Player;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Rol;

/**
 * Represents a football team in the TechCup tournament.
 */
public class Team {
    private int id; //
    private String name; //
    private List<Player> players;
    private Player captain;
    


    public Team(int id, String name) {
        this.id = id;
        this.name = name;
        this.players = new ArrayList<>();
    }

    /**
     * Adds a player to the team roster.
     */
    public void addPlayer(Player player) {
        if (player == null) {
            throw new TechCupException("Error: Cannot add a null player.");
        }
        // Basic check to avoid duplicates
        if (players.contains(player)) {
            throw new TechCupException("Error: Player " + player.getName() + " is already in the team.");
        }
        this.players.add(player);
        player.setAvailable(false);  //sets that its not avaiable for other teams.
    }

    /**
     * Validates if the team has the required number of players.
     * In TechCup, typically a minimum of 7 and maximum of 15.
     */
    public boolean validateQuantity() {
        int count = players.size();
        // Logical rule: For a football match, you need at least a squad of 7.
        if (count < 7 || count > 15) {
            return false;
        }
        return true;
    }

    /**
     * Assigns the captaincy to a specific player.
     */
    public void assignCap(Player player) {
        if (!players.contains(player)) {
            throw new TechCupException("Error: Only a team member can be assigned as captain.");
        }
        this.captain = player;
    }

    // --- GETTERS ---

    public int getId() {
        return id; //
    }

    public String getName() {
        return name; //
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    public Player getCaptain() {
        return captain;
    }


    // CHECK ALIGNMENT CONNECTION, I CREATED IT BUT DONT KNOW HOW IT WORKS


    public void makePayment(int id, String voucher){

        captain.makePay(id, voucher);
    }
}