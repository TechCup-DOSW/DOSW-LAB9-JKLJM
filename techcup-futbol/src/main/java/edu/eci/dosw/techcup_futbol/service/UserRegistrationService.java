package edu.eci.dosw.techcup_futbol.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.eci.dosw.techcup_futbol.dtos.RegisterUserDTO;
import edu.eci.dosw.techcup_futbol.dtos.UserResponseDTO;
import edu.eci.dosw.techcup_futbol.entity.UserEntity;
import edu.eci.dosw.techcup_futbol.exceptions.TechCupException;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.UserRole;
import edu.eci.dosw.techcup_futbol.repository.UserRepository;

// Service for user registration and retrieval operations
@Service
public class UserRegistrationService {

    private final UserRepository userRepository;

    public UserRegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Register a new user with validation
    public UserResponseDTO registerUser(RegisterUserDTO dto) {
        // Validate input data
        validateRegistrationData(dto);
        String normalizedEmail = dto.getEmail().trim().toLowerCase();

        // Check if email already exists in database
        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new TechCupException("Email is already registered");
        }

        // Create new UserEntity from DTO
        UserEntity user = new UserEntity(
                dto.getName().trim(),
                normalizedEmail,
            dto.getPassword(),
            UserRole.PLAYER
        );

        // Save user to database and convert to response DTO
        UserEntity savedUser = userRepository.save(user);
        return toResponse(savedUser);
    }

    // Retrieve a user by its ID
    public UserResponseDTO getUserById(Long id) {
        // Find user or throw exception if not found
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new TechCupException("User not found with id: " + id));
        return toResponse(user);
    }

    // Retrieve all registered users
    public List<UserResponseDTO> getAllUsers() {
        List<UserResponseDTO> users = userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
        return users;
    }

    // Retrieve a user by email
    public UserResponseDTO getUserByEmail(String email) {
        String normalizedEmail = normalizeEmail(email);
        UserEntity user = userRepository.findByEmail(normalizedEmail);
        if (user == null) {
            throw new TechCupException("User not found with email: " + normalizedEmail);
        }
        return toResponse(user);
    }

    // Check whether an email is already registered
    public boolean existsByEmail(String email) {
        String normalizedEmail = normalizeEmail(email);
        boolean exists = userRepository.existsByEmail(normalizedEmail);
        return exists;
    }

    // Update user registration data
    public UserResponseDTO updateUser(Long id, RegisterUserDTO dto) {
        validateRegistrationData(dto);

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new TechCupException("User not found with id: " + id));

        String normalizedEmail = normalizeEmail(dto.getEmail());
        UserEntity userWithEmail = userRepository.findByEmail(normalizedEmail);
        if (userWithEmail != null && !userWithEmail.getId().equals(id)) {
            throw new TechCupException("Email is already registered");
        }

        user.setName(dto.getName().trim());
        user.setEmail(normalizedEmail);
        user.setPassword(dto.getPassword());

        UserEntity savedUser = userRepository.save(user);
        return toResponse(savedUser);
    }

    // Delete an existing registered user
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new TechCupException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    // Validate required fields for registration
    private void validateRegistrationData(RegisterUserDTO dto) {
        if (dto == null) {
            throw new TechCupException("Request body is required");
        }
        if (isBlank(dto.getName())) {
            throw new TechCupException("Name is required");
        }
        if (isBlank(dto.getEmail())) {
            throw new TechCupException("Email is required");
        }
        if (isBlank(dto.getPassword())) {
            throw new TechCupException("Password is required");
        }
    }

    // Check if string is null or empty
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String normalizeEmail(String email) {
        if (isBlank(email)) {
            throw new TechCupException("Email is required");
        }
        return email.trim().toLowerCase();
    }

    // Convert UserEntity to UserResponseDTO (excludes password)
    private UserResponseDTO toResponse(UserEntity userEntity) {
        return new UserResponseDTO(
                userEntity.getId(),
                userEntity.getName(),
                userEntity.getEmail()
        );
    }
}
