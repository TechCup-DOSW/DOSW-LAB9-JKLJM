package edu.eci.dosw.techcup_futbol.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Input DTO for login.
 *
 * Defines the contract sent by the frontend to the authentication endpoint.
 * Also keeps aliases for compatibility with "correo" and "contrasena".
 */
public class LoginDTO {

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe ser valido")
    @JsonAlias({ "correo" })
    private String email;

    @NotBlank(message = "La contrasena es obligatoria")
    @JsonAlias({ "contrasena" })
    private String password;

    public LoginDTO() {
    }

    public LoginDTO(String email, String password) {
        this.email = email;
        this.password = password;
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
}
