package edu.eci.dosw.techcup_futbol.model.UsersAndSecurity;


import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class User {
    protected String name;
    protected String email;
    protected String password;
    protected int id;
    protected UserRole role;
    protected Set<UserRole> roles;

    public User(int id, String name, String email, String password) {
        this(id, name, email, password, UserRole.PLAYER);
    }

    public User(int id, String name, String email, String password, UserRole role) {
        this.id = id;
        this.name = name;
        this.email = email.trim().toLowerCase();
        this.password = password;
        this.role = role;
        this.roles = new LinkedHashSet<>();
        if (role != null) {
            this.roles.add(role);
        }
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
        this.email = email.trim().toLowerCase();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId(){
        return this.id;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
        this.roles.clear();
        if (role != null) {
            this.roles.add(role);
        }
    }

    public Set<UserRole> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles == null ? new LinkedHashSet<>() : new LinkedHashSet<>(roles);
        this.role = this.roles.stream().findFirst().orElse(null);
    }

    public Set<Permission> getPermissions() {
        Set<Permission> permissions = EnumSet.noneOf(Permission.class);
        for (UserRole userRole : roles) {
            permissions.addAll(userRole.getPermissions());
        }
        return Collections.unmodifiableSet(permissions);
    }

    public boolean hasRole(UserRole role) {
        return role != null && roles.contains(role);
    }

    public boolean hasPermission(Permission permission) {
        return permission != null && getPermissions().contains(permission);
    }

    public void addRole(UserRole role) {
        if (role == null) {
            return;
        }
        roles.add(role);
        this.role = this.roles.stream().findFirst().orElse(null);
    }

    public void removeRole(UserRole role) {
        if (role == null) {
            return;
        }
        roles.remove(role);
        this.role = this.roles.stream().findFirst().orElse(null);
    }
}
