package edu.eci.dosw.techcup_futbol.controller;

import java.util.Map;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import edu.eci.dosw.techcup_futbol.dtos.LoginDTO;
import edu.eci.dosw.techcup_futbol.entity.UserEntity;
import edu.eci.dosw.techcup_futbol.service.AuthenticationService;

/**
 * REST controller for authentication.
 *
 * Exposes the login endpoint and delegates credential validation
 * to the authentication service.
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticacion", description = "Operaciones de autenticacion")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Authenticates a user with email and password.
     *
     * @param credentials login data sent by the frontend
     * @return response with authentication status and basic user data
     */
    @PostMapping("/login")
    @Operation(summary = "Login", description = "Autentica un usuario usando email y password")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginDTO credentials) {
        UserEntity authenticatedUser = authenticationService.authenticate(credentials);

        List<String> roles = authenticatedUser.getRoles().stream()
            .map(role -> role.getName())
            .sorted()
            .toList();

        List<String> permissions = authenticatedUser.getPermissions().stream()
            .map(permission -> permission.getName())
            .sorted()
            .toList();

        return ResponseEntity.ok(Map.of(
                "authenticated", true,
                "message", "Inicio de sesion exitoso",
                "user", Map.of(
                        "id", authenticatedUser.getId(),
                        "name", authenticatedUser.getName(),
                "email", authenticatedUser.getEmail(),
                "roles", roles,
                "permissions", permissions)));
    }
}
