package edu.eci.dosw.techcup_futbol.model.UsersAndSecurity;

import edu.eci.dosw.techcup_futbol.exceptions.TechCupException;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class responsible for managing users in the system.
 * It handles player registration, email validations, and lookup by email.
 */
public class UserService {

    private final List<User> users;

    /**
     * Default constructor.
     * Initializes an empty list of users.
     */
    public UserService() {
        this.users = new ArrayList<>();
    }

    /**
     * Constructor that receives an existing list of users.
     *
     * @param users list of users to initialize the service
     */
    public UserService(List<User> users) {
        this.users = users;
    }

    /**
     * Registers a new player in the system after validating their email.
     *
     * @param player the player to register
     * @throws TechCupException if the player is null or the email is invalid
     */
    public void registerPlayer(Player player) {
        if (player == null) {
            throw new TechCupException("Error: Player cannot be null.");
        }

        validateEmailFormat(player.getEmail());
        validateAllowedEmailByType(player.getEmail(), player.getTypeUser());
        validateUniqueEmail(player.getEmail());

        users.add(player);
    }

    /**
     * Validates that the email is not already registered in the system.
     *
     * @param email email to validate
     * @throws TechCupException if the email already exists
     */
    public void validateUniqueEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new TechCupException("Error: Email is required.");
        }

        if (findByEmail(email) != null) {
            throw new TechCupException("Error: Email is already registered.");
        }
    }

    /**
     * Validates that the email has a correct format.
     *
     * @param email email to validate
     * @throws TechCupException if the format is invalid
     */
    public void validateEmailFormat(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new TechCupException("Error: Email is required.");
        }

        String normalizedEmail = email.trim().toLowerCase();

        if (!normalizedEmail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new TechCupException("Error: Invalid email format.");
        }
    }

    /**
     * Validates that the email domain is allowed depending on the user type.
     *
     * Institutional users must use @mail.escuelaing.edu.co.
     * Family members must use @gmail.com.
     *
     * @param email user email
     * @param typeUser type of user
     * @throws TechCupException if the email domain is not allowed
     */
    public void validateAllowedEmailByType(String email, TypeUser typeUser) {
        if (email == null || email.trim().isEmpty()) {
            throw new TechCupException("Error: Email is required.");
        }

        if (typeUser == null) {
            throw new TechCupException("Error: User type is required.");
        }

        String normalizedEmail = email.trim().toLowerCase();

        switch (typeUser) {
            case STUDENT:
            case GRADUATE:
            case PROFESSOR:
            case ADMINISTRATIVE:
                if (!normalizedEmail.endsWith("@mail.escuelaing.edu.co")) {
                    throw new TechCupException(
                            "Error: Students, graduates, professors, and administrative staff must use an institutional email."
                    );
                }
                break;

            case FAMILY_MEMBER:
                if (!normalizedEmail.endsWith("@gmail.com")) {
                    throw new TechCupException(
                            "Error: Family members must use a Gmail account."
                    );
                }
                break;

            default:
                throw new TechCupException("Error: Unsupported user type.");
        }
    }

    /**
     * Finds a user by email.
     *
     * @param email email to search
     * @return the user if found, otherwise null
     */
    public User findByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }

        String normalizedEmail = email.trim().toLowerCase();

        return users.stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(normalizedEmail))
                .findFirst()
                .orElse(null);
    }

    /**
     * Checks if an email is already registered in the system.
     *
     * @param email email to check
     * @return true if the email exists, false otherwise
     */
    public boolean emailExists(String email) {
        return findByEmail(email) != null;
    }

    /**
     * Returns the list of registered users.
     *
     * @return list of users
     */
    public List<User> getUsers() {
        return users;
    }
}
