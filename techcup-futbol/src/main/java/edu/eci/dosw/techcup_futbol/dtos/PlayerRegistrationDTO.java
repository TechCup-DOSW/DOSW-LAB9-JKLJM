package edu.eci.dosw.techcup_futbol.dtos;

import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.Rol;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.TypeUser;

/**
 * DTO used to receive player registration data from the client.
 */
public class PlayerRegistrationDTO {

    private String name;
    private String email;
    private String password;
    private int dorsal;
    private Rol position;
    private String photoUrl;
    private TypeUser typeUser;

    public PlayerRegistrationDTO() {
    }

    public PlayerRegistrationDTO(String name, String email, String password, int dorsal,
                                Rol position, String photoUrl, TypeUser typeUser) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.dorsal = dorsal;
        this.position = position;
        this.photoUrl = photoUrl;
        this.typeUser = typeUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getDorsal() {
        return dorsal;
    }

    public void setDorsal(int dorsal) {
        this.dorsal = dorsal;
    }

    public Rol getPosition() {
        return position;
    }

    public void setPosition(Rol position) {
        this.position = position;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public TypeUser getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(TypeUser typeUser) {
        this.typeUser = typeUser;
    }
}
