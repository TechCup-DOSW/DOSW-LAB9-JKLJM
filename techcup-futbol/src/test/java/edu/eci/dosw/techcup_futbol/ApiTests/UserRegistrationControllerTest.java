package edu.eci.dosw.techcup_futbol.ApiTests;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.eci.dosw.techcup_futbol.dtos.RegisterUserDTO;
import edu.eci.dosw.techcup_futbol.dtos.UserResponseDTO;
import edu.eci.dosw.techcup_futbol.service.UserRegistrationService;

// Test compatible with Spring Boot 4.0.3
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserRegistrationControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockitoBean
    private UserRegistrationService userRegistrationService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Initialize MockMvc with application context
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        objectMapper = new ObjectMapper();
    }


    // Test successful user registration
    @Test
    public void shouldRegisterUserSuccessfully() throws Exception {
        // Prepare test data
        RegisterUserDTO registerDTO = new RegisterUserDTO("John Doe", "john@example.com", "password123");
        UserResponseDTO responseDTO = new UserResponseDTO(1L, "John Doe", "john@example.com");

        // Mock service behavior
        when(userRegistrationService.registerUser(any(RegisterUserDTO.class))).thenReturn(responseDTO);

        // Send POST request and assert response with 201 CREATED status
        mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isCreated());
    }

    // Test user registration with invalid input
    @Test
    public void shouldReturnBadRequestWhenRegistrationFails() throws Exception {
        // Prepare test data with missing email
        RegisterUserDTO registerDTO = new RegisterUserDTO("John Doe", "", "password123");

        // Mock service to throw exception
        when(userRegistrationService.registerUser(any(RegisterUserDTO.class)))
                .thenThrow(new RuntimeException("Email is required"));

        // Send POST request and assert 400 BAD REQUEST response
        mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isBadRequest());
    }

    // Test retrieve user by ID
    @Test
    public void shouldReturnUserById() throws Exception {
        // Prepare test data
        UserResponseDTO responseDTO = new UserResponseDTO(1L, "John Doe", "john@example.com");

        // Mock service behavior
        when(userRegistrationService.getUserById(1L)).thenReturn(responseDTO);

        // Send GET request and assert 200 OK response
        mockMvc.perform(get("/api/register/1"))
                .andExpect(status().isOk());
    }

    // Test retrieve user with non-existent ID
    @Test
    public void shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        // Mock service to throw exception
        when(userRegistrationService.getUserById(999L))
                .thenThrow(new RuntimeException("User not found with id: 999"));

        // Send GET request and assert 404 NOT FOUND response
        mockMvc.perform(get("/api/register/999"))
                .andExpect(status().isNotFound());
    }

        @Test
        public void shouldReturnAllUsers() throws Exception {
        List<UserResponseDTO> users = List.of(
            new UserResponseDTO(1L, "John Doe", "john@example.com"),
            new UserResponseDTO(2L, "Jane Doe", "jane@example.com")
        );

        when(userRegistrationService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/register"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[1].id").value(2));
        }

        @Test
        public void shouldReturnUserByEmail() throws Exception {
        UserResponseDTO responseDTO = new UserResponseDTO(1L, "John Doe", "john@example.com");

        when(userRegistrationService.getUserByEmail("john@example.com")).thenReturn(responseDTO);

        mockMvc.perform(get("/api/register/by-email").param("email", "john@example.com"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("john@example.com"));
        }

        @Test
        public void shouldReturnNotFoundWhenEmailDoesNotExist() throws Exception {
        when(userRegistrationService.getUserByEmail("notfound@example.com"))
            .thenThrow(new RuntimeException("User not found with email: notfound@example.com"));

        mockMvc.perform(get("/api/register/by-email").param("email", "notfound@example.com"))
            .andExpect(status().isNotFound());
        }

        @Test
        public void shouldReturnExistsByEmail() throws Exception {
        when(userRegistrationService.existsByEmail("john@example.com")).thenReturn(true);

        mockMvc.perform(get("/api/register/exists").param("email", "john@example.com"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("john@example.com"))
            .andExpect(jsonPath("$.exists").value(true));
        }

        @Test
        public void shouldUpdateUserSuccessfully() throws Exception {
        RegisterUserDTO request = new RegisterUserDTO("John Updated", "john@example.com", "newpass");
        UserResponseDTO response = new UserResponseDTO(1L, "John Updated", "john@example.com");

        when(userRegistrationService.updateUser(eq(1L), any(RegisterUserDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/register/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("John Updated"));
        }

        @Test
        public void shouldReturnBadRequestWhenUpdateFails() throws Exception {
        RegisterUserDTO request = new RegisterUserDTO("", "john@example.com", "newpass");

        when(userRegistrationService.updateUser(eq(1L), any(RegisterUserDTO.class)))
            .thenThrow(new RuntimeException("Name is required"));

        mockMvc.perform(put("/api/register/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
        }

        @Test
        public void shouldDeleteUserSuccessfully() throws Exception {
        doNothing().when(userRegistrationService).deleteUser(1L);

        mockMvc.perform(delete("/api/register/1"))
            .andExpect(status().isNoContent());
        }

        @Test
        public void shouldReturnNotFoundWhenDeleteFails() throws Exception {
        doThrow(new RuntimeException("User not found with id: 999"))
            .when(userRegistrationService)
            .deleteUser(999L);

        mockMvc.perform(delete("/api/register/999"))
            .andExpect(status().isNotFound());
        }
}
