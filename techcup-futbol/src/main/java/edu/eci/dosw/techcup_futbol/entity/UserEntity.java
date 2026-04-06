package edu.eci.dosw.techcup_futbol.entity;

import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.FetchType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Persistent user entity for authentication.
 *
 * Represents the data structure queried by the repository
 * during login validation.
 */
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

            @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE })
        @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
        )
        private Set<RoleEntity> roles = new LinkedHashSet<>();

    public UserEntity() {
    }

    public UserEntity(String name, String email, String password, UserRole role) {
        this.name = name;
        this.email = normalizeEmail(email);
        this.password = password;
        setRole(role);
    }

    public UserEntity(String name, String email, String password, Set<RoleEntity> roles) {
        this.name = name;
        this.email = normalizeEmail(email);
        this.password = password;
        setRoles(roles);
    }

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
        this.email = normalizeEmail(email);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<RoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleEntity> roles) {
        this.roles = roles == null ? new LinkedHashSet<>() : new LinkedHashSet<>(roles);
    }

    public Set<PermissionEntity> getPermissions() {
        return roles.stream()
                .flatMap(role -> role.getPermissions().stream())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * Compatibility helper for single-role code paths.
     */
    public UserRole getRole() {
        if (roles == null || roles.isEmpty()) {
            return null;
        }

        String name = roles.iterator().next().getName();
        if (name == null || name.isBlank()) {
            return null;
        }

        try {
            return UserRole.valueOf(name);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    /**
     * Compatibility helper for single-role code paths.
     */
    public void setRole(UserRole role) {
        this.roles.clear();
        if (role != null) {
            RoleEntity roleEntity = new RoleEntity();
            roleEntity.setName(role.name());
            this.roles.add(roleEntity);
        }
    }

    /**
     * Normalizes email to keep storage and lookups consistent.
     */
    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }
}
