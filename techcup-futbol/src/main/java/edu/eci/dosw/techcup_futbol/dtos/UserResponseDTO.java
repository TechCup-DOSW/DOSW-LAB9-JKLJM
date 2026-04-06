package edu.eci.dosw.techcup_futbol.dtos;

import java.util.ArrayList;
import java.util.List;

// DTO for user registration response (output without password)
public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    private List<String> roles;
    private List<String> permissions;

    // Default constructor
    public UserResponseDTO() {
    }

    // Constructor with all fields
    public UserResponseDTO(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.roles = new ArrayList<>();
        this.permissions = new ArrayList<>();
    }

    public UserResponseDTO(Long id, String name, String email, List<String> roles, List<String> permissions) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.roles = roles == null ? new ArrayList<>() : roles;
        this.permissions = permissions == null ? new ArrayList<>() : permissions;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
}
