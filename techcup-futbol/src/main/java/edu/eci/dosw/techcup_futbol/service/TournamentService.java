package edu.eci.dosw.techcup_futbol.service;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.NoSuchElementException;

import edu.eci.dosw.techcup_futbol.dtos.TournamentCreateDTO;
import edu.eci.dosw.techcup_futbol.entity.TournamentEntity;
import edu.eci.dosw.techcup_futbol.mapper.TournamentMapper;
import edu.eci.dosw.techcup_futbol.model.Competences.Tournament;
import edu.eci.dosw.techcup_futbol.repository.TournamentRepository;

@Service
public class TournamentService {

    private static final Logger logger = LoggerFactory.getLogger(TournamentService.class);

    private final TournamentRepository tournamentRepository;
    private final TournamentMapper tournamentMapper;

    public TournamentService(TournamentRepository tournamentRepository, TournamentMapper tournamentMapper) {
        this.tournamentRepository = tournamentRepository;
        this.tournamentMapper = tournamentMapper;
    }

    public Tournament createTournament(TournamentCreateDTO dto) {
        logger.info("Starting tournament creation with name: {}", dto.getName());

        Tournament newTournament = new Tournament(
                dto.getName(),
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getMaxTeams(),
                dto.getPlayersPerTeam(),
                dto.getCostPerTeam()
        );

        TournamentEntity saved = tournamentRepository.save(tournamentMapper.toEntity(newTournament));
        logger.info("Tournament created successfully with id: {}", saved.getId());

        return tournamentMapper.toModel(saved);
    }

    public List<Tournament> getAllTournaments() {
        logger.info("Fetching all tournaments");
        return tournamentRepository.findAll().stream()
            .map(tournamentMapper::toModel)
                .toList();
    }

    public Tournament getTournamentById(Long id) {
        logger.info("Fetching tournament with id: {}", id);

        return tournamentRepository.findById(id)
                .map(tournamentMapper::toModel)
                .orElseThrow(() -> {
                    logger.error("Tournament not found with id: {}", id);
                    return new NoSuchElementException("No tournament found with id: " + id);
                });
    }

    public Tournament updateTournament(Long id, TournamentCreateDTO dto) {
        logger.info("Updating tournament with id: {}", id);

        Tournament existing = getTournamentById(id);

        existing.setName(dto.getName());
        existing.setTournamentDates(dto.getStartDate(), dto.getEndDate());
        existing.setMaxTeams(dto.getMaxTeams());
        existing.setPlayersPerTeam(dto.getPlayersPerTeam());
        existing.setCostPerTeam(dto.getCostPerTeam());

        TournamentEntity saved = tournamentRepository.save(tournamentMapper.toEntity(existing));
        logger.info("Tournament updated successfully with id: {}", id);

        return tournamentMapper.toModel(saved);
    }

    public void deleteTournament(Long id) {
        logger.info("Deleting tournament with id: {}", id);

        if (!tournamentRepository.existsById(id)) {
            logger.error("Cannot delete tournament. Tournament does not exist with id: {}", id);
            throw new NoSuchElementException("Cannot delete tournament. Tournament does not exist with id: " + id);
        }

        tournamentRepository.deleteById(id);
        logger.info("Tournament deleted successfully with id: {}", id);
    }
}
