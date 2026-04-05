package edu.eci.dosw.techcup_futbol.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.eci.dosw.techcup_futbol.entity.UserEntity;

/**
 * Data access repository for users.
 *
 * Encapsulates queries used by the authentication flow.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * Finds a user by email.
     */
    UserEntity findByEmail(String email);

    /**
     * Checks if a user exists by email.
     */
    boolean existsByEmail(String email);
}
