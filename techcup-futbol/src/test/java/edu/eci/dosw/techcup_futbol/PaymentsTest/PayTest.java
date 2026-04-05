package edu.eci.dosw.techcup_futbol.PaymentsTest;

import edu.eci.dosw.techcup_futbol.model.Payments.Pay;
import edu.eci.dosw.techcup_futbol.model.Payments.StatePay;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the {@link Pay} class.
 *
 * These tests validate the correct behavior of the payment entity,
 * including the default values assigned when a payment is created
 * and the correct state transitions when the payment status changes.
 *
 * The tests follow the TDD philosophy, ensuring that the Pay class
 * behaves as expected according to the business rules defined
 * for tournament payment management.
 */
class PayTest {

    /**
     * Verifies that a newly created payment has the expected default values.
     *
     * Expected behavior:
     * - The payment ID is correctly assigned.
     * - The receipt URL (voucher) is stored.
     * - The payment state starts as PENDING.
     * - The payment is not approved initially.
     * - The description contains the default review message.
     */
    @Test
    void shouldCreatePayWithPendingStateByDefault() {
        Pay pay = new Pay(1, "voucher-001");

        assertEquals(1, pay.getId());
        assertEquals("voucher-001", pay.getReceiptUrl());
        assertEquals(StatePay.PENDING, pay.getState());
        assertFalse(pay.isApprove());
        assertEquals("Pending initial review", pay.getDescription());
    }

    /**
     * Verifies that updating the payment status to APPROVED
     * updates the internal state correctly.
     *
     * Expected behavior:
     * - The state changes to APPROVED.
     * - The approval flag becomes true.
     * - The organizer's comment becomes the payment description.
     */
    @Test
    void shouldUpdateStatusToApproved() {
        Pay pay = new Pay(2, "voucher-002");

        pay.updateStatus(StatePay.APPROVED, "Payment approved by organizer");

        assertEquals(StatePay.APPROVED, pay.getState());
        assertTrue(pay.isApprove());
        assertEquals("Payment approved by organizer", pay.getDescription());
    }

    /**
     * Verifies that updating the payment status to REJECTED
     * updates the internal state correctly.
     *
     * Expected behavior:
     * - The state changes to REJECTED.
     * - The approval flag remains false.
     * - The rejection reason is stored in the description.
     */
    @Test
    void shouldUpdateStatusToRejected() {
        Pay pay = new Pay(3, "voucher-003");

        pay.updateStatus(StatePay.REJECTED, "Voucher rejected");

        assertEquals(StatePay.REJECTED, pay.getState());
        assertFalse(pay.isApprove());
        assertEquals("Voucher rejected", pay.getDescription());
    }

    /**
     * Verifies that the payment identifier can be retrieved correctly.
     */
    @Test
    void shouldReturnPaymentId() {
        Pay pay = new Pay(4, "voucher-004");

        assertEquals(4, pay.getId());
    }

    /**
     * Verifies that the receipt URL or voucher reference
     * associated with the payment is returned correctly.
     */
    @Test
    void shouldReturnReceiptUrl() {
        Pay pay = new Pay(5, "voucher-005");

        assertEquals("voucher-005", pay.getReceiptUrl());
    }
}