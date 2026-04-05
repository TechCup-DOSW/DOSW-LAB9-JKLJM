package edu.eci.dosw.techcup_futbol.model.Games;

import edu.eci.dosw.techcup_futbol.exceptions.TechCupException;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Player;

/**
 * Represents a specific event during a game (Goal, Card, etc.)
 */
public class Action {
    private int minute;
    private TypeEvent type;
    private Player player;

    public Action(int minute, TypeEvent type, Player player) {
            // New Validations
            if (player == null) {
                throw new TechCupException("Error: Action must have a player associated.");
            }
            if (type == null) {
                throw new TechCupException("Error: Event type cannot be null.");
            }
            if (minute < 0 || minute > 130) { //Valid mrgin of extra time
                throw new TechCupException("Error: Minute must be a valid time.");
            }

            this.minute = minute;
            this.type = type;
            this.player = player;
    }

    /**
     * Factory method as per UML diagram
     */
    public static Action createAction(int minute, TypeEvent type, Player player) {
        return new Action(minute, type, player);
    }

    // Getters
    public int getMinute() { return minute; }
    public TypeEvent getType() { return type; }
    public Player getPlayer() { return player; }
}