package edu.eci.dosw.techcup_futbol.AuthenticationTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import edu.eci.dosw.techcup_futbol.controller.AuthenticationController;
import edu.eci.dosw.techcup_futbol.dtos.LoginDTO;
import edu.eci.dosw.techcup_futbol.entity.UserEntity;
import edu.eci.dosw.techcup_futbol.exceptions.GlobalExceptionHandler;
import edu.eci.dosw.techcup_futbol.exceptions.InvalidCredentialsException;
import edu.eci.dosw.techcup_futbol.model.UsersAndSecurity.UserRole;
import edu.eci.dosw.techcup_futbol.service.AuthenticationService;

class AuthenticationControllerTest {

    private MockMvc mockMvc;
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        authenticationService = Mockito.mock(AuthenticationService.class);
        AuthenticationController authenticationController = new AuthenticationController(authenticationService);
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldReturnOkWhenLoginIsSuccessful() throws Exception {
        UserEntity user = new UserEntity("Admin", "admin@techcup.com", "encoded-admin", UserRole.ADMIN);
        user.setId(1L);
        when(authenticationService.authenticate(any(LoginDTO.class))).thenReturn(user);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"admin@techcup.com\",\"password\":\"admin123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authenticated").value(true))
                .andExpect(jsonPath("$.user.email").value("admin@techcup.com"));
    }

    @Test
    void shouldReturnUnauthorizedWhenLoginFails() throws Exception {
        when(authenticationService.authenticate(any(LoginDTO.class)))
                .thenThrow(new InvalidCredentialsException("Correo o contrasena invalidos"));

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"admin@techcup.com\",\"password\":\"bad-pass\"}"))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.status").value(500))
            .andExpect(jsonPath("$.error").value("Internal Server Error"))
            .andExpect(jsonPath("$.message").value("Unexpected server error"));
    }

    @Test
    void shouldAcceptCorreoAliasFromRequestBody() throws Exception {
        UserEntity user = new UserEntity("Admin", "admin@techcup.com", "encoded-admin", UserRole.ADMIN);
        user.setId(1L);
        when(authenticationService.authenticate(any(LoginDTO.class))).thenReturn(user);

        String body = "{\"correo\":\"admin@techcup.com\",\"contrasena\":\"admin123\"}";

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authenticated").value(true));
    }

    @Test
    void shouldReturnBadRequestWhenRequestHasMissingFields() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"\",\"password\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }
}
