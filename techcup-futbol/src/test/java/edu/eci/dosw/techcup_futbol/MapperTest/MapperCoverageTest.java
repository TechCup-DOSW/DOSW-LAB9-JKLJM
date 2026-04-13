package edu.eci.dosw.techcup_futbol.MapperTest;

import edu.eci.dosw.techcup_futbol.entity.PermissionEntity;
import edu.eci.dosw.techcup_futbol.entity.RoleEntity;
import edu.eci.dosw.techcup_futbol.entity.TeamEntity;
import edu.eci.dosw.techcup_futbol.entity.UserEntity;
import edu.eci.dosw.techcup_futbol.mapper.TeamMapper;
import edu.eci.dosw.techcup_futbol.mapper.UserMapper;
import edu.eci.dosw.techcup_futbol.model.Teams.Team;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Player;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Rol;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.TypeUser;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.User;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.UserRole;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MapperCoverageTest {

    private final TeamMapper teamMapper = new TeamMapper() {
    };

    private final UserMapper userMapper = new UserMapper() {
    };

    @Test
    void shouldMapTeamEntityToModelWithCaptain() {
        UserEntity captainEntity = new UserEntity();
        captainEntity.setId(9L);
        captainEntity.setName("Cap");
        captainEntity.setEmail("cap@mail.com");
        captainEntity.setPassword("secret");

        TeamEntity entity = new TeamEntity();
        entity.setId(12L);
        entity.setName("Lions");
        entity.setCaptain(captainEntity);

        Team team = teamMapper.toModel(entity);

        assertNotNull(team);
        assertEquals(12, team.getId());
        assertEquals("Lions", team.getName());
        assertNotNull(team.getCaptain());
        assertEquals("Cap", team.getCaptain().getName());
        assertEquals(1, team.getPlayers().size());
    }

    @Test
    void shouldMapTeamModelToEntityAndHandleNullInputs() {
        assertNull(teamMapper.toModel(null));
        assertNull(teamMapper.toEntity(null));

        Player captain = new Player(7, "Jane", "jane@mail.com", "pwd", 10, Rol.CAPTAIN, TypeUser.STUDENT);
        Team team = new Team(22, "Tigers");
        team.addPlayer(captain);
        team.assignCap(captain);

        TeamEntity entity = teamMapper.toEntity(team);

        assertNotNull(entity);
        assertEquals(22L, entity.getId());
        assertEquals("Tigers", entity.getName());
        assertNotNull(entity.getCaptain());
        assertEquals("jane@mail.com", entity.getCaptain().getEmail());
        assertTrue(entity.getCaptain().getRoles().stream().anyMatch(role -> "PLAYER".equals(role.getName())));
    }

    @Test
    void shouldMapUserEntityToModelFilteringUnknownRoles() {
        RoleEntity adminRole = new RoleEntity();
        adminRole.setName("ADMIN");

        RoleEntity unknownRole = new RoleEntity();
        unknownRole.setName("LEGACY_UNKNOWN");

        UserEntity entity = new UserEntity();
        entity.setId(3L);
        entity.setName("Ana");
        entity.setEmail("ana@mail.com");
        entity.setPassword("pwd");
        LinkedHashSet<RoleEntity> roleEntities = new LinkedHashSet<>();
        roleEntities.add(adminRole);
        roleEntities.add(unknownRole);
        roleEntities.add(null);
        entity.setRoles(roleEntities);

        User model = userMapper.toModel(entity);

        assertNotNull(model);
        assertEquals(3, model.getId());
        assertEquals("Ana", model.getName());
        assertTrue(model.getRoles().contains(UserRole.ADMIN));
        assertEquals(1, model.getRoles().size());
    }

    @Test
    void shouldMapUserModelToEntityHandlingNullRolesAndNullModel() {
        assertNull(userMapper.toModel(null));
        assertNull(userMapper.toEntity(null));

        User withNullRoles = new User(8, "Null Roles", "null@mail.com", "x");
        withNullRoles.setRoles(null);
        UserEntity entityWithoutRoles = userMapper.toEntity(withNullRoles);
        assertNotNull(entityWithoutRoles.getRoles());
        assertTrue(entityWithoutRoles.getRoles().isEmpty());

        User multiRoleUser = new User(10, "Bob", "bob@mail.com", "pass");
        LinkedHashSet<UserRole> modelRoles = new LinkedHashSet<>();
        modelRoles.add(UserRole.ORGANIZER);
        modelRoles.add(null);
        modelRoles.add(UserRole.REFEREE);
        multiRoleUser.setRoles(modelRoles);

        UserEntity mapped = userMapper.toEntity(multiRoleUser);

        assertEquals(10L, mapped.getId());
        assertEquals("Bob", mapped.getName());
        assertEquals(2, mapped.getRoles().size());
        assertTrue(mapped.getRoles().stream().anyMatch(role -> "ORGANIZER".equals(role.getName())));
        assertTrue(mapped.getRoles().stream().anyMatch(role -> "REFEREE".equals(role.getName())));
    }

    @Test
    void shouldKeepRolePermissionsSettableInEntityHelpers() {
        PermissionEntity permission = new PermissionEntity();
        permission.setName("users_read");

        RoleEntity role = new RoleEntity();
        role.setName("organizer");
        role.setPermissions(Set.of(permission));

        assertEquals("ORGANIZER", role.getName());
        assertEquals(1, role.getPermissions().size());
        assertEquals("USERS_READ", role.getPermissions().iterator().next().getName());
    }
}
