package edu.eci.dosw.techcup_futbol.mapper;

import edu.eci.dosw.techcup_futbol.entity.RoleEntity;
import edu.eci.dosw.techcup_futbol.entity.UserEntity;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.User;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.UserRole;
import org.mapstruct.Mapper;

import java.util.LinkedHashSet;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserMapper {

    default User toModel(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        int id = entity.getId() == null ? 0 : entity.getId().intValue();
        User model = new User(id, entity.getName(), entity.getEmail(), entity.getPassword());
        model.setRoles(toModelRoles(entity.getRoles()));
        return model;
    }

    default UserEntity toEntity(User model) {
        if (model == null) {
            return null;
        }

        UserEntity entity = new UserEntity();
        entity.setId((long) model.getId());
        entity.setName(model.getName());
        entity.setEmail(model.getEmail());
        entity.setPassword(model.getPassword());
        entity.setRoles(toEntityRoles(model.getRoles()));
        return entity;
    }

    private Set<UserRole> toModelRoles(Set<RoleEntity> roleEntities) {
        Set<UserRole> modelRoles = new LinkedHashSet<>();
        if (roleEntities == null) {
            return modelRoles;
        }

        for (RoleEntity roleEntity : roleEntities) {
            if (roleEntity == null || roleEntity.getName() == null) {
                continue;
            }

            try {
                modelRoles.add(UserRole.valueOf(roleEntity.getName()));
            } catch (IllegalArgumentException ignored) {
                // Ignore unknown database roles in domain mapping.
            }
        }

        return modelRoles;
    }

    private Set<RoleEntity> toEntityRoles(Set<UserRole> modelRoles) {
        Set<RoleEntity> roleEntities = new LinkedHashSet<>();
        if (modelRoles == null) {
            return roleEntities;
        }

        for (UserRole modelRole : modelRoles) {
            if (modelRole == null) {
                continue;
            }
            RoleEntity roleEntity = new RoleEntity();
            roleEntity.setName(modelRole.name());
            roleEntities.add(roleEntity);
        }

        return roleEntities;
    }
}
