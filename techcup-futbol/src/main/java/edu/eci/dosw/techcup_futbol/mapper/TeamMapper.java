package edu.eci.dosw.techcup_futbol.mapper;

import edu.eci.dosw.techcup_futbol.entity.TeamEntity;
import edu.eci.dosw.techcup_futbol.entity.UserEntity;
import edu.eci.dosw.techcup_futbol.entity.RoleEntity;
import edu.eci.dosw.techcup_futbol.model.Teams.Team;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    default Team toModel(TeamEntity entity) {
        if (entity == null) {
            return null;
        }

        int id = entity.getId() == null ? 0 : entity.getId().intValue();
        Team team = new Team(id, entity.getName());

        if (entity.getCaptain() != null) {
            edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Player captain = toPlayer(entity.getCaptain());
            team.addPlayer(captain);
            team.assignCap(captain);
        }

        return team;
    }

    default TeamEntity toEntity(Team model) {
        if (model == null) {
            return null;
        }

        TeamEntity entity = new TeamEntity();
        entity.setId((long) model.getId());
        entity.setName(model.getName());
        if (model.getCaptain() != null) {
            entity.setCaptain(toUserEntity(model.getCaptain()));
        }
        return entity;
    }

    private edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Player toPlayer(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        int id = entity.getId() == null ? 0 : entity.getId().intValue();
        return new edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Player(
                id,
                entity.getName(),
                entity.getEmail(),
                entity.getPassword(),
                1,
                edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Rol.CAPTAIN,
                edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.TypeUser.STUDENT
        );
    }

    private UserEntity toUserEntity(edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Player player) {
        if (player == null) {
            return null;
        }

        UserEntity entity = new UserEntity();
        entity.setId((long) player.getId());
        entity.setName(player.getName());
        entity.setEmail(player.getEmail());
        entity.setPassword(player.getPassword());
        RoleEntity playerRole = new RoleEntity();
        playerRole.setName("PLAYER");
        entity.setRoles(Set.of(playerRole));
        return entity;
    }
}
