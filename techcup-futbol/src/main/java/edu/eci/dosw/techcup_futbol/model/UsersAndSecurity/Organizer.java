package edu.eci.dosw.techcup_futbol.model.UsersAndSecurity;

import edu.eci.dosw.techcup_futbol.exceptions.TechCupException;
import edu.eci.dosw.techcup_futbol.model.Competences.Tournament;
import edu.eci.dosw.techcup_futbol.model.Games.Match;
import edu.eci.dosw.techcup_futbol.model.Payments.Pay;
import edu.eci.dosw.techcup_futbol.model.Payments.StatePay;
/**
 * Represents a tournament organizer.
 * Manages configuration, payments, and results.
 */
public class Organizer extends User {

    public Organizer(Integer id, String name, String email, String password) {
        super(id, name, email, password, UserRole.ORGANIZER);
    }

    // --- PAYMENT MANAGEMENT ---

    public void reviewPayment(Pay pay) {
        if (pay == null) {
            throw new TechCupException("Error: Payment record not found.");
        }
        if (pay.getReceiptUrl() == null || pay.getReceiptUrl().isEmpty()) {
            throw new TechCupException("Error: Cannot review payment without a valid receipt image.");
        }
        
        RecordAudit.createAudit("Reviewing payment ID: " + pay.getId(), this);
    }

    public void updatePaymentStatus(Pay pay, StatePay status, String reason) {
        if (pay == null || status == null) {
            throw new TechCupException("Error: Payment and status are required.");
        }
        //UPDATE, CHANGED STATUS FROM STR TO STATUSPAY!
        pay.updateStatus(status, reason);
        RecordAudit.createAudit("Payment ID " + pay.getId() + " updated to " + status, this);
    }

    // --- TOURNAMENT LIFECYCLE ---


    public Tournament createTournament(String name, java.time.LocalDate start, java.time.LocalDate end, int maxTeams, int players, double cost) {
        if (name == null || name.isEmpty()) throw new TechCupException("Tournament name cannot be empty.");
        
        Tournament newTournament = new Tournament(name, start, end, maxTeams, players, cost);
        
        RecordAudit.createAudit("Organizer " + this.getName() + " created tournament: " + name, this);
        
        return newTournament;
    }

    public void startTournament(Tournament tournament) {
        if (tournament == null) throw new TechCupException("Tournament not found.");
        
        if ("STARTED".equals(tournament.getStatus())) {
            throw new TechCupException("Tournament is already in progress.");
        }
        
        if (tournament.getTeams().size() < 2) {
            throw new TechCupException("Cannot start: At least 2 teams are required.");
        }

        tournament.setStatus("STARTED");
        RecordAudit.createAudit("Tournament " + tournament.getName() + " STARTED", this);
    }

    /**
     * Registers the official result of a match.
     * Uses the internal logic of the Game class to ensure integrity.
     */
    public void registerMatchResult(Match match, int scoreA, int scoreB) {
        if (match == null) {
            throw new TechCupException("Match not found.");
        }
        //DELEGATES TO GAME
        match.recordResult(scoreA, scoreB);
        
        // CLOSES THE MATCH
        boolean finalized = match.endGame();
        
        if (finalized) {
            // AUDIT
            RecordAudit.createAudit("OFFICIAL_RESULT: Match " + match.getId() + 
                                    " finalized. Score: " + scoreA + "-" + scoreB, this);
        }
    }

    public void finishTournament(Tournament tournament) {
        if (tournament == null) throw new TechCupException("Tournament not found.");
        
        if (!"STARTED".equals(tournament.getStatus())) {
            throw new TechCupException("Error: Only started tournaments can be finished.");
        }

        tournament.setStatus("FINISHED");
        RecordAudit.createAudit("Tournament " + tournament.getName() + " FINISHED", this);
    }
}