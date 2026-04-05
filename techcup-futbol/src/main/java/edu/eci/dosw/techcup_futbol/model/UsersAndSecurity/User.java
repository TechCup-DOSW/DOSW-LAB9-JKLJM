package edu.eci.dosw.techcup_futbol.model.UsersAndSecurity;

public abstract class User {
    protected String name;
    protected String email;
    protected String password;
    protected int id;
    protected UserRole role;

    public User(int id, String name, String email, String password) {
        this(id, name, email, password, UserRole.PLAYER);
    }

    public User(int id, String name, String email, String password, UserRole role) {
        this.id = id;
        this.name = name;
        this.email = email.trim().toLowerCase();
        this.password = password;
        this.role = role;
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
    }
}
