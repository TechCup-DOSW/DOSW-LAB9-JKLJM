package edu.eci.dosw.techcup_futbol.dtos;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

/**
 * DTO used to create a tournament.
 */
public class TournamentCreateDTO {

    @NotBlank(message = "name is required")
    private String name;

    @NotNull(message = "startDate is required")
    @FutureOrPresent(message = "startDate must be today or in the future")
    private LocalDate startDate;

    @NotNull(message = "endDate is required")
    private LocalDate endDate;

    @Positive(message = "maxTeams must be greater than zero")
    private int maxTeams;

    @Positive(message = "costPerTeam must be greater than zero")
    private double costPerTeam;

    private String rules;
    private String status;

    @Positive(message = "playersPerTeam must be greater than zero")
    private int playersPerTeam;

    public TournamentCreateDTO() {
    }

    public TournamentCreateDTO(String name, LocalDate startDate, LocalDate endDate,
                               int maxTeams, double costPerTeam, int playersPerTeam, String status) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxTeams = maxTeams;
        this.costPerTeam = costPerTeam;
        this.playersPerTeam = playersPerTeam;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getMaxTeams() {
        return maxTeams;
    }

    public void setMaxTeams(int maxTeams) {
        this.maxTeams = maxTeams;
    }

    public double getCostPerTeam() {
        return costPerTeam;
    }

    public void setCostPerTeam(double costPerTeam) {
        this.costPerTeam = costPerTeam;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public int getPlayersPerTeam(){
        return this.playersPerTeam;
    }

    public void setPlayersPerTeam(int newPlayersPerTeam){
        this.playersPerTeam = newPlayersPerTeam;
    }

    @AssertTrue(message = "endDate must be after startDate")
    public boolean isDateRangeValid() {
        if (startDate == null || endDate == null) {
            return true;
        }
        return endDate.isAfter(startDate);
    }
}
