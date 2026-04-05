package edu.eci.dosw.techcup_futbol.model.Payments;

/**
 * Represents a payment transaction for tournament registration.
 */
public class Pay {
    private int id;
    private String voucher;
    private StatePay state; // Enum (PENDING, APPROVED, etc.)
    private boolean status; // Access control flag
    private String description;

    public Pay(int id, String voucher) {
        this.id = id;
        this.voucher = voucher;
        this.state = StatePay.PENDING;
        this.status = false;
        this.description = "Pending initial review";
    }

    /**
     * Updates the complete payment status.
     * @param newState The new Enum state.
     * @param organizerComment The description provided by the Organizer.
     */
    public void updateStatus(StatePay newState, String organizerComment) {
        this.state = newState;
        // The boolean flag is automatically updated based on the Enum
        this.status = newState.isFinalApproved(); 
        this.description = organizerComment;
    }

    // --- GETTERS ---

    public StatePay getState() {
        return this.state;
    }

    public boolean isApprove() {
        return this.status;
    }

    public String getDescription() {
        return this.description;
    }

    public int getId() {
        return this.id;
    }

    public String getReceiptUrl() {
        return this.voucher;
    }
}