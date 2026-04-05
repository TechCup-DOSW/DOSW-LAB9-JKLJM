package edu.eci.dosw.techcup_futbol.model.Payments;

public enum StatePay {
    PENDING(false),
    IN_REVIEW(false),
    APPROVED(true),
    REJECTED(false);

    private final boolean isFinalApproved;

    StatePay(boolean isFinalApproved) {
        this.isFinalApproved = isFinalApproved;
    }

    public boolean isFinalApproved() {
        return isFinalApproved;
    }
}