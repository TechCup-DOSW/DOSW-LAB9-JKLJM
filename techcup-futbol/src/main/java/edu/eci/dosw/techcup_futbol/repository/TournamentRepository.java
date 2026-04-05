package edu.eci.dosw.techcup_futbol.repository;

import edu.eci.dosw.techcup_futbol.entity.TournamentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository FOR Tournament.
 * - save(Tournament): SAVES OR UPDATES
 * - findAll(): LISTS ALL TPURNAMENTS
 * - findById(Long): SEARCHES BY ITS PRIMARY KEY
 * - deleteById(Long): DELETE TOURNAMENT
 */
@Repository
public interface TournamentRepository extends JpaRepository<TournamentEntity, Long> {
    //IF MORE METHODS NEEDED
}