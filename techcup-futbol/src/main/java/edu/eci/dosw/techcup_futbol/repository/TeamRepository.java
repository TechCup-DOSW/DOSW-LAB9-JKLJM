package edu.eci.dosw.techcup_futbol.repository;

import edu.eci.dosw.techcup_futbol.entity.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Data access repository for teams.
 */
@Repository
public interface TeamRepository extends JpaRepository<TeamEntity, Long> {

    /**
     * Finds teams where name contains the provided value, ignoring case.
     */
    List<TeamEntity> findByNameContainingIgnoreCase(String name);
}
