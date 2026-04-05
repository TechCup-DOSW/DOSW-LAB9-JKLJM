package edu.eci.dosw.techcup_futbol.entity;

import jakarta.persistence.Column;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Entity;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tournaments")
public class TournamentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "max_teams", nullable = false)
    private int maxTeams;

    @Column(name = "players_per_team", nullable = false)
    private int playersPerTeam;

    @Column(name = "cost_per_team", nullable = false)
    private double costPerTeam;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @Column(name = "rules", nullable = false, columnDefinition = "TEXT")
    private String rules;

    @ElementCollection
    @CollectionTable(name = "tournament_available_schedule", joinColumns = @JoinColumn(name = "tournament_id"))
    @Column(name = "available_schedule", nullable = false)
    private List<String> availableSchedule = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "tournament_allowed_faculties", joinColumns = @JoinColumn(name = "tournament_id"))
    @Column(name = "faculty", nullable = false)
    private List<String> allowedFaculties = new ArrayList<>();

    @OneToMany(mappedBy = "tournament")
    private List<TeamEntity> teams = new ArrayList<>();

    public TournamentEntity() {
        // Required by JPA
    }

    //GETTERS AND SETTERS   
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getPlayersPerTeam() {
        return playersPerTeam;
    }

    public void setPlayersPerTeam(int playersPerTeam) {
        this.playersPerTeam = playersPerTeam;
    }

    public double getCostPerTeam() {
        return costPerTeam;
    }

    public void setCostPerTeam(double costPerTeam) {
        this.costPerTeam = costPerTeam;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRules() {
        return rules;
    }


    public void setRules(String rules) {
        this.rules = rules;
    }

    public List<String> getAvailableSchedule() {
        return availableSchedule;
    }

    public void setAvailableSchedule(List<String> availableSchedule) {
        this.availableSchedule = availableSchedule;
    }

    public List<String> getAllowedFaculties() {
        return allowedFaculties;
    }

    public void setAllowedFaculties(List<String> allowedFaculties) {
        this.allowedFaculties = allowedFaculties;
    }

    public List<TeamEntity> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamEntity> teams) {
        this.teams = teams;
    }
}
