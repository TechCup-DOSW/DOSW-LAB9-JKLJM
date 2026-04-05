package edu.eci.dosw.techcup_futbol.mapper;

import edu.eci.dosw.techcup_futbol.entity.TournamentEntity;
import edu.eci.dosw.techcup_futbol.model.Competences.Tournament;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TournamentMapper {

    default TournamentEntity toEntity(Tournament model) {
        if (model == null) {
            return null;
        }

        TournamentEntity entity = new TournamentEntity();
        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setStartDate(model.getStartDate());
        entity.setEndDate(model.getEndDate());
        entity.setMaxTeams(model.getMaxTeams());
        entity.setPlayersPerTeam(model.getPlayersPerTeam());
        entity.setCostPerTeam(model.getCostPerTeam());
        entity.setStatus(model.getStatus());
        entity.setRules(model.getRules());
        entity.setAllowedFaculties(model.getAllowedFaculties());
        return entity;
    }

    default Tournament toModel(TournamentEntity entity) {
        if (entity == null) {
            return null;
        }

        Tournament model = new Tournament(
                entity.getId(),
                entity.getName(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getMaxTeams(),
                entity.getPlayersPerTeam(),
                entity.getCostPerTeam(),
                entity.getStatus(),
                entity.getRules()
        );

        if (entity.getAllowedFaculties() != null) {
            for (String faculty : entity.getAllowedFaculties()) {
                model.allowFaculty(faculty);
            }
        }

        if (entity.getAvailableSchedule() != null) {
            for (String slot : entity.getAvailableSchedule()) {
                model.addSchedule(slot);
            }
        }

        return model;
    }
}
