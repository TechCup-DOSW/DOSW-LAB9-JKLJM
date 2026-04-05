package edu.eci.dosw.techcup_futbol.exceptions;

/**
 * Business exception for authentication errors.
 *
 * Thrown when the email does not exist or the password does not match.
 */
public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
