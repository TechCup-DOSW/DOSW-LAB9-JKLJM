package edu.eci.dosw.techcup_futbol.dtos;


/**
 * DTO used to represent one row of the standings table.
 */
public class TableDTO {

    private Integer teamId;
    private String teamName;
    private int playedMatches;
    private int wonMatches;
    private int drawnMatches;
    private int lostMatches;
    private int goalsFor;
    private int goalsAgainst;
    private int goalDifference;
    private int points;

    public TableDTO() {
    }

    public TableDTO(Integer teamId, String teamName, int playedMatches, int wonMatches,
                    int drawnMatches, int lostMatches, int goalsFor, int goalsAgainst,
                    int goalDifference, int points) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.playedMatches = playedMatches;
        this.wonMatches = wonMatches;
        this.drawnMatches = drawnMatches;
        this.lostMatches = lostMatches;
        this.goalsFor = goalsFor;
        this.goalsAgainst = goalsAgainst;
        this.goalDifference = goalDifference;
        this.points = points;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getPlayedMatches() {
        return playedMatches;
    }

    public void setPlayedMatches(int playedMatches) {
        this.playedMatches = playedMatches;
    }

    public int getWonMatches() {
        return wonMatches;
    }

    public void setWonMatches(int wonMatches) {
        this.wonMatches = wonMatches;
    }

    public int getDrawnMatches() {
        return drawnMatches;
    }

    public void setDrawnMatches(int drawnMatches) {
        this.drawnMatches = drawnMatches;
    }

    public int getLostMatches() {
        return lostMatches;
    }

    public void setLostMatches(int lostMatches) {
        this.lostMatches = lostMatches;
    }

    public int getGoalsFor() {
        return goalsFor;
    }

    public void setGoalsFor(int goalsFor) {
        this.goalsFor = goalsFor;
    }

    public int getGoalsAgainst() {
        return goalsAgainst;
    }

    public void setGoalsAgainst(int goalsAgainst) {
        this.goalsAgainst = goalsAgainst;
    }

    public int getGoalDifference() {
        return goalDifference;
    }

    public void setGoalDifference(int goalDifference) {
        this.goalDifference = goalDifference;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}

