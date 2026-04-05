package edu.eci.dosw.techcup_futbol.model.Competences;
import edu.eci.dosw.techcup_futbol.dtos.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import edu.eci.dosw.techcup_futbol.dtos.TableDTO;
import edu.eci.dosw.techcup_futbol.exceptions.TechCupException;
import edu.eci.dosw.techcup_futbol.model.Games.Match;
import edu.eci.dosw.techcup_futbol.model.Teams.Team;

/**
 * Represents a TechCup tournament with its configuration, registration, 
 * and competition logic (Standings and Brackets).
 */
public class Tournament {
    private Long id;

    // --- UI CONFIGURATION ATTRIBUTES ---
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private int maxTeams;
    private int playersPerTeam;
    private double costPerTeam;
    private List<String> availableSchedules;
    private List<String> allowedFaculties;
    private List<Team> registeredTeams;
    private String status; // e.g., "DRAFT", "OPEN", "STARTED", "FINISHED"
    private String rules;

    // --- COMPETENCES DIAGRAM ATTRIBUTES ---
    private TablePositions tablePositions; // Relation 1:1
    private List<EliminationKey> eliminationKeys; // Relation 1:0..*

    /**
     * Tournament Constructor
     * Validates dates before completing object instantiation.
     */
    public Tournament(String name, LocalDate startDate, LocalDate endDate, int maxTeams, int playersPerTeam, double costPerTeam) {
        this.name = name;
        this.maxTeams = maxTeams;
        this.playersPerTeam = playersPerTeam;
        //had before on constructor that option as int

        //this.rules = rules;
        this.costPerTeam = costPerTeam;
        
        // Business Rule: Validate and assign dates using the fail-fast approach
        setTournamentDates(startDate, endDate);
        initializeRuntimeState();
        
        this.status = "DRAFT";
    }

    /**
     * Constructor used to rebuild domain state from persistence without
     * applying creation date validations.
     */
    public Tournament(Long id, String name, LocalDate startDate, LocalDate endDate, int maxTeams,
                      int playersPerTeam, double costPerTeam, String status, String rules) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxTeams = maxTeams;
        this.playersPerTeam = playersPerTeam;
        this.costPerTeam = costPerTeam;
        this.rules = rules;
        this.status = status == null ? "DRAFT" : status;
        initializeRuntimeState();
    }

    private void initializeRuntimeState() {
        this.availableSchedules = new ArrayList<>();
        this.allowedFaculties = new ArrayList<>();
        this.registeredTeams = new ArrayList<>();
        this.eliminationKeys = new ArrayList<>();
        this.tablePositions = new TablePositions();
    }

    /**
     * Assigns and validates tournament dates.
     * Can be used for initial creation or rescheduling.
     * @param start Initial date of the tournament
     * @param end Final date of the tournament
     * @throws TechCupException if dates are null, in the past, or logically inconsistent.
     */
    public void setTournamentDates(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new TechCupException("Error: Start and end dates are mandatory.");
        }

        // Rule: Start date cannot be in the past
        if (start.isBefore(LocalDate.now())) {
            throw new TechCupException("Error: Start date cannot be in the past.");
        }

        // Rule: End date must be strictly after the start date
        if (end.isBefore(start) || end.isEqual(start)) {
            throw new TechCupException("Error: End date must be strictly after the start date.");
        }

        this.startDate = start;
        this.endDate = end;
    }

    // --- COMPETENCES METHODS (FROM UML DIAGRAM) ---

    /**
     * Delegates standing calculations to the TablePositions object.
     * @param tournamentGames current list of games to calculate statistics
     * @return List of DTOs for the UI table
     */
    public List<TableDTO> getUpdatedTable(List<Match> tournamentGames) {
        return this.tablePositions.calculateTable(tournamentGames);
    }

    /**
     * Adds a new elimination round as specified in the diagram relationships.
     * @param roundName (e.g., "Quarterfinals", "Final")
     */
    public void addEliminationRound(String roundName) {
        EliminationKey newKey = new EliminationKey(roundName);
        this.eliminationKeys.add(newKey);
    }

    // --- CONFIGURATION METHODS ---

    public void addSchedule(String timeSlot) {
        this.availableSchedules.add(timeSlot);
    }

    public void allowFaculty(String facultyName) {
        this.allowedFaculties.add(facultyName);
    }

    /**
     * Registers a team in the tournament if it meets the requirements.
     * Validates capacity and roster size.
     */
    public void registerTeam(Team team) {
        if (registeredTeams.size() >= maxTeams) {
            throw new TechCupException("Error: Tournament is full.");
        }
        
        if (team.getPlayers().size() > playersPerTeam) {
            throw new TechCupException("Error: Team exceeds the maximum allowed players (" + playersPerTeam + ").");
        }

        this.registeredTeams.add(team);
    }

    // --- GETTERS & SETTERS ---

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public List<Team> getTeams() {
        return new ArrayList<>(registeredTeams);
    }

    public double getCostPerTeam() { return costPerTeam; }

    public int getMaxTeams() { return maxTeams; }

    public int getPlayersPerTeam() { return playersPerTeam; }

    public String getRules() { return rules; }

    public void setRules(String rules) { this.rules = rules; }

    public List<String> getAllowedFaculties() { return allowedFaculties; }
    
    public List<EliminationKey> getEliminationKeys() {
        return eliminationKeys;
    }

    public LocalDate getStartDate() { return startDate; }

    public LocalDate getEndDate() { return endDate; }

    public void setName(String newName){
        this.name = newName;
    }

    public void setMaxTeams(int newMaxTeams){
        this.maxTeams = newMaxTeams;
    }

    public void setPlayersPerTeam(int newPlayersPerTeam){
        this.playersPerTeam = newPlayersPerTeam;
    }

    public void setCostPerTeam(double newCostPerTeam){
        this.costPerTeam = newCostPerTeam;
    }
}