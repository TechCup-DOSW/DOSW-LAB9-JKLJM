package edu.eci.dosw.techcup_futbol.model.UsersAndSecurity;

import edu.eci.dosw.techcup_futbol.exceptions.TechCupException; // for the exceptions
import edu.eci.dosw.techcup_futbol.model.Payments.Pay;
import edu.eci.dosw.techcup_futbol.model.Teams.Team;
/**
 * Player class representing a participant in the TechCup tournament.
 * Extends the abstract User class.
 */


public class Player extends User {

    // Player specific attributes
    private int dorsal;
    private boolean available;
    private Rol rol;
    private TypeUser typeUser;
    //new
    private Team currentTeam;
    
    /**
     * Player Constructor
     * @param name from User
     * @param email from User
     * @param password from User
     * @param dorsal from Player (must be positive)
     * @param rol Type Enum Rol (Goalkeeper, Defender, etc.)
     * @param typeUser Type Enum TypeUser (Student, Alumni, etc.)
     */
    public Player(int id, String name, String email, String password, int dorsal, Rol rol, TypeUser typeUser) {
        // Sending obligatory data to User Abstract class
        super(id, name, email, password, UserRole.PLAYER); 
        
        // Assigns the own attributes using setters to trigger validations and audit
        setDorsal(dorsal);
        setRol(rol);

        this.typeUser = typeUser;
        this.available = true; // Default value: Players start as available for recruitment
        this.currentTeam = null;
    }

    // --- MAIN METHODS ---

    /**
     * General update for player information
     */
    public void updatePlayer(String name, int dorsal, boolean available, Rol newRol) {
        this.setName(name);
        setDorsal(dorsal); // reuse validation and audit
        setRol(newRol);    // reuse validation and audit
        this.available = available;
    }

    /**
     * Specific action to change the player's position on the field
     */
    public void changeRol(Rol newRol) { 
        validateRol(newRol); // validate the new input
        
        if (this.rol != newRol) {
            this.rol = newRol;
            // With AUDIT CONTROL - Required by Security Req 7
            RecordAudit.createAudit("Changed rol to: " + newRol, this);
        }
    }


    //NEW SPRINT 2 CREATE TEAMS

    public void createTeam(int teamId, String teamName){
        if(!this.isAvailable()){
                throw new TechCupException("Has already a team named: "+ this.currentTeam +" ,cannot create a new one.");
            }
        Team newTeam = new Team(teamId, teamName);
        newTeam.addPlayer(this);
        newTeam.assignCap(this);
        this.changeRol(Rol.CAPTAIN);
        this.setAvailable(false);
        RecordAudit.createAudit(this.getName() + " Created a new team "+ teamName +" and became its captain.", this);
        this.currentTeam = newTeam;
    }


    //CORRECTION OF PAY WHEN TEAM MAKES PAYMENT

    public void makePay(int id, String voucher){
        if(this.rol != Rol.CAPTAIN){
            throw new TechCupException("You are not a captain, only captains make payments");

        }

        Pay payment = new Pay(id, voucher);
    }


    // --- GETTERS AND SETTERS ---

    public Rol getRol() { 
        return rol; 
    }

    public void setRol(Rol rol) {
        validateRol(rol);
        this.rol = rol;
        // Basic assignment audit
        RecordAudit.createAudit("Seted a rol: " + rol, this);
    }

    public void setDorsal(int dorsal) {
        // Business Rule: Dorsal cannot be negative or zero
        if (dorsal < 1) {
            throw new TechCupException("Invalid Dorsal: must be a positive number");
        }
        this.dorsal = dorsal;
        RecordAudit.createAudit("Seted its dorsal: " + dorsal, this);
    }

    public int getDorsal() { 
        return dorsal; 
    }

    public boolean isAvailable() { 
        return available; 
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public TypeUser getTypeUser() {
        return typeUser;
    }


    // NEW GETTER FOR SPRINT 2
    public Team getTeam(){
        return this.currentTeam;
    }

    // --- VALIDATIONS ---

    /**
     * Centralized validation for Rol
     * Checks for nullity as the Enum already restricts valid values
     */
    private void validateRol(Rol rol) {
        if (rol == null) {
            // "Paila", cannot be null
            throw new TechCupException("Error: Rol is obligatory, cannot create a player without position");
        }
    }


}