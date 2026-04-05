package edu.eci.dosw.techcup_futbol.mapper;

import edu.eci.dosw.techcup_futbol.entity.UserEntity;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Admin;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Player;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Organizer;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Referee;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Rol;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.TypeUser;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.User;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.UserRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    default User toModel(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        int id = entity.getId() == null ? 0 : entity.getId().intValue();
        String name = entity.getName();
        String email = entity.getEmail();
        String password = entity.getPassword();
        UserRole role = entity.getRole();

        if (role == null) {
            return new Player(id, name, email, password, 1, Rol.DEFENDER, TypeUser.STUDENT);
        }

        return switch (role) {
            case ADMIN -> new Admin(id, name, email, password);
            case ORGANIZER -> new Organizer(id, name, email, password);
            case REFEREE -> new Referee(id, name, email, password, null);
            case PLAYER -> new Player(id, name, email, password, 1, Rol.DEFENDER, TypeUser.STUDENT);
        };
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
        entity.setRole(model.getRole());
        return entity;
    }
}
