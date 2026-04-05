package edu.eci.dosw.techcup_futbol.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import edu.eci.dosw.techcup_futbol.dtos.RegisterUserDTO;
import edu.eci.dosw.techcup_futbol.dtos.UserResponseDTO;
import edu.eci.dosw.techcup_futbol.service.UserRegistrationService;

@RestController
@RequestMapping("/api/register")
@CrossOrigin(origins = "*")
@Tag(name = "User Registration", description = "Endpoints for user registration, retrieval, update and deletion")
public class UserRegistrationController {

    private final UserRegistrationService userRegistrationService;

    public UserRegistrationController(UserRegistrationService userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
    }

    @PostMapping
    @Operation(summary = "Register a new user", description = "Creates a new user account with name, email, and password. The email must be unique.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data or email already registered")
    })
    public ResponseEntity<?> registerUser(
            @org.springframework.web.bind.annotation.RequestBody RegisterUserDTO registerUserDTO) {
        try {
            UserResponseDTO user = userRegistrationService.registerUser(registerUserDTO);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieves a specific user by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<?> getUserById(
            @Parameter(description = "User ID") @PathVariable Long id) {
        try {
            UserResponseDTO user = userRegistrationService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieves a list of all registered users in the system")
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
            content = @Content(mediaType = "application/json"))
    public ResponseEntity<?> getAllUsers() {
        try {
            List<UserResponseDTO> users = userRegistrationService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/by-email")
    @Operation(summary = "Get user by email", description = "Retrieves a specific user by their email address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "User with this email not found")
    })
    public ResponseEntity<?> getUserByEmail(
            @Parameter(description = "Email address to search for") @RequestParam String email) {
        try {
            UserResponseDTO user = userRegistrationService.getUserByEmail(email);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/exists")
    @Operation(summary = "Check if email exists", description = "Verifies whether an email address is already registered in the system")
    @ApiResponse(responseCode = "200", description = "Email availability check completed",
            content = @Content(mediaType = "application/json"))
    public ResponseEntity<?> existsByEmail(
            @Parameter(description = "Email address to check") @RequestParam String email) {
        try {
            boolean exists = userRegistrationService.existsByEmail(email);
            return ResponseEntity.ok(Map.of("email", email, "exists", exists));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Updates an existing user's information by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<?> updateUser(
            @Parameter(description = "User ID") @PathVariable Long id,
            @org.springframework.web.bind.annotation.RequestBody RegisterUserDTO registerUserDTO) {
        try {
            UserResponseDTO updatedUser = userRegistrationService.updateUser(id, registerUserDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Removes a user from the system permanently by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<?> deleteUser(
            @Parameter(description = "User ID") @PathVariable Long id) {
        try {
            userRegistrationService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
