package edu.eci.dosw.techcup_futbol.model.Games;

import edu.eci.dosw.techcup_futbol.exceptions.TechCupException;

public class Statistics {
    private int localScoreboard;
    private int awayScoreboard;

    public Statistics() {
        this.localScoreboard = 0;
        this.awayScoreboard = 0;
    }

    public void updateScore(int localScore, int awayScore) {
        if (localScore < 0 || awayScore < 0) {
            throw new TechCupException("Error: Scores cannot be negative.");
        }
        this.localScoreboard = localScore;
        this.awayScoreboard = awayScore;
    }

    // Getters
    public int getLocalScoreboard() { return localScoreboard; }
    public int getAwayScoreboard() { return awayScoreboard; }
    
    @Override
    public String toString() {
        return localScoreboard + " - " + awayScoreboard;
    }
}